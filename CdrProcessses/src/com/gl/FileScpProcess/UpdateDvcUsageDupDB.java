/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author maverick
 */
public class UpdateDvcUsageDupDB {

    static Logger logger = Logger.getLogger(CleanUpDb.class);
    static StackTraceElement l = new Exception().getStackTrace()[0];
    public static PropertyReader propertyReader;

    public static void main(String[] args) {
        propertyReader = new PropertyReader();
        Connection conn = null;
        conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
        String query = null;
        ResultSet rs2 = null;
        Statement stmt2 = null;

        try {
            query = "select imei , msisdn from device_duplicate_db  where 	UPDATE_IMEI_ARRIVAL_TIME is null  ";
            logger.info("   Query  " + query);
            stmt2 = conn.createStatement();
            rs2 = stmt2.executeQuery(query);
            long j = 0;
            while (rs2.next()) {
                updateDb(conn, rs2.getString("imei"), rs2.getString("msisdn"));
                logger.info(j++);
            }
        } catch (Exception e) {
            logger.error("" + e);
        } finally {
            try {
                rs2.close();
                stmt2.close();
                conn.commit();
            } catch (Exception e) {
                logger.error("" + l.getClassName() + "/" + l.getMethodName() + ":" + l.getLineNumber() + e);
            }
        }

    }

    static void updateDb(Connection conn, String imei, String msisdn) {
        Statement stmt = null;
        String query = "  Update device_duplicate_db set UPDATE_RAW_CDR_FILE_NAME=RAW_CDR_FILE_NAME,UPDATE_IMEI_ARRIVAL_TIME =IMEI_ARRIVAL_TIME where imei = '" + imei + "' and msisdn = '" + msisdn + "'    ";
        logger.info(" qury is " + query);
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (Exception e) {
            logger.error("" + l.getClassName() + "/" + l.getMethodName() + ":" + l.getLineNumber() + e);
        } finally {
            try {
                stmt.close();
                conn.commit();
            } catch (Exception e) {
                logger.error("" + l.getClassName() + "/" + l.getMethodName() + ":" + l.getLineNumber() + e);
            }
        }

    }

}
