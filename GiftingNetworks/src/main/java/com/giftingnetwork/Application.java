package com.giftingnetwork;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
 

@SpringBootApplication
@EnableConfigurationProperties(DatabaseConfigurations.class) 


public class Application { 

     public static   String DefaultPageSize  ;

    public static   String DefaultMinPageSize  ;

    public static   String DefaultMaxPageSize  ;

    public static String LoginSessionTimeOut ;

    public static String MaxPageSize = "100";

    public static String  LoginRefreshTokenTimeOut = "100";
     
    
	@Autowired
    DatabaseConfigurations databaseConfigurations;
 
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    public DataSource dataSource() {
        CustomRoutingDataSource dataSource = new CustomRoutingDataSource();
        dataSource.setTargetDataSources(databaseConfigurations.createTargetDataSources());
        return dataSource;
    }
}
