/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import org.apache.log4j.Logger;

/**
 *
 * @author maverick
 */
public class CP2FileTransfer {

    public static PropertyReader propertyReader = new PropertyReader();
    static Logger log = Logger.getLogger(CP2FileTransfer.class);

    int count = 0;

    public void cp2(String operator_param, String source_param) {

        String sourceDirectory = "";
        String targetDirectory = "";
        String cdrRecdServer = "";
        try {
            sourceDirectory = propertyReader.getPropValue("target_path").trim() + "/" + operator_param + "/" + source_param + "/";
//            sourceDirectory = propertyReader.getPropValue("source_path").trim() + "/" + operator_param + "/" + source_param + "/";
//            targetDirectory = propertyReader.getPropValue("target_path").trim() + "/" + operator_param + "/" + source_param + "/";
            targetDirectory = propertyReader.getPropValue("remoteTargetPath").trim() + "/" + operator_param + "/" + source_param + "/";
            cdrRecdServer = propertyReader.getPropValue("cdrRecdServer").trim();
            log.info(sourceDirectory);

            String copyLocation = propertyReader.getPropValue("copyLocation").trim();

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
            log.info(copyLocation);

            if ("local".equalsIgnoreCase(copyLocation)) {
                List<CDRFileRecords> optional = new LinkedList<>();
//                if ("SIG1".equalsIgnoreCase(cdrRecdServer)) {
//                    optional = findByOperatorAndSourceAndStatusSIG1AndCdrRecdServer(operator_param, source_param, "INIT", cdrRecdServer);
//                }
////                else if ("SIG2".equalsIgnoreCase(cdrRecdServer)) {
////                    optional = findByOperatorAndSourceAndStatusSIG2AndCdrRecdServer(operator_param, source_param, "INIT", cdrRecdServer);
////                }

                if ("SIG1".equalsIgnoreCase(cdrRecdServer)) {
                    optional = findByOperatorAndSourceAndStatusSIG1AndCdrRecdServer(operator_param, source_param, "INIT", cdrRecdServer);
                } else if ("SIG2".equalsIgnoreCase(cdrRecdServer)) {
                    optional = findByOperatorAndSourceAndStatusSIG2AndCdrRecdServer(operator_param, source_param, "INIT", cdrRecdServer);
                }

                log.info("Operator " + operator_param + " >> source " + source_param + " Copying Number of files : " + optional.size());

                if (optional.size() > 0) {
                    start_time = System.currentTimeMillis();
                    start_timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                    for (CDRFileRecords result : optional) {
                        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                        String destinationPath = targetDirectory + result.getFileName();
                        log.info("copy file to destinationPath:" + destinationPath);

                        Path temp;
                        try {

                            temp = Files.copy(Paths.get(sourceDirectory + result.getFileName()), Paths.get(destinationPath),
                                    StandardCopyOption.REPLACE_EXISTING);
                            log.info("File copied successfully to destination path " + destinationPath);
                            count = count + 1;
                            // updating file status
                            if ("SIG1".equalsIgnoreCase(cdrRecdServer)) {
                                log.info("LOCAL : rec. server : " + cdrRecdServer + " updating status to DONE");
                                modifyFileStatus("DONE", result.getStatusSIG2(), timeStamp,
                                        result.getSig2Utime(), result.getId());
                            } else if ("SIG2".equalsIgnoreCase(cdrRecdServer)) {
                                log.info("LOCAL : rec. server : " + cdrRecdServer + " updating status to DONE");
                                modifyFileStatus(result.getStatusSIG1(), "DONE", result.getSig1Utime(),
                                        timeStamp, result.getId());
                            }

                        } catch (IOException e) {
                            log.info("Failed to copy the file due to reason :" + e.toString());
                            e.printStackTrace();
                        } catch (Exception e) {
                            log.error("oops updation failed due to reason : " + e.toString());
                        }
                    }
                }

            } // in case remote value set in properties file	
            else if ("remote".equalsIgnoreCase(copyLocation)) {

                List<CDRFileRecords> optional = new LinkedList<>();

//                if ("SIG1".equalsIgnoreCase(cdrRecdServer)) {
//                    optional = findByOperatorAndSourceAndStatusSIG1AndCdrRecdServerRemote(operator_param, source_param, "INIT", cdrRecdServer);
//                }
//                else if ("SIG2".equalsIgnoreCase(propertiesReader.cdrRecdServer)) {
//                    optional = cDRFileRecordsServiceImpl
//                            .findByOperatorAndSourceAndStatusSIG1AndCdrRecdServer(operator_param, source_param, "INIT", propertiesReader.cdrRecdServer);
//                }
                if ("SIG1".equalsIgnoreCase(cdrRecdServer)) {
                    optional = findByOperatorAndSourceAndStatusSIG2AndCdrRecdServer(operator_param, source_param, "INIT", cdrRecdServer);
                } else if ("SIG2".equalsIgnoreCase(cdrRecdServer)) {
                    optional = findByOperatorAndSourceAndStatusSIG1AndCdrRecdServer(operator_param, source_param, "INIT", cdrRecdServer);
                }

                log.info("Operator " + operator_param + " >> source " + source_param + " Copying Number of files : " + optional.size());

                if (optional.size() > 0) {
                    boolean isServerUtilityAlive = isSocketAlive(REMOTE_HOST, REMOTE_PORT, timeout);
                    if (isServerUtilityAlive == true) {
                        Session jschSession = null;
                        start_time = System.currentTimeMillis();
                        start_timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                        try {
                            JSch jsch = new JSch();
                            jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
                            // authenticate using password
                            jschSession.setPassword(PASSWORD);
                            jschSession.setConfig("StrictHostKeyChecking", "no");
                            // 10 seconds session timeout
                            jschSession.connect(SESSION_TIMEOUT);
                            Channel sftp = jschSession.openChannel("sftp");
                            // 5 seconds timeout
                            sftp.connect(CHANNEL_TIMEOUT);
                            ChannelSftp channelSftp = null;
                            for (CDRFileRecords result : optional) {
                                String timeStamp_during_remote = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
                                channelSftp = (ChannelSftp) sftp;
                                // transfer file from local to remote server
                                channelSftp.put(sourceDirectory + result.getFileName(),
                                        REMOTE_TARGET_PATH + result.getFileName());
                                log.info("file " + result.getFileName() + " copied to server " + REMOTE_HOST);
                                count = count + 1;
                                // download file from remote server to local
                                // channelSftp.get(remoteFile, localFile);
// updating file status							

                                if ("SIG1".equalsIgnoreCase(cdrRecdServer)) {
                                    modifyFileStatus(result.getStatusSIG1(), "DONE", result.getSig1Utime(),
                                            timeStamp_during_remote, result.getId());
                                    log.info("REMOTE : rec. server : " + cdrRecdServer + " updating status to DONE");
                                } else if ("SIG2".equalsIgnoreCase(cdrRecdServer)) {
                                    modifyFileStatus("DONE", result.getStatusSIG2(), timeStamp_during_remote,
                                            result.getSig2Utime(), result.getId());
                                    log.info("REMOTE : rec. server : " + cdrRecdServer + " updating status to DONE");
                                }

                            }
                            channelSftp.exit();

                        } catch (Exception e) {
                            e.printStackTrace();
                            log.info("unable to eastablish connection between remote machine. " + e.toString());
                        } finally {
                            if (jschSession != null) {
                                jschSession.disconnect();
                                log.info("connection closed with hostname " + REMOTE_HOST);
                            }
                        }

                    }
                }
            }
            long end_time = System.currentTimeMillis();
            String end_timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
            log.info("COPY PROCESS ::  start time : " + start_timeStamp + " >> end time : " + end_timeStamp + " >> file transferred : " + count + " >> total time taken :" + ((end_time - start_time) / 1000) / 60 + "minutes and " + ((end_time - start_time) / 1000) % 60 + " seconds >>" + " operator :" + operator_param + " >> source :" + source_param + " >> file transferred per second");
        } catch (Exception e) {
            e.printStackTrace();
            log.info("unable to eastablish connection between remote machine. " + e.toString());
        }

    }

