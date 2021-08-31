package com.giftingnetwork.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class ScopesController {

    String DBName;
    
    public ScopesController(String dBName) {
        this.DBName = dBName;
    }

    public String getDBName() {
        return DBName;
    }

    public void setDBName(String dBName) {
        this.DBName = dBName;
    }

    public ScopesController() {
    }
 
}
