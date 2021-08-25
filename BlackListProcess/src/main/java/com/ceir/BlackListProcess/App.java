package com.ceir.BlackListProcess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.ceir.BlackListProcess.service.BlackListService;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.ceir.BlackListProcess")
public class App 
{
    public static void main( String[] args )
    {
    	ConfigurableApplicationContext ctx=SpringApplication.run(App.class, args);
    	BlackListService blackListService=ctx.getBean(BlackListService.class);
		new Thread(blackListService).start();
		
    }
}
