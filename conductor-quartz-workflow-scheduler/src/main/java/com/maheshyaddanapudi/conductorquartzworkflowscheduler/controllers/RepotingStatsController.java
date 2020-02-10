package com.maheshyaddanapudi.conductorquartzworkflowscheduler.controllers;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;

import java.util.List;

import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.ConductorQuartzMapping;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.entities.SchedulerPortfolioStats;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.repository.ConductorQuartzHistoryRepository;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.db.repository.ConductorQuartzMappingRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Quartz Scheduler Reports API")
@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class RepotingStatsController {

	private Logger logger = LoggerFactory.getLogger(RepotingStatsController.class.getSimpleName());
	
	@Autowired
	ConductorQuartzMappingRepository conductorQuartzMappingRepository;
	
	@Autowired
	ConductorQuartzHistoryRepository conductorQuartzHistoryRepository;
	
	@Autowired
	private Gson gson;
	
	@GetMapping("allWorkflowExecutionSchedules")
	@ApiOperation(value = "Gets all Conductor Workflow Schedules")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request."), @ApiResponse(code = SC_NOT_FOUND, message = "Worfklows not found") })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ConductorQuartzMapping>> getAllWorkflowExecutionSchedules()
    {
    	logger.info(">> getAllWorkflowExecutionSchedules()");
    	
    	try {
    		
    		List<ConductorQuartzMapping> response = this.conductorQuartzMappingRepository.findAll();
    		    		
    		if(null!=response && !response.isEmpty())
    		{
    			logger.info("<< getAllWorkflowExecutionSchedules()\n Response: "+gson.toJson(response));
                
                return new ResponseEntity<>(response, HttpStatus.OK);
    		}
    		else
    		{
    			logger.info("<< getAllWorkflowExecutionSchedules()\n Response: null");
                
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    		}
    	}
    	catch(Exception e)
    	{	
    		logger.error("-- getAllWorkflowExecutionSchedules()\n Error: "+e.getMessage());
    		
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }
	
	@GetMapping("schedulerPortfolioStats")
	@ApiOperation(value = "Provides Summary of the total run statistics")
    @ApiResponses(value = { @ApiResponse(code = SC_OK, message = "ok"), @ApiResponse(code = SC_BAD_REQUEST, message = "Invalid Request."), @ApiResponse(code = SC_NOT_FOUND, message = "Worfklows not found") })
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SchedulerPortfolioStats> getSchedulerPortfolioStats()
    {
    	logger.info(">> getSchedulerPortfolioStats()");
    	
    	try {
    		
    		SchedulerPortfolioStats response = this.conductorQuartzHistoryRepository.getSchedulerPortfolioStats();
    		    		
    		if(null!=response)
    		{
    			logger.info("<< getSchedulerPortfolioStats()\n Response: "+gson.toJson(response));
                
                return new ResponseEntity<>(response, HttpStatus.OK);
    		}
    		else
    		{
    			logger.info("<< getSchedulerPortfolioStats()\n Response: null");
                
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    		}
    	}
    	catch(Exception e)
    	{	
    		logger.error("-- getSchedulerPortfolioStats()\n Error: "+e.getMessage());
    		
    		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    	}
    }

}
