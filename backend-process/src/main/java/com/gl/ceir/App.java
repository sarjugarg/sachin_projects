package com.gl.ceir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.gl.ceir.factory.service.Service;

@EnableJpaAuditing
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
@SpringBootApplication
public class App {
    public static void main( String[] args ){
    	
    	String processName = args[0];
    	
    	ApplicationContext context = SpringApplication.run(App.class, args);
    	Service service = context.getBean(Starter.class).start(processName);
    	service.fetchAndProcess();
    	
    }
}
