package com.ceir.SLAModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import com.ceir.SLAModule.service.SLAService;

import org.apache.log4j.Logger;


@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages="com.ceir.SLAModule")
public class App 
{
	private final static Logger log =Logger.getLogger(App.class);
	public static void main(String[] args) {
	    	ConfigurableApplicationContext	ctx =SpringApplication.run(App.class, args);
	    	SLAService sLAService=ctx.getBean(SLAService.class);
			new Thread(sLAService).start();	 
			}
	    }


