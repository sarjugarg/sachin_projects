/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

import java.io.File;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.apache.log4j.Logger;

/**
 *
 * @author maverick
 */
public class CP3FileTransfer {

    public static PropertyReader propertyReader = new PropertyReader();
    static Logger log = Logger.getLogger(CP3FileTransfer.class);

    public void cp3(String operator_param, String source_param) {

        try {
            String sourceDirectory = "";
            String targetDirectory = "";
            String cdrRecdServer = "";
            sourceDirectory = propertyReader.getPropValue("source_path").trim() + "/" + operator_param + "/" + source_param + "/";
            targetDirectory = propertyReader.getPropValue("target_path").trim() + "/" + operator_param + "/" + source_param + "/";
            cdrRecdServer = propertyReader.getPropValue("cdrRecdServer").trim();
            log.info(sourceDirectory);

            String copyLocation = propertyReader.getPropValue("copyLocation").trim();
            int count = 0;
            int timeout = parseInt(propertyReader.getPropValue("timeout"));
            final String REMOTE_HOST = propertyReader.getPropValue("hostName").trim();
            final String USERNAME = propertyReader.getPropValue("userName").trim();
            final String PASSWORD = propertyReader.getPropValue("password").trim();
            final int REMOTE_PORT = parseInt(propertyReader.getPropValue("serverPort").trim());
            final int SESSION_TIMEOUT = parseInt(propertyReader.getPropValue("sessionTimeout").trim());
            final int CHANNEL_TIMEOUT = parseInt(propertyReader.getPropValue("channelTimeout").trim());
            final String REMOTE_TARGET_PATH = propertyReader.getPropValue("remoteTargetPath").trim() + "/" + operator_param + "/" + source_param + "/";
            String start_timeStamp = null;
            long start_time = 0;

            String delete_file_from_dir = propertyReader.getPropValue("target_path").trim() + "/" + operator_param + "/" + source_param + "/";
            log.info("file to be delete from " + delete_file_from_dir);
            List<CDRFileRecords> optional = findByOperatorAndStatusSIG1AndStatusSIG2(operator_param, "DONE",
                    "DONE");
            log.info("Operator " + operator_param + " >> source " + source_param + " Number of files to Delete : " + optional.size());
            int zro = 0;
            if (optional.size() > zro) {
                start_time = System.currentTimeMillis();
                start_timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                log.info("inside deletion process ");

//                for (int i = 0; i < optional.size(); i++) {
//                    String filName = optional.get(i).getCdrRecdServer();
//                    log.info("inside deletion process 99 " + filName);
//                }
                for (CDRFileRecords result : optional) {
                    log.info("inside deletion process 11");
                    try {
                        log.info("inside deletion process 22");
                        File file = new File(delete_file_from_dir + result.getFileName());
                        log.info("File removed : " + delete_file_from_dir + result.getFileName());
                        if (file.exists()) {
                            Path path = Paths.get(delete_file_from_dir + result.getFileName());
                            Files.delete(path);
                            count = count + 1;
                            log.info("File removed done");
                            try {
                                CDRFilesDeleteAud cdrFilesDeleteAud = new CDRFilesDeleteAud();
                                cdrFilesDeleteAud.setSource(source_param);
                                cdrFilesDeleteAud.setOperator(operator_param);
                                cdrFilesDeleteAud.setFileName(result.getFileName());
                                cdrFilesDeleteAud.setStatusSIG1("DONE");
                                cdrFilesDeleteAud.setStatusSIG2("DONE");
                                cdrFilesDeleteAud.setCdrRecdServer(cdrRecdServer);
                                cdrFilesDeleteAud.setSig1Utime(result.getSig1Utime());
                                cdrFilesDeleteAud.setSig2Utime(result.getSig2Utime());

                                cdrFilesDeleteAud.setFileDate(result.getFileDate());
                                cdrFilesDeleteAud.setFileSize(result.getFileSize());
                                cdrFilesDeleteAud.setRecordSize(result.getRecordSize());

                                savedetails(cdrFilesDeleteAud);
                                deleteByIdDetails(result.getId());

                            } catch (Exception e) {
                                log.error("unusual happend during insertion in cdr_file_delete_aud table : " + e.toString());
                            }
                        } else {
                            log.error("file does not exist");

                        }
                    } catch (IOException e) {
                        log.error("Failed to remove file from path " + delete_file_from_dir + "due to reason" + e.toString());
                        e.printStackTrace();

                    } catch (Exception e) {
                        log.error("oops updation failed due to reason : " + e.toString());
                    }
                }
                long end_time = System.currentTimeMillis();
                String end_timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                log.info("DELETE PROCESS ::  start time : " + start_timeStamp + " >> end time : " + end_timeStamp + " >> file transferred : " + count + " >> total time taken :" + ((end_time - start_time) / 1000) / 60 + "minutes and " + ((end_time - start_time) / 1000) % 60 + " seconds >>" + " operator :" + operator_param + " >> source :" + source_param + " >> file transferred per second");
            }
        } catch (Exception e) {
            log.info(e);
        }
    }

