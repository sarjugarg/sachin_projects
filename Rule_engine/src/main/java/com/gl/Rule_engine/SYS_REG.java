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
class SYS_REG {

     static final Logger logger = Logger.getLogger(SYS_REG.class);

     static String executeRule(String[] args, Connection conn) {
          String res = "No";
          ResultSet result1 = null;
          Statement stmt2 = null;

          String actnRslt = "";
          String qury = null;
          try {
               stmt2 = conn.createStatement();
               qury = " select action from device_usage_db  where  imei ='" + args[3] + "'";
              logger.debug("" + qury);
               result1 = stmt2.executeQuery(qury);
               try {
                    while (result1.next()) {
                         actnRslt = result1.getString(1);
                    }
               } catch (Exception e) {
                    logger.error("" + e);
               }
               if (actnRslt.equals("SYS_REG")) {
                    res = "Yes";
               } else {
                    qury = " select action from device_duplicate_db  where  imei ='" + args[3] + "'";
                    logger.debug("" + qury);
           
                    result1 = stmt2.executeQuery(qury);
                    try {
                         while (result1.next()) {
                              actnRslt = result1.getString(1);
                         }
                    } catch (Exception e) {
                         logger.error("" + e);
                         if (actnRslt.equals("SYS_REG")) {
                              res = "Yes";
                         } else {
                              res = "No";
                         }
                    }
                    result1.close();
                    stmt2.close();
               }
          } catch (Exception e) {
               logger.error("" + e);
          }finally {
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

                         String fileString = args[15] + " , Error Code :CON_RULE_0030 , Error Description : IMEI/ESN/MEID is System Registered ";
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
                     // set action as SYS_REG

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
