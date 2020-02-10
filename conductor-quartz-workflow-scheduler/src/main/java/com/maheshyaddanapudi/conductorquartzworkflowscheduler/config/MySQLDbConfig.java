package com.maheshyaddanapudi.conductorquartzworkflowscheduler.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
	    value="db", 
	    havingValue = "mysql", 
	    matchIfMissing = false)
public class MySQLDbConfig {
	
	@Bean("datasource")
    public DataSource dataSource(
                          @Value("${spring.datasource.username:root}") String datasourceUsername,
                          @Value("${spring.datasource.password:}") String datasourcePassword,
                          @Value("${spring.datasource.url:jdbc:mariadb://localhost:3306/test}") String datasourceUrl,
                          @Value("${spring.datasource.driver-class-name:org.mariadb.jdbc.Driver}") String datasourceDriver){
        

        return DataSourceBuilder
                .create()
                .username(datasourceUsername)
                .password(datasourcePassword)
                .url(datasourceUrl)
                .driverClassName(datasourceDriver)
                .build();
    }

}