    private List<CDRFileRecords> findByOperatorAndStatusSIG1AndStatusSIG2(String operator_param, String StatusSIG1, String StatusSIG2) {

        Statement stmt = null;
        ResultSet rs = null;
        String query = null;

        List<CDRFileRecords> CDRFileRecordss = new LinkedList<>();
        try {
            query = "select id , source , operator, FILE_NAME ,STATUS_SIG1 ,STATUS_SIG2,CDR_RECD_SERVER,STS_SIG1_UTIME, STS_SIG2_UTIME , file_date  , file_size , record_size from cdr_file_records_db where"
                    + " operator = '" + operator_param + "' and     STATUS_SIG1  = '" + StatusSIG1 + "' and  STATUS_SIG2  = '" + StatusSIG2 + "'  ";

            log.info(query);
            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                CDRFileRecordss.add(new CDRFileRecords(rs.getLong("id"),
                        rs.getString("source"),
                        rs.getString("operator"), rs.getString("file_Name"),
                        rs.getString("STATUS_SIG1"), rs.getString("STATUS_SIG2"),
                        rs.getString("CDR_RECD_SERVER"), rs.getString("STS_SIG1_UTIME"), rs.getString("STS_SIG2_UTIME"), rs.getString("file_date"), rs.getString("file_size"), rs.getString("record_size")
                ));
            }
        } catch (Exception e) {
            log.error("" + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                log.error("" + e);
            }
        }
        return CDRFileRecordss;

    }

    private void savedetails(CDRFilesDeleteAud cdrFilesDeleteAud) {

        try {
            String query = "insert into cdr_file_delete_aud ( CREATED_ON ,SOURCE ,OPERATOR, FILE_NAME,STATUS_SIG1 ,STATUS_SIG2 ,CDR_RECD_SERVER ,FILE_DELETE_TIME   ,file_date ,file_size  , record_size ) " //  ,rs.getString("")      , rs.getString("")  
                    + " values( current_timestamp , '" + cdrFilesDeleteAud.getSource() + "' , '" + cdrFilesDeleteAud.getOperator() + "' ,  '" + cdrFilesDeleteAud.getFileName() + "' , '" + cdrFilesDeleteAud.getStatusSIG1() + "' , '" + cdrFilesDeleteAud.getStatusSIG2() + "' , '" + cdrFilesDeleteAud.getCdrRecdServer() + "' , current_timestamp  , '" + cdrFilesDeleteAud.getFileDate() + "'  ,   '" + cdrFilesDeleteAud.getFileSize() + "'  ,  '" + cdrFilesDeleteAud.getRecordSize() + "'         )";
            log.info(query);
            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        } catch (Exception e) {
            log.info("Failed  " + e);
            e.printStackTrace();
        }

    }

    private void deleteByIdDetails(long id) {
        try {
            String query = " delete from cdr_file_records_db  where id = " + id;
            log.info(query);
            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
            Statement stmt = conn.createStatement();

            stmt.executeUpdate(query);
            stmt.close();
        } catch (Exception e) {
            log.info("Failed  " + e);
            e.printStackTrace();
        }
    }
}
