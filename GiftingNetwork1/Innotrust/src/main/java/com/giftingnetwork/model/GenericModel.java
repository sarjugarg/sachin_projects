package com.giftingnetwork.model;

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
    String type;
    String status;
    String username;
    String tokenId;
    String authorization;
    String clientId;
    String errorTitle;
    String errorDetail;
    String acceptType;
    String password;
    

    public String apiName;
    public  String apiType;
    public String errorCode;
    public String query;
    public String queryString;
    public String paginators;
    public String sorting;
    public String filters;
     public String  recommId;
 



     

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getPaginators() {
        return paginators;
    }

    public void setPaginators(String paginators) {
        this.paginators = paginators;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getRecommId() {
        return recommId;
    }

    public void setRecommId(String recommId) {
        this.recommId = recommId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAcceptType() {
        return acceptType;
    }

    public void setAcceptType(String acceptType) {
        this.acceptType = acceptType;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    @Override
    public String toString() {
        return "GenericModel [acceptType=" + acceptType + ", apiName=" + apiName + ", apiType=" + apiType
                + ", authorization=" + authorization + ", clientId=" + clientId + ", code=" + code + ", content_tsype="
                + content_tsype + ", errorCode=" + errorCode + ", errorDetail=" + errorDetail + ", errorTitle="
                + errorTitle + ", filters=" + filters + ", httpStatus=" + httpStatus + ", message=" + message
                + ", paginators=" + paginators + ", password=" + password + ", query=" + query + ", queryString="
                + queryString + ", recommId=" + recommId + ", ref_code=" + ref_code + ", result=" + result
                + ", sorting=" + sorting + ", status=" + status + ", tokenId=" + tokenId + ", type=" + type
                + ", username=" + username + "]";
    }

   
    


    

}
