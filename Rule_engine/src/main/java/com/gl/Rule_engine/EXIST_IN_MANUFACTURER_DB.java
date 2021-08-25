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
class EXIST_IN_MANUFACTURER_DB {

     static final Logger logger = Logger.getLogger(EXIST_IN_MANUFACTURER_DB.class);

     static String executeRule(String[] args, Connection conn) {
          String res = "No";
          logger.debug("EXIST_IN_MANUFACTURER_DB executeRule");
//         
          Statement stmt2 = null;
          ResultSet result1 = null;
          try {
               stmt2 = conn.createStatement();
               {
                    result1 = stmt2.executeQuery("select count(imei_esn_meid) from device_manufacturer_db  where imei_esn_meid='" + args[3] + "' ");
                    logger.debug("select count(imei_esn_meid) from device_manufacturer_db  where imei_esn_meid='" + args[3] + "' ");

                    String res2 = "0";
                    try {
                         while (result1.next()) {
                              res2 = result1.getString(1);
                         }
                    } catch (Exception e) {
                         logger.error("" + e);
                    }
                    if (!res2.equals("0")) {
                         res = "Yes";
                    } else {
                         res = "No";
                    }
               }

          } catch (Exception e) {
               logger.error("error.." + e);
          } finally {
               try {
                    result1.close();
                    stmt2.close();
               } catch (Exception e) {
                    logger.error("Error  ::" + e);

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

                         String fileString = args[15] + " , Error Code :CON_RULE_0010, Error Description : IMEI/ESN/MEID is already present in the system  ";
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

//    static String executeAction(String[] args, Connection conn , BufferedWriter bw) {
//        String rrst = "Success";
//        try {
//             
//           
//            if (db_type.equalsIgnoreCase("oracle")) {
//                className = classNameO;
//                jdbcUrl = jdbcUrlO;
//            } else {
//                className = classNameM;
//                jdbcUrl = jdbcUrlM;
//            }
//
//             
////            if (args[11].equalsIgnoreCase("grace")) {
////                Connection  
////                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");   //
////                Calendar cal = Calendar.getInstance();
////                cal.add(Calendar.DATE, 0);
////                String date = dateFormat1.format(cal.getTime());
////                
////                // for device_db insertion .. already done in custom db
////
////                String historyIns = " insert into device_manufacturer_db (created_on,device_id ,device_type  , device_status , imei_esn_meid   ) values   ( '" + date + "'  ,'" + args[4] + "' ,  '' , '' ,  '" + args[3] + "'    ) ";
////                PreparedStatement statementN = conn.prepareStatement(historyIns);
////                int rowsInserted1 = statementN.executeUpdate();
////                if (rowsInserted1 > 0) {
////                    logger.debug("inserted into device_manufacturer_db ");
////                }
////  
////            } else 
//            {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("fileName", args[14]);
//                String fileString = args[15] + " ,Error Occured : :  IMEI/ESN/MEID is already present in the system ";
//                map.put("fileString", fileString);
//
//        } catch (Exception e) {
//            rrst = "Error";
//        }
//        return rrst;
//    }
}
//
//
//
//EXIST_IN_MANUFACTURER_DB
//Query : select manufacturer_txn_id from device_db where imei='XXXXXXXXX';
//if(non null)
//	Add entry in error file and move to the next record. and insert in device_info_fail_rule_db 
//else
//	move to the next rule.
