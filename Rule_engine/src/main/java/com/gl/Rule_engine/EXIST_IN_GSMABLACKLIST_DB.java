/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine;

import org.apache.log4j.Logger;

import com.gl.Rule_engine.BlackList.EncriptonBlacklistService;
import java.sql.Connection;
import java.io.BufferedWriter;

/**
 *
 * @author user
 */
public class EXIST_IN_GSMABLACKLIST_DB {
    static final Logger logger = Logger.getLogger(EXIST_IN_GSMABLACKLIST_DB.class);
    static String executeRule(String[] args, Connection conn) {
        String rslt = EncriptonBlacklistService.startBlacklistApp(args[3], conn);
        return rslt;
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
                String fileString = args[15] + " , Error Code :CON_RULE_0017, Error Description :IMEI/ESN/MEID  is Blacklisted By Gsma ";
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
            case "NAN": {
                logger.debug("Action is NAN");
                String fileString = args[15] + " , Error Code :CON_RULE_0023, Error Description :System Won't able to establish connection to Gsma Blacklist server. Please try again after Some Time.";
                 bw.write(fileString);
                bw.newLine();
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

//create table t1 (
//    c1 NUMBER GENERATED ALWAYS as IDENTITY(START with 1 INCREMENT by 1),
//    c2 VARCHAR2(10)
//    );
