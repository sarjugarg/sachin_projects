package com.giftingnetwork.api;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class ScopesController {

    String DBName;

    String refCode;

    String contentType;

    String acceptType;

    public String getAcceptType() {
        return acceptType;
    }

    public void setAcceptType(String acceptType) {
        this.acceptType = acceptType;
    }

    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

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

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

}
