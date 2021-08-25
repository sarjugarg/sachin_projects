/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import org.apache.log4j.Logger;

public class ScpJavaProcess {

    static Logger logger = Logger.getLogger(ScpJavaProcess.class);
    static StackTraceElement l = new Exception().getStackTrace()[0];
    public static PropertyReader propertyReader;

    public static void main(String[] args) {
        propertyReader = new PropertyReader();
        Connection conn = null;
        ScpJavaProcess scpProcess = new ScpJavaProcess();
        conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
        String query = null;
        String inFileName = args[0];

        inFileName = inFileName.substring(0, inFileName.length() - 5);
        logger.info("File Name " + inFileName);
        ResultSet rs2 = null;
        Statement stmt2 = null;
        int statusVal = 1;
        try {
            query = " select status from CDR_FILE_DETAILS_DB where   file_name like   '" + inFileName + "%'  and status = 'Init'   ";
            logger.info(" Status Query  " + query);
            stmt2 = conn.createStatement();
            rs2 = stmt2.executeQuery(query);
            while (rs2.next()) {
                statusVal = 0;
            }
            logger.info("Status VAlue " + statusVal);

        } catch (Exception e) {
            logger.error("" + e);
        } finally {
            try {
                rs2.close();
                stmt2.close();
            } catch (Exception e) {
                logger.error("" + l.getClassName() + "/" + l.getMethodName() + ":" + l.getLineNumber() + e);
            }
        }

        if (statusVal == 1) {
//               query = "select  file_name , OPERATOR_NAME , SOURCE_NAME   from cdr_pre_processing_report  where   FILE_TYPE = 'I'  and tag = ("
//                       + " select tag from  cdr_pre_processing_report where SOURCE_NAME != 'all' and  file_type= 'O' and  FILE_NAME  = ("
//                       + "  select  file_Name  from cdr_pre_processing_report  where  file_Type = 'I' and   tag =  ( "
//                       + " select  tag  from cdr_pre_processing_report    where source_name = 'all' and  file_type= 'O' and  file_Name like ("
            //                      + " select  concat ( SUBSTR ( FILE_NAME , 0, LENGTH(FILE_NAME)-5 ) , '%' ) from CDR_FILE_DETAILS_DB where status = 'Done' ) ))) ";

            query = "select  file_name , OPERATOR_NAME , SOURCE_NAME   from cdr_pre_processing_report  where   FILE_TYPE = 'I'  and tag = ("
                    + " select tag from  cdr_pre_processing_report where SOURCE_NAME != 'all' and  file_type= 'O' and  FILE_NAME  = ("
                    + "  select  file_Name  from cdr_pre_processing_report  where  file_Type = 'I' and   tag =  ( "
                    + " select  tag  from cdr_pre_processing_report    where source_name = 'all' and  file_type= 'O' and  file_Name like   "
                    + "   '" + inFileName + "%'     ))) ";

            Statement stmt = null;
            ResultSet rs1 = null;
            try {
                String hostIp = propertyReader.getPropValue("hostIp").trim();
                String userName = propertyReader.getPropValue("user").trim();
                String port = propertyReader.getPropValue("port").trim();
                logger.info("   " + hostIp + " .. " + userName + " ,, " + port);
                String inputFolder = propertyReader.getPropValue("inputFolderPath").trim();
                String outputFolder = propertyReader.getPropValue("outputFolderPath").trim();
                String inputFilePath = propertyReader.getPropValue("inputFilePath").trim();
//               LocalDate currentDate = LocalDate.parse(date);
                Date date = new Date();
                LocalDate currentDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                int day = currentDate.getDayOfMonth();
                Month month = currentDate.getMonth();
                int year = currentDate.getYear();
                String suffixOutFolder = "processed/" + year + "/" + month.toString() + "/" + day + "/";
                String fileOutputPath = null;
                logger.info(" [" + query + "]");
                stmt = conn.createStatement();
                rs1 = stmt.executeQuery(query);
                while (rs1.next()) {
                    logger.info(" FILENAME::  " + rs1.getString("file_name"));
                    fileOutputPath = outputFolder + "/" + rs1.getString("OPERATOR_NAME") + "/" + rs1.getString("SOURCE_NAME") + "/" + suffixOutFolder;
                    logger.info(" fileOutputPath::  " + fileOutputPath);
                    scpProcess.DirectoryBuilderViaSSH(hostIp, port, userName, fileOutputPath);
                    //       scpProcess.uploadFile(hostIp, Integer.parseInt(port), userName, inputFolder + "/" + rs1.getString("OPERATOR_NAME") + "/" + rs1.getString("SOURCE_NAME") + "/", fileOutputPath, rs1.getString("file_name"));
                    scpProcess.deleteInputScpFile(hostIp, Integer.parseInt(port), userName, inputFilePath + "/" + rs1.getString("OPERATOR_NAME") + "/" + rs1.getString("SOURCE_NAME"), rs1.getString("file_name"));
                    scpProcess.moveScpFile(hostIp, Integer.parseInt(port), userName, inputFolder + "/" + rs1.getString("OPERATOR_NAME") + "/" + rs1.getString("SOURCE_NAME") + "/", fileOutputPath, rs1.getString("file_name"));
                    scpProcess.cdrFileStatusUpdate(conn, rs1.getString("file_name"));
                }
                logger.info(" Completed");
            } catch (Exception e) {
                logger.error("" + e);
            } finally {
                try {
                    rs1.close();
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("" + l.getClassName() + "/" + l.getMethodName() + ":" + l.getLineNumber() + e);
                }
            }
        }
    }

