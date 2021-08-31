package com.giftingnetwork;

import javax.sql.DataSource;
import com.giftingnetwork.config.CustomRoutingDataSource;
import com.giftingnetwork.config.DatabaseConfigurations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
 import org.springframework.boot.autoconfigure.SpringBootApplication;
 import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties(DatabaseConfigurations.class)
public class BootApplication {

    public static String DefaultPageSize;

    public static String DefaultMinPageSize;

    public static String DefaultMaxPageSize;

    public static String LoginSessionTimeOut;

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
