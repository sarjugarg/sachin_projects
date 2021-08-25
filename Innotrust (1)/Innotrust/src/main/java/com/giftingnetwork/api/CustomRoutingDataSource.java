package com.giftingnetwork.api;

 
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class CustomRoutingDataSource extends AbstractRoutingDataSource {

    @Autowired
    HttpServletRequest request;

    @Autowired
    ScopesController scopesController;

    Logger logger = LoggerFactory.getLogger(CustomRoutingDataSource.class);

    @Override
    protected Object determineCurrentLookupKey() {
        String serverName = "localDb";
         try {
            serverName = scopesController.getDBName();
        }   finally {
            logger.info( " return server Name " +serverName );
            return serverName;
        }

      
    }

    // public String doGet() {
    // String serverName = scopesController.getDBName();
    // String returnValue = "";
    // try {
    // HttpSession session = request.getSession();

    // logger.info("Db Name ::::: " + session.getAttribute("DBName").toString());
    // returnValue = session.getAttribute("DBName").toString();
    // return returnValue;
    // } catch (Exception e) {
    // logger.error(e.getMessage());
    // return returnValue;
    // }
    // }
}