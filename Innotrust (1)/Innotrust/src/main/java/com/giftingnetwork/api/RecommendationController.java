package com.giftingnetwork.api;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  TokenService tokenService;

  @Autowired
  GenericFunctions genericFunctions;

  // @Value("${spring.HeaderType:}")
  // private String headerType;

  @Autowired
  private HttpServletRequest request;

  @Autowired
  RecommendationRepository recommendationRepository;
  @Autowired
  GenericModel genericModel;

  @PostMapping(value = { "/recommendation/find-recommendation-info", "/contacts/get-contact-info",
      "/organization/get-organization-info", "/contribution/get-contribution-list-info",
      "/newAccountSetup/get-new-approved-account-setup-info" })
  @Consumes({ "application/xml", "application/json" })
  @Produces({ "application/xml", "application/json" })
  @ResponseBody
  public ResponseEntity<String> FindRecommendationCont(@RequestBody String xmlString) {
    String result = null;
    HttpHeaders responseHeaders = new HttpHeaders();
    try {
      String path = request.getServletPath();
      String acceptValue = request.getHeader("Accept");
      String contentType = request.getHeader("Content-Type");
      scopesController.setAcceptType(acceptValue);
      String apiName = path.substring(path.lastIndexOf("/") + 1);
      String authorization = request.getHeader("Authorization");
      logger.info(" Request Path : " + path + " With ApiName  " + apiName + " , Accept Type : " + acceptValue
          + "; Authorization " + authorization);
      tokenService.ReferenceCodeGenerator(apiName, authorization);

      if ((!Optional.ofNullable(contentType).isPresent()) || (!Optional.ofNullable(acceptValue).isPresent())) {
        logger.info(" Header is missing");
        scopesController.setAcceptType("xml");
        String message = "header not found";
        String refCode = "null";
        genericModel = xmlDomHandler.generateErrorXml(apiName, message, refCode);
        result = genericModel.getResult();
        return new ResponseEntity<String>(result, responseHeaders, HttpStatus.PRECONDITION_REQUIRED);
      } else {
        scopesController.setContentType(contentType);
      }
      if (!(acceptValue.contains("json") || acceptValue.contains("xml"))) {
        logger.info(" Accept format is Incorrect");
        scopesController.setAcceptType("xml");
        String message = "header not found";
        String refCode = "null";
        genericModel = xmlDomHandler.generateErrorXml(apiName, message, refCode);
        result = genericModel.getResult();
        return new ResponseEntity<String>(result, responseHeaders, HttpStatus.PRECONDITION_REQUIRED);
      }
      if (!Optional.ofNullable(authorization).isPresent()) {
        logger.info("Rejected Due to  Authorization  Not Present");
        String code = apiName;
        String message = "Authorization does not exist";
        String refCode = "null";
        genericModel = xmlDomHandler.generateErrorXml(code, message, refCode);
        result = genericModel.getResult();
        return new ResponseEntity<String>(result, responseHeaders, HttpStatus.PRECONDITION_REQUIRED);
      }

      String  authResult = tokenService.authenticatesessionId(authorization);

      if (! authResult.equals("true") ) {
        String message = authResult.equals("false") ? "unauthorized access" : authResult;
        String refCode = "null";
        genericModel = xmlDomHandler.generateErrorXml(apiName, message, refCode);
        result = genericModel.getResult();
        responseHeaders.add("WWW-Authenticate", "Bearer realm=\"Gifting Network\" ");
        return new ResponseEntity<String>(result, responseHeaders, HttpStatus.UNAUTHORIZED);
      }
      result = recommendationService.FindRecommendationService(xmlString, apiName, authorization);
      logger.info(apiName + " Result : " + result + "   For   " + authorization);
      return new ResponseEntity<String>(result, responseHeaders, genericFunctions.getHttpStatusValues(result));
    } catch (Exception e) {
      logger.error(e.getMessage());
      String message = "internal error";
      String refCode = "null";
      String code = "server error";
      genericModel = xmlDomHandler.generateErrorXml(code, message, refCode);
      result = genericModel.getResult();
      return new ResponseEntity<String>(result, responseHeaders, HttpStatus.BAD_REQUEST);
    }
  }

}