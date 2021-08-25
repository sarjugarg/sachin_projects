package com.gl.imei.freq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootConfiguration
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages= {"com.gl.imei.freq"})
public class ImeiFreqReportApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ImeiFreqReportApplication.class, args);
		MainProcess mainProcess = context.getBean(MainProcess.class);
		mainProcess.start();
	}

}
