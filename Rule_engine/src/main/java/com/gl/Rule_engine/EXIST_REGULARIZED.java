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
class EXIST_REGULARIZED {

    static final Logger logger = Logger.getLogger(EXIST_REGULARIZED.class);

    static String executeRule(String[] args, Connection conn) {
        String str = "";
        logger.debug("EXIST_REGULARIZED executeRule ");
          Statement stmt2 = null;
          ResultSet result1 = null;
        {
            try {
                  stmt2 = conn.createStatement();
                  result1 = stmt2.executeQuery("select action, msisdn from device_usage_db  where imei_esn_meid='" + args[3] + "' ");
                logger.debug("select action, msisdn from device_usage_db  where imei_esn_meid='" + args[3] + "' ");
                String actn = "";
                String msdn = "";
                try {
                    while (result1.next()) {
                        actn = result1.getString("action");
                        msdn = result1.getString("msisdn");
                    }
                } catch (Exception e) {
                    logger.error("" + e);
                }
                logger.debug("actn " + actn + "... msdn.." + msdn);
                if (actn.equalsIgnoreCase("2") || actn.equalsIgnoreCase("0")) {
                    if (msdn.equalsIgnoreCase(args[12])) {
                        str = "No";
//                        logger.debug("No");
                    } else {
                        str = chckDubplicateDb(args, conn);
                    }
                } else {
                    str = chckDubplicateDb(args, conn);
                } 
            } catch (Exception e) {
                logger.error("Erroer" + e);
            }
            finally {
               try {
                    result1.close();
                    stmt2.close();
               } catch (Exception ex) {
                    logger.error("Error" + ex);
               }
          }
        }
        return str;
    }

    public static String chckDubplicateDb(String[] args, Connection conn) {
        logger.debug("Chcking for Dupblicate");
        String res = "";

        try {

            Statement stmt3 = conn.createStatement();
            ResultSet result3 = stmt3.executeQuery("select action from device_duplicate_db  where imei_esn_meid='" + args[3] + "'    and msisdn = '" + args[12] + "' ");
            logger.debug("select action from device_duplicate_db  where imei_esn_meid='" + args[3] + "'    and msisdn = '" + args[12] + "' ");
            String actn3 = "";
            try {
                while (result3.next()) {
                    actn3 = result3.getString(1);
                }
            } catch (Exception e) {
                logger.debug("");
            }
            if (actn3.equalsIgnoreCase("2") || actn3.equalsIgnoreCase("0")) {

                res = "No";

            } else {
                res = "Yes";
            }
            result3.close();
            stmt3.close();
        } catch (Exception e) {
            logger.error("Error .." + e);
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

                    String fileString = args[15] + " ,Error Code :CON_RULE_0022, Error Description : IMEI/ESN/MEID is already present in the system  ";

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
