package com.giftingnetwork.api;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class TokenService {

  @Autowired
  ScopesController scopesController;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  LoginRepository loginRepository;

  Logger logger = LoggerFactory.getLogger(TokenService.class);

  public String generatesessionId(String username, String password, String clientID) {
    Date date = new Date();
    long timeMilli = date.getTime();
    String sessionId = encryptThisString(String.valueOf(timeMilli) + username);
    logger.info("  Generated  : " + sessionId + " for username " + username);
    return sessionId;
  }

  public String encryptThisString(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] messageDigest = md.digest(input.getBytes());
      BigInteger no = new BigInteger(1, messageDigest);
      String hashtext = no.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      logger.error(e.getMessage());
      return "false";
    }
  }

  public String authenticatesessionId(String sessionId) {
    try {
      if (!sessionId.startsWith("Bearer ")) {
        logger.info("Bearer Not found in accessToken " + sessionId);
        return "false";
      }
      sessionId = sessionId.substring(7);
      scopesController.setDBName("localDb");
      String QueryString = "select client_id, init_time , username , password  from token_db_mapping where token_id  = '"
          + sessionId + "' ";
      logger.info(QueryString);
      return jdbcTemplate.query(QueryString, (ResultSet rs) -> {
        String data = "false";  
        while (rs.next()) {
          logger.info("Init Value :   " + rs.getString("init_time"));
          String session_value = BootApplication.LoginSessionTimeOut;
          DateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
          Date d = null;
          try {
            d = formatter.parse(rs.getString("init_time"));
          } catch (Exception e) {
            logger.error("  Error " + e);
          }
          Calendar cal = Calendar.getInstance();
          cal.setTime(d);
          cal.add(Calendar.MINUTE, Integer.parseInt(session_value));

          Date date = java.util.Calendar.getInstance().getTime();
          logger.info("Init time after  " + rs.getString("init_time") + " mins : " + cal.getTime()
              + " . Current Time : " + date + " ");
          if (cal.getTime().before(date)) {
            logger.info(" Session Time out for  " + rs.getString("username"));
            loginRepository.deleteByUserNamePassword(rs.getString("username"), rs.getString("password"));
            data =  "session time out";
          } else {
            logger.info(" Session Going On for  " + rs.getString("username") + " with ClientId " +rs.getString("client_id") );
            scopesController.setDBName(rs.getString("client_id"));
            data = "true";
          }
        }
        return data;
      });
    } catch (Exception e) {
      logger.error(e.getMessage());
      return "false";
    }
  }

  public void ReferenceCodeGenerator(String api_name, String username) {
    try {
      if ( ! api_name.equals("login") ) {
        username = getUserNameFromSessionId(username);
      }
      logger.info("generate Reference Code with api_name "+ api_name + " username : " +username );
      String rand = String.valueOf(new Random().nextInt(1000) + 1);
      String encrpRefCode = generatesessionId(username, api_name, rand);
      scopesController.setRefCode(encrpRefCode); 
      MDC.put("first", "RefCode: " + encrpRefCode);
    } catch (Exception e) {
      logger.error(e.getMessage());
    }
  }

  public String getUserNameFromSessionId(String sessionId) {
    try { 
      scopesController.setDBName("localDb");
      String QueryString = "select username  from token_db_mapping where token_id  = '" + sessionId + "' ";
      logger.info(QueryString);
      return jdbcTemplate.query(QueryString, (ResultSet rs) -> {
        String data = "false";
        while (rs.next()) {
          data = rs.getString(1);
        }
        return data;
      });
    } catch (Exception e) {
      logger.error(e.getMessage());
      return "false";
    }
  }

  public String selectDb() {
    try {
      scopesController.setDBName("localDb");
      String QueryString = "  select id , parameter_name from public.api_column_value_mapping  order by id  ";
      logger.info(QueryString);
      return jdbcTemplate.query(QueryString, (ResultSet rs) -> {
        String data = "false";
        while (rs.next()) {
          String inputString = rs.getString("parameter_name"); // get user input
          String outputString = "" + inputString.charAt(0);

          for (int i = 1; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            outputString += Character.isUpperCase(c) ? "_" + c : c;
          }
          logger.info("...s " + outputString);
          updateDb(rs.getInt("id"), outputString);
        }
        return data;
      });
    } catch (Exception e) {
      logger.error(e.getMessage());
      return "false";
    }
  }

  void updateDb(int int1, String outputString) {
    try {
      String query = "update public.api_column_value_mapping set parameter_name = ? where id = ?  ";
      logger.info("[" + query + "]" + outputString + "  ::" + int1);
      jdbcTemplate.update(query, new Object[] { outputString, int1 });
    } catch (Exception e) {
      logger.error(e.getMessage());

    }
  }

}