    public boolean isSocketAlive(String hostName, int port, int timeout) {
        boolean isAlive = false;
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        Socket socket = new Socket();
        try {
            socket.connect(socketAddress, timeout);
            socket.close();
            isAlive = true;
            log.info("hostname " + hostName + " is listening on port" + port);
        } catch (SocketTimeoutException exception) {
            log.info("SocketTimeoutException " + hostName + ":" + port + ". " + exception.getMessage());
        } catch (IOException exception) {
            log.info("IOException - Unable to connect to " + hostName + ":" + port + ". " + exception.getMessage());
        }
        return isAlive;
    }

    
    
    private List<CDRFileRecords> findByOperatorAndSourceAndStatusSIG1AndCdrRecdServer(String operator_param, String source_param, String init, String cdrRecdServer) {

           String stsSig = "STATUS_SIG1";

        Statement stmt = null;
        ResultSet rs = null;
        String query = null;

        List<CDRFileRecords> CDRFileRecordss = new LinkedList<>();
        try {
            query = "select id , source , operator, FILE_NAME ,STATUS_SIG1 ,STATUS_SIG2,CDR_RECD_SERVER,STS_SIG1_UTIME, STS_SIG2_UTIME , FILE_DATE  ,file_size, record_size  from cdr_file_records_db where"
                    + " operator = '" + operator_param + "' and  source = '" + source_param + "' and   " + stsSig + "  = '" + init + "' and CDR_RECD_SERVER  = '" + cdrRecdServer + "'  ";

            log.info(query);
            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                CDRFileRecordss.add(new CDRFileRecords(rs.getLong("id"),
                        rs.getString("source"),
                        rs.getString("operator"), rs.getString("file_Name"),
                        rs.getString("STATUS_SIG1"), rs.getString("STATUS_SIG2"),
                        rs.getString("CDR_RECD_SERVER"), rs.getString("STS_SIG1_UTIME"), rs.getString("STS_SIG2_UTIME") , rs.getString("FILE_DATE")  ,rs.getString("file_size")      , rs.getString("record_size")  
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

  
    private List<CDRFileRecords> findByOperatorAndSourceAndStatusSIG2AndCdrRecdServer(String operator_param, String source_param, String init, String cdrRecdServer) {

        String stsSig = "STATUS_SIG2";

        Statement stmt = null;
        ResultSet rs = null;
        String query = null;

        List<CDRFileRecords> CDRFileRecordss = new LinkedList<>();
        try {
            query = "select id , source , operator, FILE_NAME ,STATUS_SIG1 ,STATUS_SIG2,CDR_RECD_SERVER,STS_SIG1_UTIME, STS_SIG2_UTIME ,FILE_DATE ,file_size, record_size  from cdr_file_records_db where"
                    + " operator = '" + operator_param + "' and  source = '" + source_param + "' and   " + stsSig + "  = '" + init + "' and CDR_RECD_SERVER  = '" + cdrRecdServer + "'  ";

            log.info(query);
            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                CDRFileRecordss.add(new CDRFileRecords(rs.getLong("id"),
                        rs.getString("source"),
                        rs.getString("operator"), rs.getString("file_Name"),
                        rs.getString("STATUS_SIG1"), rs.getString("STATUS_SIG2"),
                        rs.getString("CDR_RECD_SERVER"), rs.getString("STS_SIG1_UTIME"), rs.getString("STS_SIG2_UTIME")  ,  rs.getString("FILE_DATE") , rs.getString("file_size")      , rs.getString("record_size")     
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
  private void modifyFileStatus(String statusSIG1, String statusSIG2, String sig1Utime, String sig2Utime, long id) {
        
        String query = "update cdr_file_records_db  set STATUS_SIG1 = '" + statusSIG1 + "' ,STATUS_SIG2 = '" + statusSIG2 + "', STS_SIG1_UTIME='" + sig1Utime + "' , STS_SIG2_UTIME='" + sig2Utime + "' where ID = " + id + "";
 
        try {
            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
            Statement stmt = conn.createStatement();
            log.info(query);
            stmt.executeUpdate(query);
            conn.close();
        } catch (Exception e) {
            System.out.println("Error " + e);
            log.error("" + e);
        }
    }

}

//    private List<CDRFileRecords> findByOperatorAndSourceAndStatusSIG1AndCdrRecdServerRemote(String operator_param, String source_param, String init, String cdrRecdServer) {
//
//        String stsSig = "STATUS_SIG1";
//        if ("SIG1".equalsIgnoreCase(cdrRecdServer)) {
//            stsSig = "STATUS_SIG2";
//        }
//
//        Statement stmt = null;
//        ResultSet rs = null;
//        String query = null;
//
//        List<CDRFileRecords> CDRFileRecordss = new LinkedList<>();
//        try {
//            query = "select id , source , operator, FILE_NAME ,STATUS_SIG1 ,STATUS_SIG2,CDR_RECD_SERVER,STS_SIG1_UTIME, STS_SIG2_UTIME  from cdr_file_records_db where"
//                    + " operator = '" + operator_param + "' and  source = '" + source_param + "' and   " + stsSig + "  = '" + init + "' and CDR_RECD_SERVER  = '" + cdrRecdServer + "'  ";
//
//            log.info(query);
//            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
//            stmt = conn.createStatement();
//            rs = stmt.executeQuery(query);
//
//            while (rs.next()) {
//                CDRFileRecordss.add(new CDRFileRecords(rs.getLong("id"),
//                        rs.getString("source"),
//                        rs.getString("operator"), rs.getString("file_Name"),
//                        rs.getString("STATUS_SIG1"), rs.getString("STATUS_SIG2"),
//                        rs.getString("CDR_RECD_SERVER"), rs.getString("STS_SIG1_UTIME"), rs.getString("STS_SIG2_UTIME")
//                ));
//            }
//        } catch (Exception e) {
//            log.error("" + e);
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (stmt != null) {
//                    stmt.close();
//                }
//            } catch (SQLException e) {
//                log.error("" + e);
//            }
//        }
//        return CDRFileRecordss;
//    }
