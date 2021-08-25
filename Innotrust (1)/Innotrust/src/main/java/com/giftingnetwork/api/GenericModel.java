package com.giftingnetwork.api;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class GenericModel {

    String code;
    String message;
    String ref_code;
    String content_tsype;
    HttpStatus httpStatus;
    String result;
    String type ; 
    String status;
    
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
    public String getRef_code() {
        return ref_code;
    }
    public void setRef_code(String ref_code) {
        this.ref_code = ref_code;
    }
    public String getContent_tsype() {
        return content_tsype;
    }
    public void setContent_tsype(String content_tsype) {
        this.content_tsype = content_tsype;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
  


    

}
