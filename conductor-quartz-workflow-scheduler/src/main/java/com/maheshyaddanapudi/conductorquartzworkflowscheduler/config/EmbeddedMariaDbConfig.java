package com.maheshyaddanapudi.conductorquartzworkflowscheduler.config;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;

@Configuration
@ConditionalOnProperty(
	    value="db", 
	    havingValue = "mariadb4j", 
	    matchIfMissing = true)
class EmbeddedMariaDbConfig {

    @Bean("mariaDB4jSpringService")
    public MariaDB4jSpringService mariaDB4jSpringService() {
    	MariaDB4jSpringService mariaDB4jSpringService = new MariaDB4jSpringService();
    	
    	mariaDB4jSpringService.getConfiguration().addArg("--max-connections=10000");
    	
        return mariaDB4jSpringService;
    }

    @Bean("datasource")
    @DependsOn("mariaDB4jSpringService")
    public DataSource dataSource(MariaDB4jSpringService mariaDB4jSpringService,
                          @Value("${app.mariaDB4j.databaseName:quartz}") String databaseName,
                          @Value("${spring.datasource.username:root}") String datasourceUsername,
                          @Value("${spring.datasource.password:}") String datasourcePassword,
                          @Value("${spring.datasource.driver-class-name:org.mariadb.jdbc.Driver}") String datasourceDriver) throws ManagedProcessException {
        //Create our database with default root user and no password
        mariaDB4jSpringService.getDB().createDB(databaseName);

        DBConfigurationBuilder config = mariaDB4jSpringService.getConfiguration();

        return DataSourceBuilder
                .create()
                .username(datasourceUsername)
                .password(datasourcePassword)
                .url(config.getURL(databaseName))
                .driverClassName(datasourceDriver)
                .build();
    }
}