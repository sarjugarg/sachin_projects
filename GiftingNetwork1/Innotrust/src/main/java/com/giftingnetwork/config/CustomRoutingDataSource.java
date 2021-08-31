package com.giftingnetwork.config;

 
import javax.servlet.http.HttpServletRequest;

import com.giftingnetwork.controller.ScopesController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomRoutingDataSource extends AbstractRoutingDataSource {

    
    @Autowired
    ScopesController scopesController;
    Logger logger = LoggerFactory.getLogger(CustomRoutingDataSource.class);
  
    @Override
    protected Object determineCurrentLookupKey() {
        String serverName = "localDb";
         try {
            serverName = scopesController.getDBName();
        }  finally {
             return serverName;
        }
    }

   
}