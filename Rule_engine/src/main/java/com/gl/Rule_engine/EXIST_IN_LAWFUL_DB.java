/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.BufferedWriter;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class EXIST_IN_LAWFUL_DB {

     static final Logger logger = Logger.getLogger(EXIST_IN_LAWFUL_DB.class);

     public static String executeRule(String[] args, Connection conn) {
          Statement stmt = null;
          ResultSet result2 = null;
          String res = "No";
          try {
               int count1 = 0;
               stmt = conn.createStatement();

               result2 = stmt.executeQuery("select  count(imei_esn_meid) from device_lawful_db where imei_esn_meid='" + args[3] + "' ");
               logger.debug("Qury ..select count(imei_esn_meid) from device_lawful_db where imei_esn_meid='" + args[3] + "' ");
               try {
                    while (result2.next()) {
                         count1 = result2.getInt(1);
                    }
               } catch (Exception e) {
                    logger.debug("E2." + e);
               }
               if (count1 != 0) {
                    res = "YES";
               } else {
                    res = "NO";
               }
               result2.close();
               stmt.close();
          } catch (Exception e) {
               logger.error(" " + e);
          } finally {
               try {
                    result2.close();
                    stmt.close();
               } catch (Exception e) {
                    logger.error("Error  ::" + e);

               }
          }
          return res;
     }

     static String executeAction(String[] args, Connection conn, BufferedWriter bw) {
//        logger.debug("LBD executeAction");
          try {
               switch (args[13]) {
                    case "Allow": {
                         logger.debug("Action is Allow");
                    }
                    break;
                    case "Skip": {
                         logger.debug("Action is Skip");
                    }
                    break;
                    case "Reject": {
                         logger.debug("Lawful Db  Action is Reject");
                         String errmsg = " , Error Code :CON_RULE_0024 , Error Description :  IMEI is  not Present. (It is not marked as Stolen )  , ";
                         if (args[2].equalsIgnoreCase("stolen")) {
                              errmsg = " , Error Code :CON_RULE_0025 , Error Description :  IMEI/ESN/MEID  is  already marked as Stolen  ";
                         }
                         String fileString = args[15] + "" + errmsg;
                         bw.write(fileString);
                         bw.newLine();
                    }
                    break;
                    case "Block": {
                         logger.debug("Action is Block");
                    }
                    break;
                    case "Report": {
                         logger.debug("Action is Report");

                    }
                    break;
                    case "SYS_REG": {
                         logger.debug("Action is SYS_REG");
                    }
                    break;
                    case "USER_REG": {
                         logger.debug("Action is USER_REG");
                    }
                    break;
                    default:
                         logger.debug(" The Action " + args[13] + "  is Not Defined  ");
               }
               return "Success";
          } catch (Exception e) {
               logger.error("Error.." + e);
          }
          return "Failure";

     }

}
