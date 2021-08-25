/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

import static com.gl.FileScpProcess.ScpJavaProcess.propertyReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

/**
 *
 * @author maverick
 */
public class CreateDirectory {

     static Logger logger = Logger.getLogger(CreateDirectory.class);

     void showServerReply(FTPClient ftpClient) {
          String[] replies = ftpClient.getReplyStrings();
          if (replies != null && replies.length > 0) {
               for (String aReply : replies) {
                    logger.info("SERVER: " + aReply);
               }
          }
     }

     public void DirectoryBuilder(String dir) {

 
          FTPClient ftpClient = new FTPClient();
          try {
               
               logger.info("Directory : " + dir ) ;
               
               // reading prop file again and again , 
               String server = propertyReader.getPropValue("hostIp").trim();
               String user = propertyReader.getPropValue("userName").trim();
               String pass = propertyReader.getPropValue("hostPassword").trim();
               int port = parseInt(propertyReader.getPropValue("port").trim());

               ftpClient.connect(server, port);
               showServerReply(ftpClient);
               int replyCode = ftpClient.getReplyCode();
               if (!FTPReply.isPositiveCompletion(replyCode)) {
                    logger.info("Operation failed. Server reply code: " + replyCode);
                    return;
               }
               boolean success = ftpClient.login(user, pass);
               showServerReply(ftpClient);
               if (!success) {
                    logger.info("Could not login to the server");
                    return;
               }
//               String dirToCreate = "/upload123";
               success = ftpClient.makeDirectory(dir);
               showServerReply(ftpClient);
               if (success) {
                    logger.info("Successfully created directory: " + dir);
               } else {
                    logger.info("Failed to create directory. See server's reply.");
               }
               ftpClient.logout();
               ftpClient.disconnect();
          } catch (IOException ex) {
               logger.error("Oops! Something wrong happened " + ex);
               ex.printStackTrace();
          }
     }

    
}
