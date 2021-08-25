package com.giftingnetwork.api;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    ScopesController scopesController;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    TokenService sessionService;
    @Autowired
    Environment environment;
    @Autowired
    XmlDomHandler xmlDomHandler;
    @Autowired
    GenericModel genericModel ;
    @Autowired 
    HttpServletRequest request;

    Logger logger = LoggerFactory.getLogger(LoginService.class);

    public GenericModel authenticationService(String username, String password, String clientID, int TOTAL_SERVER,
            String userIp , String contentType) {
        Set<String> set = new HashSet<String>();
        for (int i = 1; i <= TOTAL_SERVER; i++) {
            set.add(environment.getProperty("Server_Id_" + i));
            logger. info(environment.getProperty("Server_Id_" + i));
            if (environment.getProperty("Server_Id_" + i) == null) {
                logger.info("Rejected Due to  Server ID " + i + " Not Found in File  ");
                String message = "Internal Error";
                String refCode = "null";
                genericModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                return xmlDomHandler.generateErrorXml("login", message, refCode);
            }
        }
        if (set.contains(clientID)) {
            scopesController.setDBName("localDb");
            String details = null;
           // BootApplication.DBName = "localDb";
         //   String details = loginRepository.checkCurrentSessionInLocalsessionDb(clientID, username, password);
   //    logger.info(" Session Value result  " + details + " For UserName " + username);
      //      if (details.equals("true")) 
            { 
                scopesController.setDBName(clientID);
                details = loginRepository.getUserDetailsFromAuthUser(clientID, username, password);
                logger.info(" User Name and PassWord Authentication   " + details);
                if (details.equals("true")) {
                    String sessionId = sessionService.generatesessionId( username ,password , clientID);
                    scopesController.setDBName("localDb");
                    Boolean result = loginRepository.savesessioninLocalsessionDb(sessionId, clientID, username,
                            password, userIp);
                    if (result == false) {
                        logger.info("Rejected Due to session Not Saved in DB");
                        String message = " Internal Error";
                        String refCode = "null";
                        genericModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                        return xmlDomHandler.generateErrorXml("Login ", message, refCode);
                    }
                    return xmlDomHandler.generateXmlContentForLogin(sessionId, "success" , contentType);
                } else {
                    logger.info("Rejected Due to No UserName, Password Found  ");
                    String code = "login";
                    String message = "unauthorized access";
                    String refCode = "null";
                    genericModel.setHttpStatus(HttpStatus.UNAUTHORIZED);
                    return xmlDomHandler.generateErrorXml(code, message, refCode);
                }
            } 
            // else {
            //     logger.info("Rejected Due to  Session for Current UserName Password already Exists  ");
            //     String code = "login";
            //     String message = "session already exists";
            //     String refCode = "null";
            //     genericModel.setHttpStatus(HttpStatus.UNAUTHORIZED);
            //     return xmlDomHandler.generateErrorXml(code, message, refCode);
            // }
        } else {
            logger.info("Rejected Due to Client Id provided Doesnot Exists ");
            String code = "login_error";
            String message = "client_id not found";
            String refCode = "null";
            genericModel.setHttpStatus(HttpStatus.UNAUTHORIZED);
            return xmlDomHandler.generateErrorXml(code, message, refCode);
        }
    }

    public String authenticationsessionCheckerService(String session, String code) {
        try {
            logger.info("accesstoken ID " + session);
            String  result = sessionService.authenticatesessionId(session);
            logger.info(" Authentication Check Result  " + result);
            if (! result.equals("false") ) { 
                return "true";
            } else {
                logger.info("Rejected Due to Session Id provided by User doesnot exists ");
                String message = "unauthorized access";
                String refCode = "null";
                genericModel.setHttpStatus(HttpStatus.UNAUTHORIZED);
                return xmlDomHandler.generateErrorXml(code  , message, refCode).getResult();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            String message = "Server Error";
            String refCode = "null";
            return xmlDomHandler.generateErrorXml(code  , message, refCode).getResult();
        }
    }

    public Boolean deleteBysessionId(String  sessionId) { 
        scopesController.setDBName("localDb");
        logger.info("Delete from Db by sessionid  " +sessionId);
        return loginRepository.deletefromTokenBySessionId(sessionId);
    }

    public String generateLogoffXml() {
        return xmlDomHandler.generateLogOffXml();
    }

    

}
