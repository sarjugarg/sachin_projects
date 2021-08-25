/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine;

import java.sql.Connection;
import java.io.BufferedWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
public class EXISTS_IN_FOREIGN_DB {

     static final Logger logger = Logger.getLogger(EXIST_IN_CUSTOM_DB.class);

     static String executeRule(String[] args, Connection conn) {
          String res = "";
          Statement stmt2 = null;
          ResultSet result1 = null;
          try {

               stmt2 = conn.createStatement();

               String qry = "select count(regularize_device_db.nid) from regularize_device_db inner join end_userdb on end_userdb.nid=regularize_device_db.nid where "
                       + " first_imei='" + args[3] + "' or second_imei='" + args[3] + "' or third_imei='" + args[3] + "' or fourth_imei='" + args[3] + "' and nationality<>'Cambodian'  ";
               logger.debug(" Foreign Db " + qry);
               result1 = stmt2.executeQuery(qry);

               int res2 = 0;
               try {
                    while (result1.next()) {
                         res2 = result1.getInt(1);
                    }
               } catch (Exception e) {
                    logger.debug("");
               }
               if (res2 != 0) {
                    res = "Yes";
               } else {
                    res = "No";
               }

          } catch (Exception e) {
               logger.error("error.." + e);
          } finally {
               try {
                    result1.close();
                    stmt2.close();
               } catch (Exception ex) {
                    logger.error("Error" + ex);
               }
          }

          return res;
     }

     static String executeAction(String[] args, Connection conn, BufferedWriter bw) {
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
                         logger.debug("Action is Reject");

                         String fileString = args[15] + " ,Error Code :CON_RULE_0018, Error Description : IMEI/ESN/MEID is already present in the system  ";
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
               logger.debug(" Error " + e);
               return "Failure";
          }

     }

}
