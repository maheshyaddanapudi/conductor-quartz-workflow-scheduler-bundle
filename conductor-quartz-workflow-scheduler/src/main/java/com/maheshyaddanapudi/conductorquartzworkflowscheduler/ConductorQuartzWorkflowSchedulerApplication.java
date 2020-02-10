package com.maheshyaddanapudi.conductorquartzworkflowscheduler;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

@SpringBootApplication
@EnableBatchProcessing
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class ConductorQuartzWorkflowSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConductorQuartzWorkflowSchedulerApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}
	
	@Bean
	public Gson gson()
	{
		return new Gson();
	}

}
