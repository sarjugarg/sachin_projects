package com.giftingnetwork.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.giftingnetwork.BootApplication;
import com.giftingnetwork.controller.ScopesController;
import com.giftingnetwork.formatter.XmlDomHandler;
import com.giftingnetwork.model.GenericModel;
import com.giftingnetwork.repo.LoginRepository;
import com.giftingnetwork.repo.RecommendationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    com.giftingnetwork.util.GenericFunctions  genericFunctions ;

    @Autowired
    Environment environment;
    @Autowired
    XmlDomHandler xmlDomHandler;

    @Autowired
    RecommendationRepository recommendationRepository;

    @Value("${TOTAL_SERVER}")
    Integer TOTAL_SERVER;

    Logger logger = LoggerFactory.getLogger(LoginService.class);

    public GenericModel authenticationService( GenericModel genericModel) {
        Set<String> set = new HashSet<String>();
        
        for (int i = 1; i <= TOTAL_SERVER; i++) {
            set.add(environment.getProperty("Server_Id_" + i));
            logger.info(environment.getProperty("Server_Id_" + i));
            if (environment.getProperty("Server_Id_" + i) == null) {
                logger.info("Rejected Due to Server ID " + i + " Not Found in File  ");
                 genericModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                 genericModel.setErrorTitle("Internal Error");
                genericModel = xmlDomHandler.generateErrorXml1(genericModel);
                return genericModel ;
            }
        }

        if (set.contains(genericModel.getClientId())) {
            scopesController.setDBName("localDb");
            String details = null;
            {
                scopesController.setDBName(genericModel.getClientId());
                details = loginRepository.getUserDetailsFromAuthUser(genericModel) ;
                logger.info(" User Name and PassWord Authentication   " + details);
                if (details.equals("true")) {
                    String sessionId = genericFunctions.generateHashValue(genericModel.getUsername(), genericModel.getPassword(), genericModel.getClientId());
                    genericModel.setAuthorization(sessionId);
                    scopesController.setDBName("localDb");
                    String refresh_token = genericFunctions.generateHashValue(sessionId,genericModel.getUsername(), genericModel.getPassword());
                    Date date = java.util.Calendar.getInstance().getTime();
                    Calendar cal = Calendar.getInstance();
                    long presentTime = cal.getTimeInMillis() / 1000;
                    cal.setTime(date);
                    cal.add(Calendar.MINUTE, Integer.parseInt(BootApplication.LoginSessionTimeOut));
                    String sessionTime = String.valueOf((cal.getTimeInMillis() / 1000) - presentTime);
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(date);
                    long newPresentTime = cal2.getTimeInMillis() / 1000;
                    cal2.add(Calendar.MINUTE, Integer.parseInt(BootApplication.LoginSessionTimeOut));
                    logger.info("Session Timeout   : " + cal.getTime() + " RefreshTimeout: " + cal2.getTime());
                    String.valueOf((cal2.getTimeInMillis() / 1000) - newPresentTime);
                    Boolean result = loginRepository.savesessioninLocalsessionDb(sessionId, genericModel.getClientId(),  genericModel.getUsername(), genericModel.getPassword() , genericModel.getCode(), refresh_token, cal.getTime(), cal2.getTime());
        
                            if (result == false) {
                        logger.info("Rejected Due to session Not Saved in DB");
                          genericModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                        genericModel.setErrorTitle("Internal Error");
                       genericModel = xmlDomHandler.generateErrorXml1(genericModel);
                      }
                    return xmlDomHandler.generateXmlContentForLogin( genericModel ,sessionTime);
                } else {
                    logger.info("Rejected Due to No UserName, Password Found  ");
                    genericModel.setHttpStatus(HttpStatus.UNAUTHORIZED);
                     genericModel.setErrorTitle("unauthorized access");
                     return xmlDomHandler.generateErrorXml1(genericModel);
                }
            }
 
        } else {
            logger.info("Rejected Due to Client Id provided Doesnot Exists ");
            genericModel.setHttpStatus(HttpStatus.UNAUTHORIZED);
            genericModel.setErrorTitle("client_id not found");
           genericModel = xmlDomHandler.generateErrorXml1(genericModel);
           return genericModel ;
        }
    }





    public GenericModel authenticationsessionCheckerService(GenericModel genericModel) {
        try {
              recommendationRepository.authenticatesessionId(genericModel);
            logger.info(" Authentication Check Result  " + genericModel.getStatus());
            if (  genericModel.getStatus() == null   ||    ! genericModel.getStatus().equals("access"))  {
                logger.info("Rejected Due to Session Id provided by User doesnot exists ");
                genericModel.setHttpStatus(HttpStatus.UNAUTHORIZED);
                genericModel.setErrorTitle("unauthorized access");
                  xmlDomHandler.generateErrorXml1(genericModel);
            }
            return genericModel ;
        } catch (Exception e) {
            logger.error(e.getMessage());
            genericModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            genericModel.setErrorTitle("Server Error");
            return    xmlDomHandler.generateErrorXml1(genericModel);
        }
    }

    public Boolean deleteBysessionId(String sessionId) {
        scopesController.setDBName("localDb");
        logger.info("Delete from Db by sessionid  " + sessionId);
        return loginRepository.deletefromTokenBySessionId(sessionId);
    }

    public String generateLogoffXml(  GenericModel genericModel) {
        return xmlDomHandler.generateLogOffXml(genericModel);
    }

    public Boolean deleteByrefershToken(String accessToken) {
        return loginRepository.deletebyRefreshTokenFromDb(accessToken);
    }

}
