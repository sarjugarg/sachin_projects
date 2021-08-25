/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine;

import java.sql.Connection;
import java.io.BufferedWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
class DUPLICATE_USAGE_CHECK {

     static final Logger logger = Logger.getLogger(DUPLICATE_USAGE_CHECK.class);

     static String executeRule(String[] args, Connection conn) {
          String res = null;
          Statement stmt2 = null;
          ResultSet result1 = null;
          Statement stmt3 = null;
          ResultSet result3 = null;
          Statement stmt4 = null;
          ResultSet result4 = null;
          try {
                    stmt2 = conn.createStatement();
                    result1 = stmt2.executeQuery("select count( imei) as c1  from device_usage_db where imei='" + args[3] + "' ");
                    logger.debug(" select count( msisdn) as c1  from device_usage_db where imei='" + args[3] + "'");
                    int res1 = 0;
                    try {
                         while (result1.next()) {
                              res1 = result1.getInt(1);
                         }
                    } catch (Exception e) {
                         logger.debug("eror " + e);
                    }
                    result1.close();
                    stmt2.close();
                    logger.debug("device_usage_db count:" + res1);
                    stmt3 = conn.createStatement();
                    logger.debug(" select count(msisdn) as c1  from device_duplicate_db where imei='" + args[3] + "' ");
                    result3 = stmt3.executeQuery("  select count(imei) as c1  from device_duplicate_db where imei='" + args[3] + "' ");
                    int res3 = 0;
                    try {
                         while (result3.next()) {
                              res3 = result3.getInt(1);
                         }
                    } catch (Exception e) {
                         logger.debug("errro2 " + e);
                    }
                    result3.close();
                    stmt3.close();
                    logger.debug("device_duplicate_db count:" + res3);
                    int ttl = res1 + res3;
                    logger.debug("Total  count: " + ttl);
                    stmt4 = conn.createStatement();
                    result4 = stmt4.executeQuery("Select  value from system_configuration_db where tag='DUPLICATE_IMEI_USAGE_COUNT'");
                    int res4 = 0;
                    try {
                         while (result4.next()) {
                              res4 = result4.getInt(1);
                         }
                    } catch (Exception e) {
                         logger.error("" + e);
                    }
                    logger.debug("Select  value from system_configuration_db where tag='DUPLICATE_IMEI_USAGE_COUNT'  .... " + res4);
                    if (res4 <= ttl) {
                         res = "Yes";
                    } else {
                         res = "No";
                    }
                    result4.close();
                    stmt4.close();
//                    result1.close();
//                    result3.close();
//                    result4.close();
//                    stmt2.close();
//                    stmt3.close();
//                    stmt4.close();

               
          } catch (Exception e) {
               logger.error(" Error " + e);
          } finally {
               try {
                    result1.close();
                    result3.close();
                    result4.close();
                    stmt2.close();
                    stmt3.close();
                    stmt4.close();
               } catch (SQLException ex) {
                    logger.error(ex);
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
                         String fileString = args[15] + " ,  Error Code :CON_RULE_0034 , Error Description : IMEI/ESN/MEID is already present in the system  ";
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
               logger.error(" Error " + e);
               return "Failure";
          }
     }
}

//     regularize the user (USER_REG).
//
//         
//       
//        if (db_type.equals("oracle")) {
//            className = classNameO;
//            jdbcUrl = jdbcUrlO;
//        } else {
//            className = classNameM;
//            jdbcUrl = jdbcUrlM;
//        }
//        try {
//             
////            if (args[11].equalsIgnoreCase("grace")) 
//            {
//                Connection  
//                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");   //
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, 0);
//                String date = dateFormat1.format(cal.getTime());
//                String historyIns = " insert into device_usage_db (imei, created_on ,action    ) values  (  '" + args[3] + "'    ,  'USER_REG' ) ";
//                PreparedStatement statementN = conn.prepareStatement(historyIns);
//                int rowsInserted1 = statementN.executeUpdate();
//                if (rowsInserted1 > 0) {
//                    logger.debug("inserted into device_usage_db");
//                }
//                 
//            }
//            
//            
//            {
//              Map<String, String> map = new HashMap<String, String>();
//            map.put("fileName", args[14]);
//          String fileString =args[15]  + " ,Error Occured :IMEI/ESN/MEID is already present in the system ";
//          map.put("fileString", fileString);
//               bw.write(fileString);
//        }
//            
//            
//            
//        } catch (Exception e) {
//            logger.debug("Errror" + e);
//        }
//
//
// Field                      | Type         | Null | Key | Default | Extra |
//+----------------------------+--------------+------+-----+---------+-------+
//| imei                       | bigint(20)   | NO   | PRI | NULL    |       |
//| created_on                 | datetime     | YES  |     | NULL    |       |
//| duplicate_count            | int(11)      | NO   |     | NULL    |       |
//| foregin_rule               | bit(1)       | NO   |     | NULL    |       |
//| global_blacklist           | bit(1)       | NO   |     | NULL    |       |
//| lastp_policy_breached      | int(11)      | NO   |     | NULL    |       |
//| lastp_policy_breached_date | datetime     | YES  |     | NULL    |       |
//| mobile_operator            | varchar(255) | YES  |     | NULL    |       |
//| pending                    | bit(1)       | NO   |     | NULL    |       |
//| period                     | varchar(30)  | YES  |     | NULL    |       |
//| remarks                    | varchar(255) | YES  |     | NULL    |       |
//| tax_paid                   | bit(1)       | NO   |     | NULL    |       |
//| valid_import               | bit(1)       | NO   |     | NULL    |       |
//| action                     | varchar(255) | YES  |     | NULL    |       |
//| failed_rule_id             | varchar(255) | YES  |     | NULL    |       |
//| failed_rule_name           | varchar(255) | YES  |     | NULL    |       |
//| imei_status                | int(11)      | YES  |     | NULL    |       |
//| imsi                       | bigint(20)   | YES  |     | NULL    |       |
//| mobile_operator_id         | bigint(20)   | YES  |     | NULL    |       |
//| msisdn                     | bigint(20)   | YES  |     | NULL    |       |
//| updated_on                 | datetime     | YES  |     | NULL    |       |
//| record_type                | smallint(6)  | YES  |     | NULL    |       |
//| system_type                | varchar(100) | YES  |     | NULL    |       |
//| create_filename            | varchar(50)  | YES  |     | NULL    |       |
//| update_filename            | varchar(50)  | YES  |     | NULL    |       |
//| failed_rule_date           | datetime     | YES  |     | NULL    |       |
//| tac                        | varchar(10)  | NO   |     | NULL    |       |
//+----------------------------+--------------+------+-----+---------+---
