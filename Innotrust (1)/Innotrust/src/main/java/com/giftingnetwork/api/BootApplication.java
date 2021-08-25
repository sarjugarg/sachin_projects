package com.giftingnetwork.api;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
 
@SpringBootApplication
@EnableConfigurationProperties(DatabaseConfigurations.class)

public class BootApplication { 

     public static String LoginSessionTimeOut ;

    public static String MaxPageSize = "100";
     
	@Autowired
    DatabaseConfigurations databaseConfigurations;
 
	public static void main(String[] args) {
		SpringApplication.run(BootApplication.class, args);
	}

    @Bean
    public DataSource dataSource() {
        CustomRoutingDataSource dataSource = new CustomRoutingDataSource();
        dataSource.setTargetDataSources(databaseConfigurations.createTargetDataSources());
        return dataSource;
    }
}
