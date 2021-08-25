package com.ceir.GreyListProcess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import com.ceir.GreyListProcess.service.GreyListService;
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.ceir.GreyListProcess")
public class App 
{
    public static void main( String[] args )
    {
    	ConfigurableApplicationContext	ctx =SpringApplication.run(App.class, args);
		GreyListService greyListService=ctx.getBean(GreyListService.class);
		new Thread(greyListService).start();
    }
}
