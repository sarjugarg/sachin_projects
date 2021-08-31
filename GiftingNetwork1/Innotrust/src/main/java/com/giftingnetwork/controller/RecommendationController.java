package com.giftingnetwork.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.giftingnetwork.formatter.XmlDomHandler;
import com.giftingnetwork.model.GenericModel;
import com.giftingnetwork.repo.RecommendationRepository;
import com.giftingnetwork.service.LoginService;
import com.giftingnetwork.service.RecommendationService;
import com.giftingnetwork.util.GenericFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; 
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RecommendationController {

  Logger logger = LoggerFactory.getLogger(RecommendationController.class);

  @Autowired
  ScopesController scopesController;
  @Autowired
  LoginService loginService;
  @Autowired
  XmlDomHandler xmlDomHandler;

  @Autowired
  RecommendationService recommendationService;


  @Autowired
  GenericFunctions genericFunctions;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  RecommendationRepository recommendationRepository;
   

  @GetMapping(value = { "/v1/recommendation/{id}", "/v1/contact/{id}", "/v1/organization/{id}", "/v1/gift/{id}",
      "/v1/grant/{id}", "/v1/fund/{id}", "/v1/statement/{id}" })
  @ResponseBody
  public ResponseEntity<String> getDetailsById(@PathVariable("id") String id)   throws IllegalArgumentException   {
   
   
    HttpHeaders responseHeaders = new HttpHeaders();
     GenericModel genericModel = new GenericModel();
    String str = request.getServletPath();
    logger.info("Path Name  " + str + " with Id "  +  id );
    String apiName = str.substring(str.indexOf("/", str.indexOf("/") + 1) + 1,
        str.indexOf("/", str.indexOf("/", str.indexOf("/") + 1) + 1));
        
    genericModel.setApiType("id");

    if (apiName.equals("gift")) {
      apiName = "gift-history";
    }
    if (apiName.equals("grant")) {
      apiName = "grant-history";
    }
    if (apiName.equals("statement")) {
      apiName = "fund-statement";
    }
 
     genericModel.setErrorDetail(apiName);

    String acceptValue = request.getHeader("Accept").contains("xml") ? "xml" : "json";
    genericModel.setAcceptType(acceptValue);

    String authorization = request.getHeader("Authorization");
    genericModel.setRef_code( genericFunctions.ReferenceCodeGenerator(id, authorization)  ) ;
   
    if (!Optional.ofNullable(request.getHeader("Authorization")).isPresent()) {
      logger.info("Rejected Due to  Authorization  Not Present");
       genericModel.setErrorTitle ( "Authorization does not exist");
       genericModel.setHttpStatus (HttpStatus.PRECONDITION_REQUIRED);
      genericModel = xmlDomHandler.generateErrorXml1(genericModel);
      recommendationRepository.insertintoAuditLogging( genericModel);
      return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());
    }
    if (id.contains("^")   ||   id.contains("|") ) {
      logger.info("Rejected Due to  Invalid Chars   ");
       genericModel.setErrorTitle ( "client error");
       genericModel.setHttpStatus (HttpStatus.BAD_REQUEST);
      genericModel = xmlDomHandler.generateErrorXml1(genericModel);
      recommendationRepository.insertintoAuditLogging( genericModel);
      return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());
    }

    genericModel.setApiName(apiName);
    genericModel.setAuthorization(authorization);
  
    genericModel.setRecommId(id);
    genericModel.setQueryString("id:" + id);
  
    recommendationRepository.authenticatesessionId(genericModel);
    logger.info( "[]"  +genericModel.toString() ) ;
    if ( genericModel.getStatus() == null    ||   ! genericModel.getStatus().equals("access") ) {
       genericModel.setHttpStatus(HttpStatus.UNAUTHORIZED);
       logger.info("UNAUTHORIZED");
      genericModel = xmlDomHandler.generateErrorXml1(genericModel);
      responseHeaders.add("WWW-Authenticate", "Bearer realm=\"Gifting Network\" ");
      recommendationRepository.insertintoAuditLogging(genericModel);
      return new ResponseEntity<String>(genericModel.getResult(), responseHeaders,  genericModel.getHttpStatus());
    }

    String result = recommendationService.getDetailsById( genericModel);
    logger.info(apiName + "Result : " + result + "   For   " + authorization);
    recommendationRepository.updateTokenTime(authorization);
    recommendationRepository.insertintoAuditLogging(  genericModel);
    return new ResponseEntity<String>(result, responseHeaders,   genericModel.getHttpStatus()  );
  }

  @GetMapping(value = { "/v1/recommendation/search", "/v1/contact/search", "/v1/organization/search", "/v1/gift/search",
      "/v1/grant/search", "/v1/fund/search", "/v1/statement/search" })

  @ResponseBody
  public ResponseEntity<String> GetValuesBySearchController() throws IllegalArgumentException   {
     String result = null;
    GenericModel genericModel = new GenericModel();
 
    HttpHeaders responseHeaders = new HttpHeaders();
    genericModel.setQueryString(request.getQueryString().trim());
    genericModel.setApiType("search");
 
    try {
      String str = request.getServletPath();
      logger.info("Path Name  " + str);
      String apiName = str.substring(str.indexOf("/", str.indexOf("/") + 1) + 1,
          str.indexOf("/", str.indexOf("/", str.indexOf("/") + 1) + 1));
      if (apiName.equals("gift")) {
        apiName = "gift-history";
      }
      if (apiName.equals("grant")) {
        apiName = "grant-history";
      }
      if (apiName.equals("statement")) {
        apiName = "fund-statement";
      }
  
     
      String acceptValue = request.getHeader("Accept").contains("xml") ? "xml" : "json";
      String authorization = request.getHeader("Authorization");
      genericModel.setAuthorization(authorization);

      genericModel.setApiName(apiName);
      genericModel.setAuthorization(authorization);
      genericModel.setErrorDetail(apiName);
      logger.info(" Request Path : " + str + " With ApiName  " + apiName + " , Accept Type : " + acceptValue
      + "; Authorization " + authorization  + " and QueryString"  + request.getQueryString().trim());

      genericModel.setAcceptType(acceptValue);
      genericModel.setRef_code( genericFunctions.ReferenceCodeGenerator(apiName, authorization)  ) ;
     
      if (!(acceptValue.contains("json") || acceptValue.contains("xml"))) {
        logger.info(" Accept format is Incorrect");
        genericModel.setErrorDetail("header not found");
         genericModel.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
        genericModel = xmlDomHandler.generateErrorXml1(genericModel);
        result = genericModel.getResult();
        recommendationRepository.insertintoAuditLogging( genericModel);
        return new ResponseEntity<String>(result, responseHeaders,  genericModel.getHttpStatus());
      }

      if (request.getQueryString().trim().contains("^")   ||   request.getQueryString().trim().contains("|") ) {
        logger.info("Rejected Due to  Invalid Chars   ");
         genericModel.setErrorTitle ( "client error");
         genericModel.setHttpStatus (HttpStatus.BAD_REQUEST);
        genericModel = xmlDomHandler.generateErrorXml1(genericModel);
        recommendationRepository.insertintoAuditLogging( genericModel);
        return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());
      }
  
      if (!Optional.ofNullable(authorization).isPresent()) {
        logger.info("Rejected Due to  Authorization  Not Present");
         genericModel.setErrorTitle( "Authorization does not exist");
         genericModel.setHttpStatus(HttpStatus.PRECONDITION_REQUIRED);
        genericModel = xmlDomHandler.generateErrorXml1(genericModel);
        recommendationRepository.insertintoAuditLogging( genericModel);
        return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());
      }

      recommendationRepository.authenticatesessionId(genericModel);
      
      if ( genericModel.getStatus() == null    ||     !genericModel.getStatus().equals("access")) {
         genericModel.setHttpStatus(HttpStatus.UNAUTHORIZED);
        genericModel = xmlDomHandler.generateErrorXml1(genericModel) ;
        responseHeaders.add("WWW-Authenticate", "Bearer realm=\"Gifting Network\" ");
        recommendationRepository.insertintoAuditLogging( genericModel);
        return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());
      }
      genericModel.setApiName(apiName);
      genericModel.setAuthorization(authorization);
      result = recommendationService.GetValuesBySearchService(genericModel);
      logger.info(apiName + " Result : " + result + "   For   " + authorization);
      recommendationRepository.insertintoAuditLogging( genericModel);
      return new ResponseEntity<String>(result, responseHeaders,  genericModel.getHttpStatus());

    } catch (Exception e) {
      logger.error(e.getMessage());
      genericModel.setHttpStatus(HttpStatus.BAD_REQUEST);
      genericModel.setErrorTitle("internal error");
      genericModel = xmlDomHandler.generateErrorXml1(genericModel);
      result = genericModel.getResult();
      recommendationRepository.insertintoAuditLogging( genericModel);
      return new ResponseEntity<String>( genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());
    }
  }

}