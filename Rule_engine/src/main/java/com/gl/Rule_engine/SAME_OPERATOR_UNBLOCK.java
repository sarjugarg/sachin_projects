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

/**
 *
 * @author user
 */
class SAME_OPERATOR_UNBLOCK {

    static final Logger logger = Logger.getLogger(SYS_REG.class);

    static String executeRule(String[] args, Connection conn) {
        String res = "";
        
          Statement stmt2 = null;
          ResultSet result1 = null;
        try {
            String opr1 = null;
            String opr2 = null;
              stmt2 = conn.createStatement();
            String qury = " select OPERATOR_TYPE_ID from stolenand_recovery_mgmt where  TXN_ID = (select TXN_ID  from  device_operator_db where IMEI_ESN_MEID = '" + args[3] + "' )";
              result1 = stmt2.executeQuery(qury);
            logger.debug(qury);
            try {
                while (result1.next()) {
                    opr1 = result1.getString(1);
                }
            } catch (Exception e) {
                logger.error("opr1 " + e);
            }

            qury = " select OPERATOR_TYPE_ID from stolenand_recovery_mgmt where  TXN_ID =  '" + args[14] + "' ";
            result1 = stmt2.executeQuery(qury);
            logger.debug(qury);
            try {
                while (result1.next()) {
                    opr2 = result1.getString(1);
                }
            } catch (Exception e) {
                logger.error("opr2 " + e);
            }

            if (opr1.equals(opr2)) {
                res = "Yes";
            } else {
                res = "No";
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

                    String fileString = args[15] + ",Error Code :CON_RULE_0029 , Error Description : Current Operator don't have Permission to UnBlock this  IMEI/ESN/MEID   ";
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
