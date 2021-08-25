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
class EXIST_IN_TAX_PAID_DB {

     static final Logger logger = Logger.getLogger(EXIST_IN_TAX_PAID_DB.class);

     static String executeRule(String[] args, Connection conn) {
          logger.debug("EXIST_IN_TAX_PAID_DB executeRule");
          String res = "No";

          Statement stmt2 = null;
          ResultSet result1 = null;
          try {

               stmt2 = conn.createStatement();
               result1 = stmt2.executeQuery("select count(imei_esn_meid) as cnt  from device_custom_db  where imei_esn_meid='" + args[3] + "'  ");
               logger.debug("select count(imei_esn_meid) as cnt  from device_custom_db  where imei_esn_meid='" + args[3] + "'  ");
               int res1 = 0;
               try {
                    while (result1.next()) {
                         res1 = result1.getInt(1);
                    }
               } catch (Exception e) {
                    logger.debug("Error" + e);
               }
               if (res1 != 0) {
                    res = "Yes";
               } else {
                    res = "No";
               }
               result1.close();
               stmt2.close();

          } catch (Exception e) {
               logger.error("" + e);
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

                         String fileString = args[15] + " ,Error Code :CON_RULE_0016, Error Description : IMEI/ESN/MEID is already present in the system  ";

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
//        logger.debug("exist_in_tax_paid executeAction ");
//        String res = "Success";
//        try {
//            if (args[2].equalsIgnoreCase("CDR")) {
//                 
//               
//                if (db_type.equalsIgnoreCase("oracle")) {
//                    className = classNameO;
//                    jdbcUrl = jdbcUrlO;
//                } else {
//                    className = classNameM;
//                    jdbcUrl = jdbcUrlM;
//                }
//
//                 
//                Connection  
//                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");   //
//                Calendar cal = Calendar.getInstance();
//                cal.add(Calendar.DATE, 0);
//                String date = dateFormat1.format(cal.getTime());
//
//                String historyIns = " update device_usage_db set failed_rule_date = '" + date + "'   ,failed_rule_id = (select id from rule_engine where name = 'EXIST_IN_TAX_PAID_DB'),  failed_rule_name ='EXIST_IN_TAX_PAID_DB' , action = 'USER' ";
//                logger.debug(" is " + historyIns);
//                PreparedStatement statementN = conn.prepareStatement(historyIns);
//                int rowsInserted1 = statementN.executeUpdate();
//                if (rowsInserted1 > 0) {
//                    logger.debug("device_usage_db updated");
//                }
//                 
//            } else {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("fileName", args[14]);
//                String fileString =args[15]  + " ,Error Occured :IMEI/ESN/MEID is already present in the system ";
//                map.put("fileString", fileString);
//                   bw.write(fileString);
//            }
//
//        } catch (Exception e) {
//            logger.debug("" + e);
//            res = "Error";
//        }
//        return res;
//
//    }
}

//else {
//                Statement stmt3 = conn.createStatement();
//                ResultSet result3 = stmt3.executeQuery("select count(*) as cnt  from device_db  where imei_esn_meid='" + args[3] + "' "
//                        + "  and manufacturer_device_status = 'approved' ");
//
//                logger.debug(" Query ... select count(*) as cnt  from device_db  where imei_esn_meid='" + args[3] + "' "
//                        + "  and manufacturer_device_status = 'approved' ");
//
//                int res3 = 0;
//                try {
//                    while (result3.next()) {
//                        res3 = result3.getInt(1);
//                    }
//                } catch (Exception e) {
//                    logger.debug("Error1 " + e);
//                }
//                if (res3 != 0) {
//                    logger.debug("Yes");
//                    res = "Yes";
//                } else {
//                    Statement stmt4 = conn.createStatement();
//                    ResultSet result4 = stmt4.executeQuery(" select  tax_paid_status from  regularize_device_db  where first_imei= '" + args[3] + "' or second_imei  = '" + args[3] + "' or fourth_imei= '" + args[3] + "' or third_imei = '" + args[3] + "'  ");
//
//                    logger.debug("Qry 3 ...select  tax_paid_status from  regularize_device_db  where first_imei= '" + args[3] + "' or second_imei  = '" + args[3] + "' or fourth_imei= '" + args[3] + "' or third_imei = '" + args[3] + "'  ");
//                    int res4 = 9;
//                    try {
//                        while (result4.next()) {
//                            res4 = result4.getInt(1);
//                        }
//                    } catch (Exception e) {
//                        logger.debug("");
//                    }
//                    if (res4 == 0) {
//                        logger.debug("Yes");
//                        res = "Yes";
//
//                    } else {
//                        logger.debug("No");
//                        res = "No";
//                    }
//
//                }
//                 
//            }   whoch state in regularize_device_db update
