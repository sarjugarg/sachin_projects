/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.util.Calendar;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
class EXISTS_IN_TYPE_APPROVED_DB {

     static final Logger logger = Logger.getLogger(EXISTS_IN_TYPE_APPROVED_DB.class);

     static String executeRule(String[] args, Connection conn) {
          String res = "";
          Statement stmt2 = null;
          ResultSet result1 = null;
          try {
               stmt2 = conn.createStatement();  // state = 6
               result1 = stmt2.executeQuery("select count(tac) as cnt  from type_approved_db where tac='" + args[3].substring(0, 8) + "'     ");   //and    status = 6
               logger.debug("select count(tac) as cnt  from type_approved_db where tac='" + args[3].substring(0, 8) + "' ");
               int res1 = 0;

               while (result1.next()) {
                    res1 = result1.getInt(1);
               }
               if (res1 == 0) {
                    res = "No";
               } else {
                    res = "Yes";
               }
               result1.close();
               stmt2.close();
          } catch (Exception e) {
               logger.error("Error" + e);
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
                         String fileString = args[15] + ", Error Code :CON_RULE_0003,  ,Error Description : TAC in the IMEI/MEID is not a approved TAC from TRC ";
                         bw.write(fileString);
                         bw.newLine();
                    }
                    break;
                    case "Block": {
                    }
                    break;
                    case "Report": {
                         PreparedStatement statementN = null;
                         try {
                              DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;   //
                              Calendar cal = Calendar.getInstance();
                              cal.add(Calendar.DATE, 0);
                              String date = dateFormat1.format(cal.getTime());
                              String user_id_qury = "    (select user_id from " + args[2].trim().toLowerCase() + "_mgmt where txn_id = '" + args[14] + "' ) ";
                              String pending_tac_approved_db = " insert into pending_tac_approved_db (created_on,feature_name ,  tac , txn_id, user_id   ,modified_on   ) "
                                      + "   values  (current_timestamp  , '" + args[2] + "'  ,    '" + args[3].substring(0, 8) + "'  , '" + args[14] + "' , " + user_id_qury + "  ,  current_timestamp  ) ";
                              logger.debug("Qury is " + pending_tac_approved_db);
                              statementN = conn.prepareStatement(pending_tac_approved_db);
                              statementN.executeUpdate();
                              statementN.close();
                         } catch (Exception e) {
//                              logger.debug("Error" + e);
                         } finally {
                              statementN.close();
                         }

                    }
                    break;
                    case "SYS_REG": {
                    }
                    break;
                    case "USER_REG": {
                    }
                    break;
                    default:
                         logger.debug(" The Action " + args[13] + "  is Not Defined  ");

               }

               return "Success";
          } catch (Exception e) {
//               logger.debug(" Error " + e);
               return "Failure";
          }
     }

}
