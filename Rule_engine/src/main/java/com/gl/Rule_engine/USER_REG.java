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
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;
 
class USER_REG {

     static final Logger logger = Logger.getLogger(USER_REG.class);

     static String executeRule(String[] args, Connection conn) {
//        logger.debug(" USER_REG executeRule ");
          String res = "";
          ResultSet result1 = null;
          Statement stmt2 = null;

          try {

               stmt2 = conn.createStatement();
               String qury = " select action from device_usage_db  where  imei ='" + args[3] + "'   union  select action from  device_duplicate_db  where  imei =   '" + args[3] + "'  ";
               result1 = stmt2.executeQuery(qury);
               logger.debug(qury);
               Set<String> hash_Set = new HashSet<String>();
               try {
                    while (result1.next()) {
                         hash_Set.add(result1.getString(1));
                    }
               } catch (Exception e) {
                    logger.debug("Error " + e);
               }
               if (hash_Set.contains("USER_REG")) {
                    logger.debug("Yes");
                    res = "Yes";
               } else {
                    logger.debug("No");
                    res = "no";
               }
               result1.close();
               stmt2.close();

          } catch (Exception e) {
               logger.error("Error:" + e);
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
                         String fileString = args[15] + " , Error Code :CON_RULE_0032 , Error Description : IMEI/ESN/MEID  is User Registered   ";
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
                         // set action as USER_REG
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

//    static String executeAction(String[] args, Connection conn , BufferedWriter bw) {
//        logger.debug("Skip the action");
//        return "Skip";
//
////              Map<String, String> map = new HashMap<String, String>();
////            map.put("fileName", args[14]);
////           String fileString =args[15]  + " ,Error Occured :IMEI/ESN/MEID is already present in the system ";
////  map.put("fileString", fileString);
////               bw.write(fileString);
//    }
}


  //select count(regularize_device_db.nid) from regularize_device_db inner join end_userdb on end_userdb.nid=regularize_device_db.nid where (first_imei='1234567890123456' or second_imei='1234567890123456' or third_imei='1234567890123456' or fourth_imei='1234567890123456') and nationality<>'Cambodian';
