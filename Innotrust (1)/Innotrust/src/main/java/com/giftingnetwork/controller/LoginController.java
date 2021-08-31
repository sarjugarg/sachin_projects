package com.giftingnetwork.controller;

import java.io.StringReader;
import java.util.Enumeration;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.giftingnetwork.formatter.XmlDomHandler;
import com.giftingnetwork.model.GenericModel;
 import com.giftingnetwork.repo.RecommendationRepository;
import com.giftingnetwork.service.LoginService;
 import com.giftingnetwork.util.GenericFunctions;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.json.JSONObject;
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
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    RecommendationRepository recommendationRepository;
    
    @Autowired
    GenericFunctions genericFunctions;

    @Autowired
    LoginService loginService;

    @Autowired
    XmlDomHandler xmlDomHandler;
    
    @Autowired
    private HttpServletRequest request;

    @Autowired
    ScopesController scopesController;

    @Autowired
    GenericModel genericModel;

    @PostMapping(value = "/v1/auth/login")
    @ResponseBody
    public ResponseEntity<String> authenticationLogin(@RequestBody String requestString) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String result = null;
        String contentType = request.getHeader("Content-Type").contains("xml") ? "xml" : "json";
        String acceptValue = request.getHeader("Accept").contains("xml") ? "xml" : "json";
         GenericModel genericModel = new GenericModel();
        genericModel.setAcceptType(acceptValue);
        String str = request.getServletPath();
         String apiName = str.substring(str.indexOf("/", str.indexOf("/") + 1) + 1,
                str.indexOf("/", str.indexOf("/", str.indexOf("/") + 1) + 1));
                apiName = "login";
                genericModel.setErrorDetail(apiName);
        genericModel.setApiName(apiName);
        genericModel.setRef_code(genericFunctions.ReferenceCodeGenerator(apiName, String.valueOf(Math.random())));
        String username = null;
        String password = null;
        String client_id = null;
        try {
            if (contentType.contains("json")) {
                JSONObject json = new JSONObject(requestString);
                username = json.optString("username");
                password = json.optString("password");
                client_id = json.optString("client_id");
                logger.info(" Json Values :" + json.optString("username") + " " + password + " "
                        + client_id);
            } else {
                Document doc = null;
                doc = loadXMLFromString(requestString);
                doc.getDocumentElement().normalize();
                NodeList rootList = doc.getElementsByTagName("data");
                if (rootList.getLength() == 0) {
                    logger.info("Select rootNode is Missing");
                }
                for (int i = 0; i < rootList.getLength(); i++) {
                    Node node = rootList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        username = element.getElementsByTagName("username").item(0).getTextContent();
                        password = element.getElementsByTagName("password").item(0).getTextContent();
                        client_id = element.getElementsByTagName("client_id").item(0).getTextContent();
                        logger.info(" xml Values :" + element.getElementsByTagName("username").item(0).getTextContent()
                                + " :: " + password + "  :: "+ element.getElementsByTagName("client_id").item(0).getTextContent());
                    }
                }
            }
        } catch (Exception e) {
            logger.info("Rejected Due to format is not match  ");
            genericModel.setHttpStatus(HttpStatus.BAD_REQUEST);
            genericModel.setErrorTitle("Client Error");
            genericModel = xmlDomHandler.generateErrorXml1(genericModel);
            recommendationRepository.insertintoAuditLogging(genericModel);
            return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());
        }
        genericModel.setUsername(username);
        genericModel.setClientId(client_id);
        genericModel.setPassword(password);
        if (username == null || username.trim().length() < 1) {
            logger.info("Rejected Due to  UserName Value Not Correct  ");
            genericModel.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            genericModel.setErrorTitle("username not found");
            genericModel = xmlDomHandler.generateErrorXml1(genericModel);
            recommendationRepository.insertintoAuditLogging( genericModel);
            return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());

        } else if (password == null || password.trim().length() < 1) {
            logger.info("Rejected Due to password Value Not Correct  ");
            genericModel.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            genericModel.setErrorTitle("password not found");
            genericModel = xmlDomHandler.generateErrorXml1(genericModel);
            recommendationRepository.insertintoAuditLogging( genericModel);
            return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());

        } else if (!Optional.ofNullable(client_id).isPresent()) {
            logger.info("Rejected Due to No Client   Not Correct  ");
            genericModel.setHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY);
            genericModel.setErrorTitle("client not found");
            genericModel = xmlDomHandler.generateErrorXml1(genericModel);
            recommendationRepository.insertintoAuditLogging( genericModel);
            return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());

        }   else {
            String userIp = request.getHeader("HTTP_CLIENT_IP") == null
                    ? (request.getHeader("X-FORWARDED-FOR") == null ? request.getRemoteAddr()
                            : request.getHeader("X-FORWARDED-FOR"))
                    : request.getHeader("HTTP_CLIENT_IP");
            genericModel.setCode(userIp);
            logger.info("  Client ID for Login Request " + client_id + " For UserName "
                   );
             loginService.authenticationService(genericModel);
            genericModel.setUsername(username);
            genericModel.setClientId(client_id); 
             if (genericModel.getHttpStatus() == HttpStatus.UNAUTHORIZED) {
                responseHeaders.add("WWW-Authenticate", "Bearer realm=\"Gifting Network\" ");
            }
            recommendationRepository.insertintoAuditLogging( genericModel);
            return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());
        }
    }

    @PostMapping(value = "/v1/auth/logout")
    @ResponseBody
    public ResponseEntity<String> authenticationLogout() {
         GenericModel genericModel = new GenericModel();
        HttpHeaders responseHeaders = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            logger.info(key + " : " + value);
        }
      String  apiName = "logout";
        genericModel.setErrorDetail(apiName);
        genericModel.setApiName(apiName);

        String acceptValue = request.getHeader("Accept").contains("xml") ? "xml" : "json";
        genericModel.setAcceptType(acceptValue);
         if (!Optional.ofNullable(request.getHeader("Authorization")).isPresent()) {
            logger.info("Rejected Due to  token id  Not Present");
            String message = "Authorization does not exist";
            genericModel.setHttpStatus(HttpStatus.PRECONDITION_REQUIRED);
            genericModel.setErrorTitle(message);
            genericModel = xmlDomHandler.generateErrorXml1(genericModel);
            String result = genericModel.getResult();
            recommendationRepository.insertintoAuditLogging(genericModel);
            return new ResponseEntity<String>(result, responseHeaders, HttpStatus.PRECONDITION_REQUIRED);
        }
        String accessToken = request.getHeader("Authorization");
            genericModel.setAuthorization(accessToken);
        genericModel.setAuthorization(accessToken);
        genericFunctions.ReferenceCodeGenerator(apiName, accessToken);
        if (!(acceptValue.contains("json") || acceptValue.contains("xml"))) {
            logger.info(" Accept format is Incorrect :" + acceptValue);
            String message = "header not found";
             genericModel.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
            genericModel.setErrorTitle(message);
            genericModel = xmlDomHandler.generateErrorXml1(genericModel);
            String result = genericModel.getResult();
            recommendationRepository.insertintoAuditLogging(genericModel);
            return new ResponseEntity<String>(result, responseHeaders, HttpStatus.NOT_ACCEPTABLE);
        }
         loginService.authenticationsessionCheckerService(genericModel);
       //  logger.info(":" + genericModel);
        if ( genericModel.getStatus() != null &&     genericModel.getStatus().equals("access")) {
            logger.info(" Delete session from DbTable " + accessToken);
            Boolean value = loginService.deleteBysessionId(accessToken);
            if (value == false) {
                logger.info("Rejected Due to session Not Deleted in DB ");
                String message = "internal error";
                genericModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                genericModel.setErrorTitle(message);
                genericModel = xmlDomHandler.generateErrorXml1(genericModel);
                 logger.info("Logout Response : " + genericModel.getResult());
                recommendationRepository.insertintoAuditLogging( genericModel);
                return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
             String  result = loginService.generateLogoffXml( genericModel);
                logger.info("Logout Result : " + result);
                recommendationRepository.insertintoAuditLogging(genericModel);
                return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
            }
        } else {
          responseHeaders.add("WWW-Authenticate", "Bearer realm=\"Gifting Network\" ");
           xmlDomHandler.generateErrorXml1(genericModel);
            recommendationRepository.insertintoAuditLogging( genericModel);
            return new ResponseEntity<String>(genericModel.getResult(), responseHeaders, genericModel.getHttpStatus());
        }
    }
    public Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }
}
