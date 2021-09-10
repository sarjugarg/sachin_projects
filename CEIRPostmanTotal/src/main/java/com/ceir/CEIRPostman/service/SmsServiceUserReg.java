/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceir.CEIRPostman.service;

import java.util.List;
import java.util.Objects;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ceir.CEIRPostman.Repository.PolicyBreachNotificationRepository;
import com.ceir.CEIRPostman.RepositoryService.EndUserRepoService;
import com.ceir.CEIRPostman.RepositoryService.MessageRepoSevice;
import com.ceir.CEIRPostman.RepositoryService.PolicyBreachNotificationRepoImpl;
import com.ceir.CEIRPostman.RepositoryService.RunningAlertRepoService;
import com.ceir.CEIRPostman.RepositoryService.SystemConfigurationDbRepoImpl;
import com.ceir.CEIRPostman.RepositoryService.UserRepoService;
import com.ceir.CEIRPostman.RepositoryService.UserTempRepoService;
import com.ceir.CEIRPostman.configuration.AppConfig;
import com.ceir.CEIRPostman.model.PolicyBreachNotification;
import com.ceir.CEIRPostman.model.RunningAlertDb;
import com.ceir.CEIRPostman.model.SystemConfigurationDb;
import com.ceir.CEIRPostman.util.SmsUtil;

@Service
public class SmsServiceUserReg implements Runnable {

     @Autowired
     SmsUtil emailUtil;

     @Autowired
     PolicyBreachNotificationRepository policyBreachNotificationRepo ;
     
       @Autowired
     PolicyBreachNotificationRepoImpl policyBreachNotificationRepoImpl;

     @Autowired
     AppConfig appConfig;

     @Autowired
     SystemConfigurationDbRepoImpl systemConfigRepoImpl;

     @Autowired
     EndUserRepoService endUserRepoService;

     @Autowired
     UserRepoService userRepoService;

     @Autowired
     UserTempRepoService userTempRepoService;

     @Value("${type}")
     String type;

     @Autowired
     RunningAlertRepoService alertDbRepo;

     @Autowired
     MessageRepoSevice messageRepo;

     @Autowired
     AuthorityRepoService authorityRepo;

     private final Logger log = Logger.getLogger(getClass());

     public void run() {
          SystemConfigurationDb batchSizeData = systemConfigRepoImpl.getDataByTag("Total_email_Send_InSec");
          SystemConfigurationDb emailProcessSleep = systemConfigRepoImpl.getDataByTag("EmailProcess_Sleep");
          SystemConfigurationDb sleepTps = systemConfigRepoImpl.getDataByTag("Email_TPS_Milli_Sec");
          SystemConfigurationDb fromEmail = systemConfigRepoImpl.getDataByTag("Email_Username");
          SystemConfigurationDb emailRetryCount = systemConfigRepoImpl.getDataByTag("Email_Retry_Count");
          //SystemConfigurationDb authorityMailSend = systemConfigRepoImpl.getDataByTag("Reporting_Authority_Mail_Status");
          //MessageConfigurationDb messageDb = messageRepo.getByTag("Reporting_Authority_Notification");
          Integer sleepTimeinMilliSec = 0;
          Integer emailretrycountValue = 0;
          //Integer authorityStatusValue = 0;
          try {
               emailretrycountValue = Integer.parseInt(emailRetryCount.getValue());
               log.info("sms  retry count value: " + emailRetryCount.getValue());
          } catch (Exception e) {
               RunningAlertDb alertDb = new RunningAlertDb("alert008", "sms retry count value not found in db", 0);
               alertDbRepo.saveAlertDb(alertDb);
               log.info(e.toString());
          }

          try {
               sleepTimeinMilliSec = Integer.parseInt(sleepTps.getValue());

          } catch (Exception e) {
               log.info(e.toString());
          }
          while (true) {
               log.info("inside SmsServiceUSerREg process");
               int batchSize = 0;
               if (batchSizeData != null) {
                    log.info("no of Sms per second value from db: " + batchSizeData.getValue());
                    batchSize = Integer.parseInt(batchSizeData.getValue());
               } else {
                    batchSize = 1;
               }

               try {
                    log.info("inside sms 4 UserReg process");
                    log.info("going to fetch data from policyBreachnotification table by status=1 and channel type= " + type);
                    List<PolicyBreachNotification> notificationData = policyBreachNotificationRepoImpl.dataByStatusAndChannelType(1, type);   //change here 
                    int totalMailsent = 0;
                    int totalMailNotsent = 0;

                    if (notificationData.isEmpty() == false) {
                         log.info("notification data is not empty and size is " + notificationData.size());
                           int sNo = 0;
                         emailUtil.setBatchSize(batchSize, notificationData.size());
                         for (PolicyBreachNotification notification : notificationData) {
                              log.info(" policyBreachnotification data id= " + notification.getId());
                              sNo++;
                              String body = new String();
                              body = notification.getMessage();
                              String toEmail =  notification.getContactNumber().toString();
                              if (Objects.nonNull(notification.getContactNumber()) && notification.getContactNumber() != 0) {
                                    boolean emailStatus = false;
                                   if (toEmail != null && !toEmail.isEmpty()) {
                                        log.info("toSms  " + toEmail);
                                        log.info("fromSms  " +  fromEmail.getValue() );
                                             emailStatus = emailUtil.sendSmss(toEmail, fromEmail.getValue(),
                                                     notification.getSubject(), body, notificationData.size(), sNo,
                                                     sleepTimeinMilliSec);
                                             if (emailStatus) {
                                                  notification.setStatus(0);
                                                  totalMailsent++;
                                             } else {
                                                  notification.setRetryCount(notification.getRetryCount() + 1);
                                                  if (notification.getRetryCount() >= emailretrycountValue) {
                                                       notification.setStatus(2);
                                                  }
                                                  totalMailNotsent++;
                                             }  
                                   } else {
                                        log.info("if sms msg value for this user id " + notification.getUserId()
                                                + " not found in db ");
                                        notification.setRetryCount(notification.getRetryCount() + 1);
                                        notification.setStatus(2);
                                   }

                              } else {
                                   notification.setRetryCount(notification.getRetryCount() + 1);
                                   notification.setStatus(2);
                                   log.info("msisdn phone no. for this notification is either null or 0");
                              }

                              policyBreachNotificationRepo.save(notification);
                         }

                         log.info("total sms sent=  " + totalMailsent);
                         log.info("sms failed to send: " + totalMailNotsent);
                         emailUtil.setIndexZero();
                    } else {
                         log.info("notification data is  empty");
                         log.info(" so no sms is pending to send");
                    }
                    log.info("exit from sms process");
               } catch (Exception e) {
                    log.info("error in sending email");
                    log.info(e.toString());
                    log.info(e.toString());
               }
               log.info("exit from  service");
           System.exit(0);
//               try {
//                    Thread.sleep(Integer.parseInt(emailProcessSleep.getValue()));
//               } catch (Exception e) {
//                    log.info(e.toString());
//               }
          }
     }
}
