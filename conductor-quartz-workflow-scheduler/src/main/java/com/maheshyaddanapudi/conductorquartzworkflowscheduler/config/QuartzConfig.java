package com.maheshyaddanapudi.conductorquartzworkflowscheduler.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.*;

import com.maheshyaddanapudi.conductorquartzworkflowscheduler.constants.ConductorQuartzWorkflowSchedulerConstants;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.dtos.requests.ScheduleConductorWorkflowExecutionRequest;
import com.maheshyaddanapudi.conductorquartzworkflowscheduler.jobs.ScheduleConductorWorkflowExecutionJob;

import javax.sql.DataSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

@Slf4j
@Configuration
public class QuartzConfig {
    private ApplicationContext applicationContext;
    private DataSource dataSource;

    public QuartzConfig(ApplicationContext applicationContext, DataSource dataSource) {
        this.applicationContext = applicationContext;
        this.dataSource = dataSource;
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler(Trigger... triggers) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", "Conducotr-Quartz-Workflow_Scheduler-1");
        properties.setProperty("org.quartz.scheduler.instanceId", "Conducotr-Quartz-Workflow_Scheduler-Instante-Id-1");

        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setQuartzProperties(properties);
        schedulerFactory.setDataSource(dataSource);
        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);

        if (ArrayUtils.isNotEmpty(triggers)) {
            schedulerFactory.setTriggers(triggers);
        }

        return schedulerFactory;
    }

    static SimpleTriggerFactoryBean createTrigger(JobDetail jobDetail, long pollFrequencyMs, String triggerName) {
        //log.debug("createTrigger(jobDetail={}, pollFrequencyMs={}, triggerName={})", jobDetail.toString(), pollFrequencyMs, triggerName);

        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setStartDelay(0L);
        factoryBean.setRepeatInterval(pollFrequencyMs);
        factoryBean.setName(triggerName);
        factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);

        return factoryBean;
    }

    static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression, String triggerName) {
        //log.debug("createCronTrigger(jobDetail={}, cronExpression={}, triggerName={})", jobDetail.toString(), cronExpression, triggerName);

        // To fix a known issue with time-based cron jobs
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
        factoryBean.setJobDetail(jobDetail);
        factoryBean.setCronExpression(cronExpression);
        factoryBean.setStartTime(calendar.getTime());
        factoryBean.setStartDelay(0L);
        factoryBean.setName(triggerName);
        factoryBean.setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);

        return factoryBean;
    }

    static JobDetailFactoryBean createJobDetail(Class jobClass, String jobName) {
        //log.debug("createJobDetail(jobClass={}, jobName={})", jobClass.getName(), jobName);

        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setName(jobName);
        factoryBean.setJobClass(jobClass);
        factoryBean.setDurability(true);

        return factoryBean;
    }
    
    public static JobDetail buildJobDetail(String uniqueIdentifier, ScheduleConductorWorkflowExecutionRequest scheduleConductorWorkflowExecutionRequest) {
        

        JobDataMap jobDataMap = new JobDataMap();

        jobDataMap.put(ConductorQuartzWorkflowSchedulerConstants.JSON, scheduleConductorWorkflowExecutionRequest.getJson());
        
        return JobBuilder.newJob(ScheduleConductorWorkflowExecutionJob.class)
                .withIdentity(uniqueIdentifier, ConductorQuartzWorkflowSchedulerConstants.Conductor)
                .withDescription(ConductorQuartzWorkflowSchedulerConstants.SCHEDULE_CONDUCTOR_WORKFLOW_EXECUTION_JOB)
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    public static Trigger buildJobTrigger(String startDate, String endDate, JobDetail jobDetail, String cronExpression) {
       try {
	    	   Date endAt = new SimpleDateFormat(ConductorQuartzWorkflowSchedulerConstants.DATE_FORMAT).parse(endDate);
	    	   if(null!=startDate)
	           {
	           	Date startAt = new SimpleDateFormat(ConductorQuartzWorkflowSchedulerConstants.DATE_FORMAT).parse(startDate);
	
	               
	           	return TriggerBuilder.newTrigger()
	                       .forJob(jobDetail)
	                       .withIdentity(jobDetail.getKey().getName(), ConductorQuartzWorkflowSchedulerConstants.WORKFLOW_TRIGGERS)
	                       .startAt(startAt)
	                       .endAt(endAt)
	                       .withDescription(ConductorQuartzWorkflowSchedulerConstants.SEND_WORKFLOW_TRIGGER)
	                       .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
	                       .build();
	           }
	           else
	           {
	           	return TriggerBuilder.newTrigger()
	                       .forJob(jobDetail)
	                       .withIdentity(jobDetail.getKey().getName(), ConductorQuartzWorkflowSchedulerConstants.WORKFLOW_TRIGGERS)
	                       .startNow()
	                       .endAt(endAt)
	                       .withDescription(ConductorQuartzWorkflowSchedulerConstants.SEND_WORKFLOW_TRIGGER)
	                       .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
	                       .build();
	           }
       }
       catch(Exception e)
       {
    	   e.printStackTrace();
    	   return TriggerBuilder.newTrigger()
                   .forJob(jobDetail)
                   .withIdentity(jobDetail.getKey().getName(), ConductorQuartzWorkflowSchedulerConstants.WORKFLOW_TRIGGERS)
                   .withDescription(ConductorQuartzWorkflowSchedulerConstants.SEND_WORKFLOW_TRIGGER)
                   .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                   .build();
       }
    }
}
