/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine;

//import com.example.BasicApplication;
 
import org.apache.log4j.Logger;
import gsmaTac.BasicApplication;
import java.sql.Connection;
import java.io.BufferedWriter;

/**
 *
 * @author user
 */
class EXISTS_IN_GSMA_TAC_DB {     // EXISTS_IN_GSMA_TAC_DB used in TYPE APPROVE TOO

    static final Logger logger = Logger.getLogger(EXISTS_IN_GSMA_TAC_DB.class);

    static String executeRule(String[] args, Connection conn) {
        String res = "No";
        logger.debug("EXISTS_IN_GSMA_TAC_DB executeRule");
        try {
            String tac = "";
            if (args[2].equalsIgnoreCase("CDR")) {
                if (args[10].equalsIgnoreCase("GSM")) {
                    tac = args[3].trim().substring(0, 8);
                }
            } else {
                tac = args[3].trim().substring(0, 8);
            }
            logger.debug("tac val .." + tac);
            if (tac.equalsIgnoreCase("")) {
                res = "No";
            } else {
                logger.debug("GSMA started");
                BasicApplication w = new BasicApplication();
                res = w.gsmaApplication(tac, conn);
            }
        } catch (Exception e) {
            logger.debug("errror " + e);
        }
        return res;
    }

    static String executeAction(String[] args, Connection conn,  BufferedWriter bw) {
      try{  switch (args[13]) {
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
                String fileString = args[15] + " , Error Code :CON_RULE_0003 , Error Description :TAC in IMEI is not approved TAC from GSMA  ";
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
            case "NAN": {
                logger.debug("Action is NAN");
                String fileString = args[15] + " , Error Code :CON_RULE_0002, Error Description :Could not connect to GSMA server.Try after Some Time.  ";
                 bw.write(fileString);
                bw.newLine();
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

//        
//        logger.debug("EXISTS_IN_GSMA_TAC_DB executeAction ");
//        String res = "Success";
//        try {
//            if (args[2].equalsIgnoreCase("CDR")) {
//             
//         
//                
//            }
//            if (args[2].equalsIgnoreCase("consignment")) { 
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("fileName", args[14]);
//                String fileString = args[15] + " , ";
//                map.put("fileString", fileString);
//                   bw.write(fileString);
                
//            }
//
//        } catch (Exception e) {
//            logger.debug("" + e);
//            res = "Error";
//        }
//        return res;
//    }
//
//}
//Execute Action
//if feature is CDR
//	
//else
//	create error file.
