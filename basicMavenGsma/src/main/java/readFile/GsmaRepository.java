package readFile;

import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
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
               logger.debug("databaseMapper.");
               Gson gson = new Gson();
               GsmaEntity product = gson.fromJson(message, GsmaEntity.class);
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               String val = sdf.format(new Date());
//               String now = "TO_DATE( '" + val + "','YYYY-MM-DD HH24:MI:SS')";
               String now = "current_timestamp";

               String getDeviceCertifybody = product.getDeviceCertifybody().contains("[") ? product.getDeviceCertifybody().replace("[", " ") : product.getDeviceCertifybody();
               getDeviceCertifybody = getDeviceCertifybody.contains("]") ? getDeviceCertifybody.replace("]", " ") : getDeviceCertifybody;
               logger.debug("gsma_tac_db getDeviceCertifybody ...." + getDeviceCertifybody);
               String getOperatingSystem = product.getOperatingSystem().contains("[") ? product.getOperatingSystem().replace("[", " ") : product.getOperatingSystem();
               getOperatingSystem = getOperatingSystem.contains("]") ? getOperatingSystem.replace("]", " ") : getOperatingSystem;
               logger.debug("gsma_tac_db getOperatingSystem ...." + getOperatingSystem);
               String getRadioInterface = product.getRadioInterface().contains("[") ? product.getRadioInterface().replace("[", " ") : product.getRadioInterface();
               getRadioInterface = getRadioInterface.contains("]") ? getRadioInterface.replace("]", " ") : getRadioInterface;
               logger.debug("gsma_tac_db getRadioInterface ...." + getRadioInterface);
               String sqlqry = "insert into  gsma_tac_db ( created_on, status_message,  device_id, band_name ,model_name,internal_model_name ,marketing_name,equipment_type ,sim_support,"
                       + " nfc ,    wlan,bluetooth  ,lpwan  ,manufacturer_or_applicant, tac_approved_date , gsma_approved_tac, operating_system , device_certify_body , radio_interface ,  status_code , brand_name , modified_on ) "
                       + "  Values ( " + now + ", "
                       + " '" + product.getStatusMessage() + "', '" + product.getDeviceId() + "', "
                       + " '" + product.getBrandName() + "' ,"
                       + " '" + product.getModelName() + "', "
                       + " '" + product.getInternalModelName() + "',"
                       + " '" + product.getMarketingName() + "', '" + product.getEquipmentType() + "', '" + product.getSimSupport() + "', '" + product.getNfcSupport() + "',"
                       + " '" + product.getWlanSupport() + "', '" + product.getBlueToothSupport() + "', '" + product.getLpwan() + "', '" + product.getManufacturer() + "', '" + product.getTacApprovedDate() + "', '" + product.getGsmaApprovedTac() + "', '" + getOperatingSystem + "', '" + getDeviceCertifybody + "', '" + getRadioInterface + "', '" + product.getStatusCode() + "', "
                       + " '" + product.getBrandName() + "',  " + now + "   "
                       + ") ";

               stmt.executeQuery(sqlqry);
               logger.debug(" sqlqry " + sqlqry);
               logger.debug("product.getBrandName()" + product.getBrandName());
               if (!product.getBrandName().equals("NA")) {
                    logger.debug("Excecution start for brand and Model");
                    String srt = "insert into brand_name( brand_name  ,created_on, MODIFIED_ON ) VAlues(   '" + product.getBrandName() + "'  , current_timestamp , current_timestamp ) ";
                    logger.debug("insert ....." + srt);
                    try {
                         stmt.executeQuery(srt);
                    } catch (Exception e) {
                         logger.warn("Warning : brand_name Already Exists " + e);
                    }
                    srt = "insert into model_name( model_name,brand_name , brand_name_id ,  created_on , MODIFIED_ON) "
                            + "VAlues( '" + product.getModelName() + "','" + product.getBrandName() + "' ,(select id from brand_name where brand_name = '" + product.getBrandName() + "') ,  current_timestamp,current_timestamp  )";
                    logger.debug("Query  insert model_name.. " + srt);
                    try {
                         stmt.executeQuery(srt);
                    } catch (Exception e) {
                         logger.warn(" Model_namse Already Exist " + e);
                    }
               } else {
                    logger.debug("Data will not Save  in Model_name  and Brand_name as It shows NA ");
               }
