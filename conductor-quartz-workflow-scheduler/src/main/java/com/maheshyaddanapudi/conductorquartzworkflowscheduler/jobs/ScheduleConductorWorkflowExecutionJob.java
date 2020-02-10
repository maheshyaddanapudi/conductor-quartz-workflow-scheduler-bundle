package com.maheshyaddanapudi.conductorquartzworkflowscheduler.jobs;
import java.io.Serializable;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.maheshyaddanapudi.conductorquartzworkflowscheduler.constants.ConductorQuartzWorkflowSchedulerConstants;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.ConductorQuartzHistory;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.ConductorQuartzMapping;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.repository.ConductorQuartzHistoryRepository;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.repository.ConductorQuartzMappingRepository;

@Component
public class ScheduleConductorWorkflowExecutionJob extends QuartzJobBean implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -875336427502820536L;

	private static final Logger logger = LoggerFactory.getLogger(ScheduleConductorWorkflowExecutionJob.class);
	
	private String runLog = "";
	
	@Value("${conductor.server.api.endpoint:http://localhost:8080/api}")
	private String CONDUCTOR_SERVER_API_ENDPOINT; 

    @Autowired
    RestTemplate restTemplate;
    
    @Autowired
    ConductorQuartzMappingRepository conductorQuartzMappingRepository;
    
    @Autowired
    ConductorQuartzHistoryRepository conductorQuartzHistoryRepository;
    
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    	
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        
        String schedulerId = jobExecutionContext.getJobDetail().getKey().toString();
        
        logger.info("schedulerId: " + schedulerId);
        
        ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(schedulerId.replaceAll(ConductorQuartzWorkflowSchedulerConstants.Conductor+".", ""));
        ConductorQuartzHistory conductorQuartzHistory = new ConductorQuartzHistory();
        
        if(null!=conductorQuartzMapping)
        {
        	conductorQuartzHistory.setConductorQuartzMapping(conductorQuartzMapping);
        	conductorQuartzHistory.setQuartzExecutionStatus(false);
        	
        	conductorQuartzHistory = this.conductorQuartzHistoryRepository.saveAndFlush(conductorQuartzHistory);
        }

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String json = jobDataMap.getString("json");

        this.runLog = "";
        
        String conductorCorrelationId = triggerWorkflow(json);
        
        if(conductorQuartzHistory.getHistoryId() > 0 && null!=conductorCorrelationId)
        {
        	conductorQuartzHistory.setConductorCorrelationId(conductorCorrelationId);
        	conductorQuartzHistory.setQuartzExecutionStatus(true);
        	
        	conductorQuartzHistory = this.conductorQuartzHistoryRepository.saveAndFlush(conductorQuartzHistory);
        }
        else if(conductorQuartzHistory.getHistoryId() > 0)
        {
        	conductorQuartzHistory.setQuartzExecutionStatus(false);
        	conductorQuartzHistory.setQuartzExecutionLog(this.runLog.substring(0, 500));
        	
        	conductorQuartzHistory = this.conductorQuartzHistoryRepository.saveAndFlush(conductorQuartzHistory);
        }
    }
    
    private String triggerWorkflow(String requestJson)
	{
		
		  logger.info(">> triggerWorkflow()\n"+requestJson);
		  
		  try {
			  
			  HttpHeaders headers = new HttpHeaders(); headers.setContentType(
			  MediaType.APPLICATION_JSON );
			  
			  HttpEntity request= new HttpEntity( requestJson, headers );
			  
			  String triggerWorkflowResponse = restTemplate.postForObject(this.CONDUCTOR_SERVER_API_ENDPOINT+"workflow", request, String.class );
			  
			  logger.info("## triggerWorkflow\n"+triggerWorkflowResponse);
			  
			  return triggerWorkflowResponse;
			  
		  } catch(HttpClientErrorException e) {
			  
			  if(e.getStatusCode().compareTo(HttpStatus.CONFLICT) == 0) 
				  logger.warn("-- triggerWorkflow\n"+e.getMessage()); 
			  else 
				  logger.error("-- triggerWorkflow\n"+e.getMessage());
			  this.runLog = e.getMessage();
		  }
		  
		  logger.info("<< triggerWorkflow()");
		  
		  return null;
		 }
}