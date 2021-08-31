package com.giftingnetwork.repo;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import com.giftingnetwork.BootApplication;
import com.giftingnetwork.controller.ScopesController;
import com.giftingnetwork.formatter.JavaJsonEncoderNew2;
import com.giftingnetwork.formatter.XmlDomHandler;
import com.giftingnetwork.model.GenericModel;

 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class RecommendationRepository {
 
  @Autowired
  private JdbcTemplate jdbcTemplate;
 
  @Autowired
  JavaJsonEncoderNew2 javaJsonEncoder2;
  
  @Autowired
  ScopesController scopesController;

  @Autowired
  XmlDomHandler xmlDomHandler;

  @Autowired
  LoginRepository loginRepository;
 
   Logger logger = LoggerFactory.getLogger(RecommendationRepository.class);

  public String getXmlFromQuery(  String query,   GenericModel genericModel) {
     try {
      LocalDateTime localDateTime = LocalDateTime.now();
       logger.info("Start  Time " +  localDateTime);
      return jdbcTemplate.query(query, (ResultSet rs) -> {
        return javaJsonEncoder2.toDocument (  rs,   query  ,  genericModel );
      });
     } catch (Exception e) {
      logger.error(e.getCause() + " :error: " + e.getMessage());
      genericModel.setErrorDetail("server Error");
      genericModel.setErrorTitle("internal Error");
      genericModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
      xmlDomHandler.generateErrorXml1(genericModel);
      return genericModel.getResult();
     } 
  } 

  public void insertintoAuditLogging(  GenericModel genericModel) {
    try {
      scopesController.setDBName("localDb");
      String query = " insert into audit_db  ( date, username, token, client_id ,api_name  ,api_paramater , output_status_code  )  values ( now() , '"+genericModel.getUsername()+ "'  , '"+ genericModel.getAuthorization()+ "' ,  '"+genericModel.getClientId()+ "' ,  '"+genericModel.getApiName()+ "' ,  '"+genericModel.getQueryString()+ "',  '"+genericModel.getHttpStatus().value()+ "' ) ";
      logger.info("[" + query + "]"  );
       jdbcTemplate.update(query,new Object[] {});
            } catch (Exception e) {
      logger.error(e.getMessage()  + "," + e.getLocalizedMessage());
    }

  }




  public GenericModel authenticatesessionId(GenericModel genericModel) {
    try {
      genericModel.setErrorTitle("unauthorized access");
      if (!genericModel.getAuthorization().startsWith("Bearer ")) {
        logger.info("Bearer Not found in accessToken " + genericModel.getAuthorization());
        return genericModel;
      }
      String sessionId = genericModel.getAuthorization().substring(7);
      scopesController.setDBName("localDb");
      String QueryString = "select client_id, token_id_expire_time , username , password  from token_db_mapping where token_id  = '"
          + sessionId + "' ";
      logger.info(QueryString);
      return jdbcTemplate.query(QueryString, (ResultSet rs) -> {
        while (rs.next()) {
          logger.info("Token Expire Time: " + rs.getString("token_id_expire_time"));
          DateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
          Date d = null;
          try {
            d = formatter.parse(rs.getObject("token_id_expire_time").toString());
          } catch (Exception e) {
            logger.error("  Error " + e);
          }
          Calendar cal = Calendar.getInstance();
          cal.setTime(d);
          Date date = java.util.Calendar.getInstance().getTime();
          logger.info("Token Expire Time  after  : " + cal.getTime() + " . Current Time : " + date + " ");
          if (cal.getTime().before(date)) {
            logger.info(" Session Time out for  " + rs.getString("username"));
            loginRepository.deleteByUserNamePassword(rs.getString("username"), rs.getString("password"));
            genericModel.setErrorTitle("session time out");
          } else {
            logger.info("Session Going for " + rs.getString("username") + " ClientId " + rs.getString("client_id"));
            genericModel.setClientId(rs.getString("client_id"));
            genericModel.setUsername(rs.getString("username"));
            scopesController.setDBName(rs.getString("client_id"));
            genericModel.setStatus("access");
          }
        }
        return genericModel;
      });
    } catch (Exception e) {
      logger.error(e.getMessage());
      genericModel.setErrorTitle("unauthorized access");
      return genericModel;
    }
  }

  

  

  public void updateTokenTime(String authorization) {
    scopesController.setDBName("localDb");
    authorization = authorization.substring(7);
    String mins = BootApplication.LoginSessionTimeOut;
    try {
      String query = "  update token_db_mapping  set refresh_token_expire_time = ( now()  +  INTERVAL '" + mins
          + " MINUTE' )  ,   token_id_expire_time = ( now()  +  INTERVAL '" + mins
          + " MINUTE' )   where token_id =    '" + authorization + "'";
      logger.info("[" + query + "]");
      jdbcTemplate.update(query, new Object[] {});
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

   

}