    public boolean uploadFile(String host, int port, String user, String localFilePath, String hostPath, String fileName) {
        Process upload = null;
        boolean uploadStatus = false;
        try {

            logger.info("SCP upload file command:[scp -rP  " + port + " " + localFilePath + fileName + "   " + user + "@" + host + ":" + hostPath + "]");
            upload = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "scp -rP " + port + " " + localFilePath + fileName + "   " + user + "@" + host + ":" + hostPath});
            upload.waitFor();
            if (upload.exitValue() == 0) {
                uploadStatus = true;
            }

            logger.info("Upload Status   " + uploadStatus);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            uploadStatus = false;
        } finally {
            if (upload != null) {
                upload.destroy();
            }
        }
        return uploadStatus;
    }

    public void DirectoryBuilderViaSSH(String host, String port, String user, String hostPath) {
        logger.info("DirectoryBuilderViaSSH :: " + " ssh " + user + "@" + host + " -p " + port + " \" mkdir -p " + hostPath + "  \" ");
        Process upload = null;
        boolean uploadStatus = false;
        try {   //ssh  ceirapp@172.24.2.60  -p 22022 "mkdir -p  /u02/ceirdata/processed_cdr/seatel/st_p_gw/processed/2020/OCTOBER/29
            upload = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", " ssh " + user + "@" + host + " -p " + port + " \" mkdir -p " + hostPath + "  \" "});
            upload.waitFor();
            if (upload.exitValue() == 0) {
                uploadStatus = true;
            }
            logger.info(" Dir Status   " + uploadStatus);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            uploadStatus = false;
        } finally {
            if (upload != null) {
                upload.destroy();
            }
        }

    }

    public boolean moveScpFile(String host, int port, String user, String localFilePath, String hostPath, String fileName) {
        Process upload = null;
        boolean uploadStatus = false;
        try {
            //  ssh ceirapp@172.24.2.60 -p 22022 " mv /u01/bin/FileScpProcess-0.0.1-SNAPSHOT.jar /u01/bin/test/ " 

            logger.info(" ssh " + user + "@" + host + " -p " + port + " \" mv " + localFilePath + fileName + " " + hostPath + "  \" ");
            upload = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", " ssh " + user + "@" + host + " -p " + port + " \" mv " + localFilePath + fileName + " " + hostPath + "  \" "});

            upload.waitFor();
            if (upload.exitValue() == 0) {
                uploadStatus = true;

            }
            logger.info("Move Status   " + uploadStatus);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            uploadStatus = false;
        } finally {
            if (upload != null) {
                upload.destroy();
            }
        }
        return uploadStatus;
    }

    static void cdrFileStatusUpdate(Connection conn, String fileName) {
        Statement stmt = null;

        String query = "update CDR_FILE_DETAILS_DB set status = 'Processed' where file_name like  '" + fileName + "%' ";
        logger.info(" qury is " + query);

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (SQLException e) {
            logger.error("" + l.getClassName() + "/" + l.getMethodName() + ":" + l.getLineNumber() + e);
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error("" + l.getClassName() + "/" + l.getMethodName() + ":" + l.getLineNumber() + e);
            }
        }

    }

    public boolean deleteInputScpFile(String host, int port, String user, String inputFilePathWithOpSource, String fileName) {
        Process upload = null;
        boolean uploadStatus = false;
        try {
            logger.info(" deleteInputScpFile :: ssh " + user + "@" + host + " -p " + port + " \"  rm -f  " + inputFilePathWithOpSource + "/" + fileName + "     \" ");
            upload = Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", " ssh " + user + "@" + host + " -p " + port + " \"     rm -f  " + inputFilePathWithOpSource + "/" + fileName + "    \" "});

            upload.waitFor();
            if (upload.exitValue() == 0) {
                uploadStatus = true;

            }
            logger.info("Delete Status   " + uploadStatus);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            uploadStatus = false;
        } finally {
            if (upload != null) {
                upload.destroy();
            }
        }
        return uploadStatus;
    }

}

//            query = "select file_name , OPERATOR_NAME , SOURCE_NAME  from cdr_pre_processing_report  where   FILE_TYPE = 'I'  and tag ="
//                  + " ( select tag from  cdr_pre_processing_report where SOURCE_NAME != 'all' and  file_type= 'O' and  FILE_NAME  ="
//                  + " (  select  file_Name  from cdr_pre_processing_report  where  file_Type = 'I' and   tag = "
//                  + " (  select  tag  from cdr_pre_processing_report    where source_name = 'all' and  file_type= 'O'"
//                  + " and  file_Name = ( select FILE_NAME from CDR_FILE_DETAILS_DB where status = 'Done'        )      )))  ";

