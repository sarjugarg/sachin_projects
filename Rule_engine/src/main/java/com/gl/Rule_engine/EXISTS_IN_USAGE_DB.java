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
class EXISTS_IN_USAGE_DB {

    static final Logger logger = Logger.getLogger(EXISTS_IN_USAGE_DB.class);

    static String executeRule(String[] args, Connection conn) {

        String res = "";
          Statement stmt2 =null;
          ResultSet result1 = null;
        try {
              stmt2 = conn.createStatement();
              result1 = stmt2.executeQuery("select count(imei_esn_meid) as cnt  from device_usage_db  where imei_esn_meid='" + args[3] + "' ");
            logger.debug("select count(imei_esn_meid) as cnt  from device_usage_db  where imei_esn_meid='" + args[3] + "' ");
            int res1 = 0;
            try {
                while (result1.next()) {
                    res1 = result1.getInt(1);
                }
            } catch (Exception e) {
                logger.debug("" + e);
            }
            if (res1 != 0) {
                logger.debug("Yes");
                res = "Yes";

            } else {
                logger.debug("No");
                res = "No";
            }
            result1.close();
            stmt2.close();
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

    static String executeAction(String[] args, Connection conn,  BufferedWriter bw) {
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
                    String fileString = args[15] + " ,Error Code :CON_RULE_0004,  Error Description : IMEI/ESN/MEID is already in use in the network ";
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
//        String res = "Success";
//        try{
//        logger.debug("EXISTS_IN_USAGE_DB executeAction");
////        if (args[2].equalsIgnoreCase("CDR")) {
////            logger.debug("Error Not for CDR");
////        } else 
//        {
//              Map<String, String> map = new HashMap<String, String>();
//            map.put("fileName", args[14]);
//            String fileString = args[15] + ", Error Code:CON_RULE_0001,TAC in the IMEI/MEID is not a approved TAC from TRC" ;
//            map.put("fileString", fileString);
//               bw.write(fileString);
 
//        }
//        }catch(Exception e){
//        res = "Error";}
//        return res ;
//    }
}
//
//EXISTS_IN_USAGE_DB - Other than CDR - Expected O/P no.
//Execute Rule
//select count(*) as cnt  from device_usage_db where imei ='XXXXXXX';
//if cnt >0 
//then
//	return yes
//else 
//	return no
//
//Execute Action
//	create error file.
