package com.giftingnetwork.api;

import java.util.Enumeration;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Controller
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    // @Value("${spring.HeaderType:}")
    // String headerType;
    @Value("${UseNameLength}")
    Integer userNamelength;
    @Value("${PasswordLength}")
    Integer passwordLength;
    @Value("${TOTAL_SERVER}")
    Integer TOTAL_SERVER;

    @Autowired
    GenericFunctions genericFunctions;

    @Autowired
    LoginService loginService;

    @Autowired
    XmlDomHandler xmlDomHandler;

    @Autowired
    TokenService tokenService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    ScopesController scopesController;

    @Autowired
    GenericModel genericModel;

    @Autowired
    JavaXmlDecoder javaXmlDecoder;

    @PostMapping(value = "/authentication/login")
    @Consumes({ "application/xml", "application/json" })
    @Produces({ "application/xml", "application/json" })
    @ResponseBody
    public ResponseEntity<String> authenticationLogin(@RequestBody String requestString) {
        HttpHeaders responseHeaders = new HttpHeaders();
        String result = null;

        LoginModel loginModel = new LoginModel();
        String contentType = request.getHeader("Content-Type");

        logger.info("contentType : " + contentType);

        if (contentType.contains("json")) {
            JSONObject json = new JSONObject(requestString).getJSONObject("data");
            loginModel.username = json.optString("username");
            loginModel.password = json.optString("password");
            loginModel.client_id = json.optString("client_id");
            logger.info(" json Values :" + json.optString("username") + " " + loginModel.password + " "
                    + loginModel.client_id);
        } else if (contentType.contains("xml")) {
            Document doc = null;
            try {
                doc = javaXmlDecoder.loadXMLFromString(requestString);
                doc.getDocumentElement().normalize();
            } catch (Exception e) {
                logger.info("Unable to parse the object :" + e.getMessage());
                logger.info("Rejected Due to format is not match  ");
                String code = "login";
                String message = "syntax error";
                String refCode = "null";
                genericModel = xmlDomHandler.generateErrorXml(code, message, refCode);
                result = genericModel.getResult(); 
                return new ResponseEntity<String>(result, responseHeaders, HttpStatus.BAD_REQUEST);
            }
             NodeList rootList = doc.getElementsByTagName("data");
             if (rootList.getLength() == 0) {
                logger.info("Select rootNode is Missing");
                //  scopesController.setAcceptType("xml");
                // String message = "header not found";
                // String refCode = "null";
                // tokenService.ReferenceCodeGenerator("login", "");
                // GenericModel genericModel = new GenericModel();
                // genericModel = xmlDomHandler.generateErrorXml("login", message, refCode);
                // result = genericModel.getResult();
                // logger.info("Response : " + result);
                // return new ResponseEntity<String>(result, responseHeaders, HttpStatus.PRECONDITION_REQUIRED); // PRECONDITION_REQUIRED
            } 
            for (int i = 0; i < rootList.getLength(); i++) {
                Node node = rootList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    loginModel.username = element.getElementsByTagName("username").item(0).getTextContent();
                    loginModel.password = element.getElementsByTagName("password").item(0).getTextContent();
                    loginModel.client_id = element.getElementsByTagName("client_id").item(0).getTextContent();
                    logger.info(" xml Values :" + element.getElementsByTagName("username").item(0).getTextContent() + " :: " + loginModel.password + "  :: "
                    + element.getElementsByTagName("client_id").item(0).getTextContent());
                }
            }
        } else {
            logger.info("Accept format is Incorrect :" + contentType);
            scopesController.setAcceptType("xml");
            String message = "header not found";
            String refCode = "null";
            tokenService.ReferenceCodeGenerator("login", "");
            GenericModel genericModel = new GenericModel();
            genericModel = xmlDomHandler.generateErrorXml("login", message, refCode);
            result = genericModel.getResult();
            logger.info("Response : " + result);
            return new ResponseEntity<String>(result, responseHeaders, HttpStatus.PRECONDITION_REQUIRED); // PRECONDITION_REQUIRED
        }
        logger.info("Login Request Parameters " + loginModel.getUsername() + " " + loginModel.getPassword() + " "
                + loginModel.getClient_id());

        tokenService.ReferenceCodeGenerator("login", loginModel.getUsername());
        
        String acceptValue = request.getHeader("Accept");
        scopesController.setAcceptType(acceptValue);

        if (!(acceptValue.contains("json") || acceptValue.contains("xml"))) {
            logger.info(" Accept format is Incorrect :" + acceptValue);
            scopesController.setAcceptType("xml");
            String message = "header not found";
            String refCode = "null";
            GenericModel genericModel = new GenericModel();
            genericModel = xmlDomHandler.generateErrorXml("login", message, refCode);
            result = genericModel.getResult();
            logger.info("Response : " + result);
            return new ResponseEntity<String>(result, responseHeaders, HttpStatus.PRECONDITION_REQUIRED); // PRECONDITION_REQUIRED
        }

        if (userNamelength == null || loginModel.getUsername() == null
                || loginModel.getUsername().length() > userNamelength) {
            logger.info("Rejected Due to  UserName Value Not Correct  ");
            String code = "login";
            String message = "username not found";
            String refCode = "null";
            genericModel = xmlDomHandler.generateErrorXml(code, message, refCode);
            result = genericModel.getResult(); 
            return new ResponseEntity<String>(result, responseHeaders, HttpStatus.BAD_REQUEST);
        } else if (passwordLength == null || loginModel.getPassword() == null
                || loginModel.getPassword().length() > passwordLength) {
            logger.info("Rejected Due to password Value Not Correct  ");
            String code = "login ";
            String message = "password not found";
            String refCode = "null";
            genericModel = xmlDomHandler.generateErrorXml(code, message, refCode);
            result = genericModel.getResult();
            // result = xmlDomHandler.generateErrorXml(code, message, refCode);
            return new ResponseEntity<String>(result, responseHeaders, HttpStatus.BAD_REQUEST);
        } else if (!Optional.ofNullable(loginModel.getClient_id()).isPresent()) {
            logger.info("Rejected Due to No Client Value Not Correct  ");
            String code = "login";
            String message = "client_id not found";
            String refCode = "null";
            genericModel = xmlDomHandler.generateErrorXml(code, message, refCode);
            result = genericModel.getResult();
            // result = xmlDomHandler.generateErrorXml(code, message, refCode);
            return new ResponseEntity<String>(result, responseHeaders, HttpStatus.BAD_REQUEST);
        } else if (!Optional.ofNullable(TOTAL_SERVER).isPresent()) {
            logger.info("Rejected Due to No Headers/ Server Count  Not Correct  ");

            String message = "internal error";
            String refCode = "null";
            genericModel = xmlDomHandler.generateErrorXml("login", message, refCode);
            result = genericModel.getResult();
            // result = xmlDomHandler.generateErrorXml(code, message, refCode);
            return new ResponseEntity<String>(result, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // responseHeaders.add("Content-Type", "" + headerType + " ; charset=utf-8");
            String userIp = request.getHeader("HTTP_CLIENT_IP") == null
                    ? (request.getHeader("X-FORWARDED-FOR") == null ? request.getRemoteAddr()
                            : request.getHeader("X-FORWARDED-FOR"))
                    : request.getHeader("HTTP_CLIENT_IP");

            logger.info("  Client ID for Login Request " + loginModel.getClient_id() + " For UserName "
                    + loginModel.getUsername());
            genericModel = loginService.authenticationService(loginModel.getUsername(), loginModel.getPassword(),
                    loginModel.getClient_id(), TOTAL_SERVER, userIp, acceptValue);
            result = genericModel.getResult();
            logger.info("Login Information Result  " + result + " UserName " + loginModel.getUsername());
            if (genericModel.getHttpStatus() == HttpStatus.UNAUTHORIZED) {
                responseHeaders.add("WWW-Authenticate", "Bearer realm=\"Gifting Network\" ");
            }
            return new ResponseEntity<String>(result, responseHeaders, genericModel.getHttpStatus());
        }
    }

    @PostMapping(value = "/authentication/logout")
    @Consumes({ "application/xml", "application/json" })
    @Produces({ "application/xml", "application/json" })
    @ResponseBody
    public ResponseEntity<String> authenticationLogout(@RequestBody String val) {
        HttpHeaders responseHeaders = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            logger.info(key + " : " + value);
        }

        String acceptValue = request.getHeader("Accept");
        String accessToken = "";
        if (Optional.ofNullable(request.getHeader("Authorization")).isPresent()) {
            accessToken = request.getHeader("Authorization");
        }
        scopesController.setAcceptType(acceptValue);
        tokenService.ReferenceCodeGenerator("logout", accessToken);

        if (!(acceptValue.contains("json") || acceptValue.contains("xml"))) {
            logger.info(" Accept format is Incorrect :" + acceptValue);
            scopesController.setAcceptType("xml");
            String message = "header not found";
            String refCode = "null";
            genericModel = xmlDomHandler.generateErrorXml("logout", message, refCode);
            String result = genericModel.getResult();
            return new ResponseEntity<String>(result, responseHeaders, HttpStatus.PRECONDITION_REQUIRED);
        }
        if (!Optional.ofNullable(request.getHeader("Authorization")).isPresent()) {
            logger.info("Rejected Due to  token id  Not Present");
            String code = "logout";
            String message = "Authorization does not exist";
            String refCode = "null";
            genericModel = xmlDomHandler.generateErrorXml(code, message, refCode);
            String result = genericModel.getResult();
            return new ResponseEntity<String>(result, responseHeaders, HttpStatus.PRECONDITION_REQUIRED);
        }

        String result = loginService.authenticationsessionCheckerService(accessToken, "logout");
        if (result.equals("true")) {
            logger.info(" Delete session from DbTable " + accessToken);
            Boolean value = loginService.deleteBysessionId(accessToken);
            if (value == false) {
                logger.info("Rejected Due to session Not Deleted in DB ");
                String message = "internal error";
                String refCode = "null";
                genericModel = xmlDomHandler.generateErrorXml("logout", message, refCode);
                result = genericModel.getResult();
                // result = xmlDomHandler.generateErrorXml(code, message, refCode);
                logger.info("Logout Result : " + result);
                return new ResponseEntity<String>(result, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                result = loginService.generateLogoffXml();
                logger.info("Logout Result : " + result);
                return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);
            }
        } else {
            logger.info("Logout Result : " + result);
            if (genericModel.getHttpStatus() == HttpStatus.UNAUTHORIZED) {
                responseHeaders.add("WWW-Authenticate", "Bearer realm=\"Gifting Network\" ");
            }
            return new ResponseEntity<String>(result, responseHeaders, genericModel.getHttpStatus());
        }

    }

}