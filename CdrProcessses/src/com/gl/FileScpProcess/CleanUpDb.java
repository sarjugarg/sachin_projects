/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author maverick
 */
public class CleanUpDb {
     
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
               query = " select * from cdr_process_status where  process_name = 'scriptV2' and SERVER_ID = '" + args[0].trim() + "' and  OPERATOR = '" + args[1].trim() + "'   order by id desc fetch next 1 rows only  ";
               logger.info("   Query  " + query);
               stmt2 = conn.createStatement();
               rs2 = stmt2.executeQuery(query);
               String CREATED_ON = "";
               String OPERATOR = "";
               String status = "";
               
               SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
               Date date = new Date();
               String dat = formatter.format(date).toUpperCase();
               System.out.println(formatter.format(date).toUpperCase());
               while (rs2.next()) {
                    CREATED_ON = rs2.getString("CREATED_ON");
                    OPERATOR = rs2.getString("OPERATOR");
                    status = rs2.getString("status");
               }
               
               if (status.equalsIgnoreCase("Start")) {
                    logger.info(CREATED_ON + " **  " + OPERATOR + " ** " + status + " ** " + dat);
                    deleteCdrFileDetails(conn, dat);
                    deleteCdrPreProcessing(conn, dat);
               }
               logger.debug("process ended");
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
     
     static void deleteCdrFileDetails(Connection conn, String dat) {
          Statement stmt = null;
          
          String query = " delete from cdr_file_details_db  where  created_on >= TO_DATE('"+dat+"', 'DD-MM-YY')      ";
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
     
     static void deleteCdrPreProcessing(Connection conn, String dat) {
          Statement stmt = null;
          
//          String query = " delete from cdr_pre_processing_report  where created_on like '" + dat + "%'";
          String query = " delete from cdr_pre_processing_report  where created_on >= TO_DATE('"+dat+"', 'DD-MM-YY')  ";
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
