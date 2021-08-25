/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine;

import java.sql.Connection;
import java.io.BufferedWriter;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
class TAC_FORMAT {

    static final Logger logger = Logger.getLogger(TAC_FORMAT.class);

    static String executeRule(String[] args, Connection conn) {
        String res = null;
        logger.debug("TAC_FORMAT executeRule ....." + args[3]);
        try {
            if ((args[3].length() == 8 && (args[3].matches("^[-\\w.]+")))) {      // args[10].equalsIgnoreCase("GSM") &&
                res = "Yes";
                logger.debug("TAC_FORMAT   ok ");
            } else {
                res = "No";
                logger.debug("TAC_FORMAT   NOT OK ");
            }
        } catch (Exception e) {
            logger.debug("Error.." + e);
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
                logger.debug("Action is Reject");

                String fileString = args[15] + " , Error Code :CON_RULE_0031 , Error Description :  TAC is not as per specifications  ";

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

//                try {
//                     
//
//                    String actn = "";
//
//                    if (args[11].equalsIgnoreCase("grace")) {
//                        actn = "0";
//                    } else {
//                        actn = "1";
//                    }
//                    logger.debug("Action ..." + actn);
//                    Connection  
//                    boolean isOracle = conn.toString().contains("oracle");
//                    String dateFunction = Util.defaultDate(isOracle);
//                    String qry1 = " insert into device_invalid_db (imei ,   failedrule, failedruleid, action, failed_rule_date  ) values  (  '" + args[3] + "'  ,  'IMEI_LENGTH'  , ( select id from rule_engine where name =   'IMEI_LENGTH' ), '" + actn + "' , " + dateFunction + " ) ";
//                    logger.debug("" + qry1);
//                    PreparedStatement statement1 = conn.prepareStatement(qry1);
//                    int rowsInserted11 = statement1.executeUpdate();
//                    if (rowsInserted11 > 0) {
//                        logger.debug("inserted into device_invalid_db tabl");
//                    }
//                     
//                } catch (Exception e) {
//                    logger.debug("Error e " + e);
//                }
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
