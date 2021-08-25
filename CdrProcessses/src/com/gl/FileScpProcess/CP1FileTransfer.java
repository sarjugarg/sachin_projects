/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author maverick
 */
public class CP1FileTransfer {

    public static PropertyReader propertyReader = new PropertyReader();
    static Logger log = Logger.getLogger(CP1FileTransfer.class);

    public void cp1(String operator_param, String source_param) {

        String sourceDirectory = "";
        String targetDirectory = "";
        String cdrRecdServer = "";
        String extension = "";
        try {
            sourceDirectory = propertyReader.getPropValue("source_path").trim() + "/" + operator_param + "/" + source_param + "/";
            targetDirectory = propertyReader.getPropValue("target_path").trim() + "/" + operator_param + "/" + source_param + "/";
            cdrRecdServer = propertyReader.getPropValue("cdrRecdServer").trim();
            extension = propertyReader.getPropValue("EXTENSION").trim();
            log.info(": " + extension);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CP1FileTransfer.class.getName()).log(Level.SEVERE, null, ex);
        }

        String start_timeStamp = null;
        long start_time = 0;
        int count = 0;
        File[] files = new File(sourceDirectory).listFiles();
        log.info("Operator " + operator_param + " >> source " + source_param + " ready to move Number of files : " + files.length);
        Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
        for (File file : files) {
            if (file.isFile() && !file.getName().endsWith(extension)) {
                start_time = System.currentTimeMillis();
                long fileSize = file.length();
                log.info("File Size " + fileSize);
                try {
                  long val =    Files.size(new File(sourceDirectory +  file.getName() ).toPath());
                      log.info("File Size value " + val);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(CP1FileTransfer.class.getName()).log(Level.SEVERE, null, ex);
                }

                start_timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                String destinationPath = targetDirectory + file.getName();
                log.info("moving file to destinationPath:" + destinationPath);
                Path temp;
                try {
                    temp = Files.move(Paths.get(sourceDirectory + file.getName()), Paths.get(destinationPath));
                    log.info("File moved successfully to destination path " + destinationPath);
                    String fileDate = file.getName().substring(file.getName().indexOf("2021"), file.getName().indexOf("2021") + 8);
                    count = count + 1;
                    saveDatainDb(conn, source_param.trim(), operator_param, file.getName(), cdrRecdServer, fileDate, fileSize);
                } catch (IOException e) {
                    log.info("Failed to move the file due to reason : " + e.toString());
                }
            }
        }
        try {
            conn.close();
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(CP1FileTransfer.class.getName()).log(Level.SEVERE, null, ex);
        }
        long end_time = System.currentTimeMillis();
        String end_timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        log.info("MOVE PROCESS ::  start time : " + start_timeStamp + " >> end time : " + end_timeStamp + " >> file transferred : " + count + " >> total time taken :" + ((end_time - start_time) / 1000) / 60 + "minutes and " + ((end_time - start_time) / 1000) % 60 + " seconds >>" + " operator :" + operator_param + " >> source :" + source_param + " >> file transferred per second");

    }

    static void saveDatainDb(Connection conn, String source, String operator, String name, String cdrRecdServer, String fileDate, long filesize) {
        try {
            String query = "insert into cdr_file_records_db ( CREATED_ON ,SOURCE ,OPERATOR, FILE_NAME , CDR_RECD_SERVER , STATUS_SIG1 ,STATUS_SIG2  , FILE_DATE , file_size, record_size) "
                    + " values( current_timestamp , '" + source + "' , '" + operator + "' ,  '" + name + "' ,   '" + cdrRecdServer + "' , 'INIT' , 'INIT'   ,   '" + fileDate + "' ,    '" + filesize + "' , (  " + filesize + " / (select  avg_record_size from operator_source_avg_record_size where operator = '" + operator + "' and source  =   '" + source + "'  )         )    )";
            log.info(query);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (Exception e) {
            log.info("Failed  " + e);
            e.printStackTrace();
        }
    }

}

//    public static void main(String[] args) {
//
//        String source_directory = "";
//        String target_directory = "";
//        try {
//            source_directory = propertyReader.getPropValue("source_directory").trim();
//            target_directory = propertyReader.getPropValue("target_directory").trim();
//        } catch (IOException ex) {
//            java.util.logging.Logger.getLogger(CP1FileTransfer.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        log.info("source_directory : " + source_directory);
//        log.info("target_directory : " + target_directory);
//
//        String[] arr = source_directory.split("/");
//        String source = arr[arr.length - 1];
//        String operator = arr[arr.length - 2];
//        File[] files = new File(source_directory).listFiles();
//        log.info("Number of files : " + files.length);
//        for (File file : files) {
////            CDRFileRecords entity = new CDRFileRecords();
//            if (file.isFile()) {
//                Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
//                String destinationPath = target_directory + file.getName();
//                log.info("moving file to destinationPath:" + destinationPath);
//                Path temp;
//                try {
//                    temp = Files.move(Paths.get(source_directory + file.getName()), Paths.get(destinationPath));
//                    log.info("File moved successfully to destination path " + destinationPath);
//                    saveDatainDb(conn, source, operator, file.getName());
//                } catch (IOException e) {
//                    log.info("Failed to move the file");
//                    e.printStackTrace();
//                }
//
//            }
//        }
//    }
