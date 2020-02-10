package com.maheshyaddanapudi.conductorquartzworkflowscheduler.controllers;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.config.QuartzConfig;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.constants.ConductorQuartzWorkflowSchedulerConstants;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.ConductorQuartzHistory;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.ConductorQuartzMapping;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.repository.ConductorQuartzHistoryRepository;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.repository.ConductorQuartzMappingRepository;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.dtos.requests.ScheduleConductorWorkflowExecutionRequest;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.dtos.responses.SchedulerResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Conductor Scheduler API")
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ScheduleConductorWorkflowExecutionController {
	
	private Logger logger = LoggerFactory.getLogger(ScheduleConductorWorkflowExecutionController.class.getSimpleName());

	@Autowired
    private Scheduler scheduler;
	
	@Autowired
	ConductorQuartzMappingRepository conductorQuartzMappingRepository;
	
	@Autowired
	ConductorQuartzHistoryRepository conductorQuartzHistoryRepository;
	
	@Autowired
	private Gson gson;
    
	@ApiOperation(value = "Schedules a Conductor Workflow with a Predefined Payload")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request.") })
    @PostMapping("scheduleWorkflowExecution")
	@ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SchedulerResponse> scheduleWorkflowExecution(@Valid @RequestBody ScheduleConductorWorkflowExecutionRequest request) {
    
		logger.info(">> scheduleWorkflowExecution()\n Request: "+gson.toJson(request));
		
    	try {
    		String uniqueIdentifier = UUID.randomUUID().toString();
    		
    		DateFormat dafeFormat = new SimpleDateFormat(ConductorQuartzWorkflowSchedulerConstants.DATE_FORMAT);
    		
    		ConductorQuartzMapping conductorQuartzMapping = new ConductorQuartzMapping();
    		
    		conductorQuartzMapping.setQuartzSchedulerId(uniqueIdentifier);
    		conductorQuartzMapping.setScheduleCronExpression(request.getCronExpression());
    		conductorQuartzMapping.setScheduleCurrentStatus("DRAFT");
    		conductorQuartzMapping.setScheduleDescription(request.getScheduleDesc());
    		conductorQuartzMapping.setScheduleName(request.getScheduleName());
    		conductorQuartzMapping.setScheduleStartTimestamp(dafeFormat.parse(request.getStartDate()));
    		conductorQuartzMapping.setScheduleStopTimestamp(dafeFormat.parse(request.getEndDate()));
    		conductorQuartzMapping.setScheduleWorkflowPayload(request.getJson().getBytes());
    		conductorQuartzMapping.setWorkflowName(request.getWorkflowName());
    		conductorQuartzMapping.setWorkflowVersion(request.getWorkflowVersion());
    		
    		conductorQuartzMapping = this.conductorQuartzMappingRepository.saveAndFlush(conductorQuartzMapping);
    		
    		JobDetail jobDetail = QuartzConfig.buildJobDetail(uniqueIdentifier, request);
            Trigger trigger = QuartzConfig.buildJobTrigger(request.getStartDate(), request.getEndDate(), jobDetail, request.getCronExpression());
            scheduler.scheduleJob(jobDetail, trigger);
            
            if(scheduler.isShutdown()) {
            	scheduler.start();
            	logger.info("Quartz Scheduler was in Shutdown status. Started Quartz Scheduler to fulfill the new schedule.");
            }
            
            conductorQuartzMapping.setScheduleCurrentStatus("SCHEDULED");
            conductorQuartzMapping = this.conductorQuartzMappingRepository.saveAndFlush(conductorQuartzMapping);
            
            SchedulerResponse response = new SchedulerResponse(uniqueIdentifier, ConductorQuartzWorkflowSchedulerConstants.SUCCESS);
            
            logger.info("<< scheduleWorkflowExecution()\n Response: "+gson.toJson(response));
            
            return new ResponseEntity<>(response, HttpStatus.OK);
    	}
    	catch(Exception e) {
    		
    		logger.error("-- scheduleWorkflowExecution()\n Error: "+e.getMessage());
    		
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
	
	@ApiOperation(value = "Pauses a Conductor Workflow Schedule")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request."), @ApiResponse(code = SC_NOT_FOUND, message = "Worfklow not found") })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("pauseWorkflowExecution")
    public ResponseEntity<SchedulerResponse> pauseWorkflowExecution(@RequestParam String scheduleId) {
		
		logger.info(">> pauseWorkflowExecution()\n Request: scheduleId="+scheduleId);
		
    	try {
    		
    		ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(scheduleId);
    		
    		if(null!=conductorQuartzMapping && conductorQuartzMapping.getMappingId() > 0)
    		{	
    			scheduler.pauseJob(new JobKey(scheduleId, ConductorQuartzWorkflowSchedulerConstants.Conductor));

                SchedulerResponse response = new SchedulerResponse(scheduleId, ConductorQuartzWorkflowSchedulerConstants.SUCCESS);
                
    			conductorQuartzMapping.setScheduleCurrentStatus("PAUSED");
    			conductorQuartzMapping = this.conductorQuartzMappingRepository.saveAndFlush(conductorQuartzMapping);
                
                logger.info("<< pauseWorkflowExecution()\n Response: "+gson.toJson(response));
                
                return new ResponseEntity<>(response, HttpStatus.OK);
    		}
    		else
    		{
    			logger.info("<< pauseWorkflowExecution()\n Response: null");
                
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    		}
    		
    	}
    	catch(Exception e)
    	{
    		
    		logger.error("-- scheduleWorkflowExecution()\n Error: "+e.getMessage());
    		
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
	@GetMapping("resumeWorkflowExecution")
	@ApiOperation(value = "Resumes a Conductor Workflow Schedule")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request."), @ApiResponse(code = SC_NOT_FOUND, message = "Worfklow not found") })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SchedulerResponse> resumeWorkflowExecution(@RequestParam String scheduleId)
    {
		logger.info(">> resumeWorkflowExecution()\n Request: scheduleId="+scheduleId);
		
    	try {
    		
    		ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(scheduleId);
    		
    		if(null!=conductorQuartzMapping && conductorQuartzMapping.getMappingId() > 0)
    		{
    			scheduler.resumeJob(new JobKey(scheduleId, ConductorQuartzWorkflowSchedulerConstants.Conductor));
        		
        		SchedulerResponse response = new SchedulerResponse(scheduleId, ConductorQuartzWorkflowSchedulerConstants.SUCCESS);
        		
        		conductorQuartzMapping.setScheduleCurrentStatus("SCHEDULED");
    			conductorQuartzMapping = this.conductorQuartzMappingRepository.saveAndFlush(conductorQuartzMapping);
                
                logger.info("<< resumeWorkflowExecution()\n Response: "+gson.toJson(response));
                
                return new ResponseEntity<>(response, HttpStatus.OK);
    		}
    		else
    		{
    			logger.info("<< resumeWorkflowExecution()\n Response: null");
                
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    		}
    		
    	}
    	catch(Exception e)
    	{
    		
    		logger.error("-- resumeWorkflowExecution()\n Error: "+e.getMessage());
    		
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @DeleteMapping("workflowExecutionSchedule")
	@ApiOperation(value = "Deletes a Conductor Workflow Schedule")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request."), @ApiResponse(code = SC_NOT_FOUND, message = "Worfklow not found") })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SchedulerResponse> deleteWorkflowExecution(@RequestParam String scheduleId)
    {
    	logger.info(">> deleteWorkflowExecution()\n Request: scheduleId="+scheduleId);
    	
    	try {
    		
    		ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(scheduleId);
    		
    		if(null!=conductorQuartzMapping && conductorQuartzMapping.getMappingId() > 0)
    		{
    			scheduler.deleteJob(new JobKey(scheduleId, ConductorQuartzWorkflowSchedulerConstants.Conductor));
        		
        		SchedulerResponse response = new SchedulerResponse(scheduleId, ConductorQuartzWorkflowSchedulerConstants.SUCCESS);
        		
        		this.conductorQuartzMappingRepository.delete(conductorQuartzMapping);
                
                logger.info("<< deleteWorkflowExecution()\n Response: "+gson.toJson(response));
                
                return new ResponseEntity<>(response, HttpStatus.OK);
    		}
    		else
    		{

    			logger.info("<< deleteWorkflowExecution()\n Response: null");
                
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    		}
    		
    	}
    	catch(Exception e)
    	{	
    		logger.error("-- deleteWorkflowExecution()\n Error: "+e.getMessage());
    		
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @GetMapping("workflowExecutionSchedule")
	@ApiOperation(value = "Gets a Conductor Workflow Schedule Detail")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request."), @ApiResponse(code = SC_NOT_FOUND, message = "Worfklow not found") })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ConductorQuartzMapping> getWorkflowExecutionSchedule(@RequestParam String scheduleId)
    {
    	logger.info(">> getWorkflowExecutionSchedule()\n Request: scheduleId="+scheduleId);
    	
    	try {
    		
    		ConductorQuartzMapping response = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(scheduleId);
    		    		
    		if(null!=response)
    		{
    			logger.info("<< getWorkflowExecutionSchedule()\n Response: "+gson.toJson(response));
                
                return new ResponseEntity<>(response, HttpStatus.OK);
    		}
    		else
    		{
    			logger.info("<< getWorkflowExecutionSchedule()\n Response: null");
                
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    		}
    	}
    	catch(Exception e)
    	{	
    		logger.error("-- getWorkflowExecutionSchedule()\n Error: "+e.getMessage());
    		
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
    @GetMapping("workflowScheduleExecutionHistory")
	@ApiOperation(value = "Gets a Conductor Workflow Scheduled Execution History")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request."), @ApiResponse(code = SC_NOT_FOUND, message = "Worfklow not found") })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ConductorQuartzHistory>> getWorkflowScheduleExecutionHistory(@RequestParam String scheduleId)
    {
    	logger.info(">> getWorkflowScheduleExecutionHistory()\n Request: scheduleId="+scheduleId);
    	
    	try {
    		
    		ConductorQuartzMapping conductorQuartzMapping = this.conductorQuartzMappingRepository.findByQuartzSchedulerId(scheduleId);
    		    		
    		if(null!=conductorQuartzMapping)
    		{
    			List<ConductorQuartzHistory> response = new ArrayList<ConductorQuartzHistory>();
    			
    			response = this.conductorQuartzHistoryRepository.findByConductorQuartzMapping(conductorQuartzMapping);
    			
    			logger.info("<< getWorkflowScheduleExecutionHistory()\n Response: "+gson.toJson(response));
                
                return new ResponseEntity<>(response, HttpStatus.OK);
    		}
    		else
    		{
    			logger.info("<< getWorkflowScheduleExecutionHistory()\n Response: null");
                
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    		}
    	}
    	catch(Exception e)
    	{	
    		logger.error("-- getWorkflowScheduleExecutionHistory()\n Error: "+e.getMessage());
    		
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
    
}