//               stmt.close();
          } catch (Exception e1) {
               logger.error("" + e1);
          } finally {
               try {
                    stmt.close();
                    conn.commit();
               } catch (SQLException ex) {
                    logger.error("Sql Error :" + ex);
                    logger.error("DataError  " + ex);
                    java.util.logging.Logger.getLogger(GsmaDbDao.class.getName()).log(Level.SEVERE, null, ex);
               }
          }
          return "";
     }

     public String getExistingGsmaDetails(String imeiTac, Connection conn, BufferedWriter bw1) {
          logger.debug("checking for getExistingGsmaDetails");
          int resultid = 0;
          String output = "NAN";
          ResultSet resultmsdn = null;
          Statement stmt = null;
          try {
               stmt = conn.createStatement();
               String qry = " select * from gsma_tac_db where  device_id = '" + imeiTac + "'  ";
               resultmsdn = stmt.executeQuery(qry);
               try {
                    while (resultmsdn.next()) {
                         resultid = 1;
                         bw1.write(imeiTac + "|" + resultmsdn.getString("band_name") + "|" + resultmsdn.getString("model_name")   + " | " +  resultmsdn.getString("equipment_type") + " | "  +   resultmsdn.getString("operating_system") );
                         bw1.newLine();
                         bw1.flush();
                    }
               } catch (Exception e) {
                    logger.error("Database1  " + e);
               }
               logger.debug(" resultid " + resultid);
               if (resultid == 0) {
                    qry = " select * from gsma_invalid_tac_db where  tac = '" + imeiTac + "'  ";
                    resultmsdn = stmt.executeQuery(qry);
                    try {
                         while (resultmsdn.next()) {
                              resultid = 1;
                              bw1.write(imeiTac + "| NA  | NA | NA | NA");
                              bw1.newLine();
                              bw1.flush();
                         }
                    } catch (Exception e) {
                         logger.error("Database2  " + e);
                    }
                    if (resultid == 0) {
                         output = "NAN";
                    } else {
                         output = "No";
                    }

                    logger.debug(" resultid " + resultid);
               } else {
                    output = "Yes";
               }

               logger.debug(" output " + output);
          } catch (Exception e) {
               logger.error("Error P." + e);
          } finally {
               try {
                    resultmsdn.close();
                    stmt.close();
               } catch (Exception e) {
                    logger.error("Error N." + e);
               }
          }

          return output;

     }

     void invalidGsmaDb(String deviceId, Connection conn) {
          Statement stmt = null;
          try {
               String dateFunction = "sysdate";

               try {
                    stmt = conn.createStatement();
               } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(GsmaDbDao.class.getName()).log(Level.SEVERE, null, ex);
               }
               stmt.executeQuery("insert into gsma_invalid_tac_db (created_on , tac  ,modified_on  ) values ( current_timestamp , '" + deviceId + "',current_timestamp )    ");
               logger.error("IMEI is not GsmaApprovedTac...  " + "insert into gsma_invalid_tac_db (created_on , tac , modifiedON) values (  current_timestamp  , '" + deviceId + "'  , current_timestamp  )    ");

          } catch (Exception e) {
               logger.error("Error + " + e);
          } finally {
               try {
                    stmt.close();
                    conn.commit();
               } catch (SQLException ex) {
                    logger.error("Error N." + ex);
                    java.util.logging.Logger.getLogger(GsmaDbDao.class.getName()).log(Level.SEVERE, null, ex);
               }
          }

     }

     Map<String, String> getGsmaApiDetails(Connection conn) {

          Map<String, String> map = new HashMap<String, String>();
          Statement stmt = null;
          try {
               stmt = conn.createStatement();
               String str = " select tag , value from system_configuration_db where tag in ('gsma_tac_APIKey' , 'gsma_tac_Password','gsma_tac_Salt_String' , 'gsma_tac_Organization_Id' ,'gsma_tac_Secretkey', 'gsma_tac_httpPostUrl',  'gsma_tac_timewait')  ";
               ResultSet result = stmt.executeQuery(str);
               logger.debug("  " + str);
               while (result.next()) {
                    map.put(result.getString("tag"), result.getString("value"));
               }
               result.close();
               stmt.close();
          } catch (Exception e) {
               logger.error("Error " + e);
          }
          return map;
     }

}



