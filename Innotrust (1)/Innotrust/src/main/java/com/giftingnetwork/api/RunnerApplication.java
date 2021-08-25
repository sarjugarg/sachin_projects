package com.giftingnetwork.api;

import org.springframework.beans.factory.annotation.Autowired;
  
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Order(value=127000)
public class RunnerApplication implements CommandLineRunner {
  Logger logger = LoggerFactory.getLogger(RunnerApplication.class);
 


  @Autowired
  GenericFunctions genericFunctions;
  

  @Override
  public void run(String...args) throws Exception {

    logger.info(" RunnerApplication  CommandLineRunner ");
    System.out.println(" RunnerApplication  CommandLineRunner ");
    genericFunctions.setConfigurationVariables(); 
    // .populateConfigParamForSessionTimeout() ;
  }
}