package com.example;

import com.google.gson.Gson;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;

class GsmaDbDao {

     static final Logger logger = Logger.getLogger(GsmaDbDao.class);

     public String databaseMapper(String message, Connection conn) {

          Statement stmt = null;
          try {
               stmt = conn.createStatement();
          } catch (SQLException ex) {
               java.util.logging.Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
          }

          try {
               logger.info("databaseMapper.");
               Gson gson = new Gson();
 

               GsmaEntity product = gson.fromJson(message, GsmaEntity.class);
               DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss");
               LocalDateTime now1 = LocalDateTime.now();
               String now = dtf.format(now1);
               String getDeviceCertifybody = product.getDeviceCertifybody().contains("[") ? product.getDeviceCertifybody().replace("[", " ") : product.getDeviceCertifybody();
               getDeviceCertifybody = getDeviceCertifybody.contains("]") ? getDeviceCertifybody.replace("]", " ") : getDeviceCertifybody;
               logger.info("gsma_tac_db getDeviceCertifybody ...." + getDeviceCertifybody);
               String getOperatingSystem = product.getOperatingSystem().contains("[") ? product.getOperatingSystem().replace("[", " ") : product.getOperatingSystem();
               getOperatingSystem = getOperatingSystem.contains("]") ? getOperatingSystem.replace("]", " ") : getOperatingSystem;
               logger.info("gsma_tac_db getOperatingSystem ...." + getOperatingSystem);
               String getRadioInterface = product.getRadioInterface().contains("[") ? product.getRadioInterface().replace("[", " ") : product.getRadioInterface();
               getRadioInterface = getRadioInterface.contains("]") ? getRadioInterface.replace("]", " ") : getRadioInterface;
               logger.info("gsma_tac_db getRadioInterface ...." + getRadioInterface);
               String sqlqry = "insert into  gsma_tac_db ( created_on, status_message,  device_id, band_name ,model_name,internal_model_name ,marketing_name,equipment_type ,sim_support,"
                       + " nfc ,    wlan,bluetooth  ,lpwan  ,manufacturer_or_applicant, tac_approved_date , gsma_approved_tac, operating_system , device_certify_body , radio_interface ,  status_code , brand_name , modified_on ,  brand_name_new , model_name_new )  "
                       + " Values (  current_timestamp , "
                       + " '" + product.getStatusMessage() + "', '" + product.getDeviceId() + "', "
                       + " '" + product.getBrandName() + "' ,"
                       + " '" + product.getModelName() + "', "
                       + "'" + product.getInternalModelName() + "',"
                       + " '" + product.getMarketingName() + "', '" + product.getEquipmentType() + "', '" + product.getSimSupport() + "', '" + product.getNfcSupport() + "',"
                       + " '" + product.getWlanSupport() + "', '" + product.getBlueToothSupport() + "', '" + product.getLpwan() + "', '" + product.getManufacturer() + "', '" + product.getTacApprovedDate() + "', '" + product.getGsmaApprovedTac() + "', '" + getOperatingSystem + "', '" + getDeviceCertifybody + "', '" + getRadioInterface + "', '" + product.getStatusCode() + "', "
                       + " '" + product.getBrandName() + "', current_timestamp , '" + product.getBrandName().toUpperCase() + "'  , '" + product.getModelName().toUpperCase() + "'                "
                       + ") ";

               stmt.executeQuery(sqlqry);
               logger.info(" sqlqry " + sqlqry);
               System.out.println(" sqlqry " + sqlqry);
               logger.info("product.getBrandName()" + product.getBrandName());
               if (!product.getBrandName().equals("NA")) {
                    logger.info("Excecution start for brand and Model");
                    String srt = "insert into brand_name( brand_name  ,created_on, MODIFIED_ON ) VAlues(   '" + product.getBrandName() + "'  , current_timestamp ,  current_timestamp  ) ";
                    logger.info("insert ....." + srt);
                    try {
                         stmt.executeQuery(srt);
                    } catch (Exception e) {
                         logger.warn("Warning : brand_name Already Exists " + e);
                    }
                    srt = "insert into model_name( model_name,brand_name , brand_name_id ,  created_on , MODIFIED_ON) "
                            + "VAlues( '" + product.getModelName() + "','" + product.getBrandName() + "' ,(select id from brand_name where brand_name = '" + product.getBrandName() + "') , current_timestamp ,current_timestamp  )";
                    logger.info("Query  insert model_name.. " + srt);
                    try {
                         stmt.executeQuery(srt);
                    } catch (Exception e) {
                         logger.warn(" Model_namse Already Exist " + e);
                    }
               } else {
                    logger.info("Data will not Save  in Model_name  and Brand_name as It shows NA ");
               }
               stmt.close();
          } catch (Exception e1) {
               logger.error("" + e1);
          } finally {
               try {
                    stmt.close();
               } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(GsmaDbDao.class.getName()).log(Level.SEVERE, null, ex);
               }
          }
          return "";
     }

     public Map<String, String> getExistingGsmaDetails(Connection conn) {
          String resultid = "0";
          Map<String, String> map = new HashMap<String, String>();
          try {

               Statement stmt = conn.createStatement();

               String str = " select tag , value from system_configuration_db where tag in ('gsma_tac_APIKey' , 'gsma_tac_Password','gsma_tac_Salt_String' , 'gsma_tac_Organization_Id' ,'gsma_tac_Secretkey', 'gsma_tac_httpPostUrl',  'gsma_tac_timewait')  ";
               ResultSet result = stmt.executeQuery(str);
               logger.debug("  " + str);
               while (result.next()) {
                    map.put(result.getString("tag"), result.getString("value"));
               }

               result.close();
               stmt.close();

          } catch (Exception e) {
               logger.info("Error." + e);
          }

          return map;

     }

     void invalidGsmaDb(String deviceId, Connection conn) {
          Statement stmt = null;
          try { 
               try {
                    stmt = conn.createStatement();
               } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(GsmaDbDao.class.getName()).log(Level.SEVERE, null, ex);
               }
               String qury = "insert into gsma_invalid_tac_db (created_on , tac  ,modified_on  ) values ( current_timestamp , '" + deviceId + "',   current_timestamp  )    " ;
              logger.info( "[ "+qury + "]");
               stmt.executeQuery("insert into gsma_invalid_tac_db (created_on , tac  ,modified_on  ) values ( current_timestamp , '" + deviceId + "',   current_timestamp  )    ");
       
          } catch (Exception e) {
               logger.error("Error + " + e);
          } finally {
               try {
                    stmt.close();
               } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(GsmaDbDao.class.getName()).log(Level.SEVERE, null, ex);
               }
          }

     }

}

