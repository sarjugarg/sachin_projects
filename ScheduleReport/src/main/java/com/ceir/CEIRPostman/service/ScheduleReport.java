/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceir.CEIRPostman.service;

import com.ceir.CEIRPostman.RepositoryService.RunningAlertRepoService;
import com.ceir.CEIRPostman.RepositoryService.ScheduleReportDbRepoImpl;
import com.ceir.CEIRPostman.RepositoryService.SystemConfigurationDbRepoImpl;
import com.ceir.CEIRPostman.configuration.AppConfig;
import org.json.JSONObject;
import com.ceir.CEIRPostman.model.ScheduleReportDb;
import com.ceir.CEIRPostman.model.RunningAlertDb;
import com.ceir.CEIRPostman.model.SystemConfigurationDb;
import com.ceir.CEIRPostman.util.EmailUtil;
import com.ceir.CEIRPostman.util.HttpConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author maverick
 */
@Service
public class ScheduleReport implements Runnable {

    @Autowired
    EmailUtil emailUtil;

    @Autowired
    AppConfig appConfig;

    @Autowired
    SystemConfigurationDbRepoImpl systemConfigRepoImpl;

    @Value("${type}")
    String type;

    @Autowired
    RunningAlertRepoService alertDbRepo;

    @Autowired
    ScheduleReportDbRepoImpl scheduleReportRepoImpl;

    @Autowired
    HttpConnection httpConnection;

    private final Logger log = Logger.getLogger(getClass());

    public void run() {

        SystemConfigurationDb batchSizeData = systemConfigRepoImpl.getDataByTag("Total_email_Send_InSec");
        SystemConfigurationDb emailProcessSleep = systemConfigRepoImpl.getDataByTag("EmailProcess_Sleep");
        SystemConfigurationDb sleepTps = systemConfigRepoImpl.getDataByTag("Email_TPS_Milli_Sec");
        SystemConfigurationDb fromEmail = systemConfigRepoImpl.getDataByTag("Email_Username");
        SystemConfigurationDb emailRetryCount = systemConfigRepoImpl.getDataByTag("Email_Retry_Count");
        Integer sleepTimeinMilliSec = 0;
        Integer emailretrycountValue = 0;

        try {
            log.info("going to fetch data from   table by flag =1  ");
            List<ScheduleReportDb> scheduleReportDbData = scheduleReportRepoImpl.getDataByFlag("Enable");   //change here 
            if (scheduleReportDbData.isEmpty() == false) {
                log.info("notification data is not empty and size is " + scheduleReportDbData.size());
//                    emailUtil.setBatchSize(batchSize, scheduleReportDbData.size());
                for (ScheduleReportDb ScheduleReportDbList : scheduleReportDbData) {
                    String category = ScheduleReportDbList.getCategory();
                    String reportnameId = ScheduleReportDbList.getReportName();
                    String toEmailArr[] = ScheduleReportDbList.getEmailId().split(";");
                    String Action = ScheduleReportDbList.getAction();
                    String body = "Hi, \n Please find the report, \n Regards CEIR Team";

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -1);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String startDate = dateFormat.format(cal.getTime());
                    String responseBody = "{\n"
                            + "\"reportnameId\": " + reportnameId + "   ,\n"
                            + "\"startDate\": \"" + startDate + "\",\n"
                            + "\"typeFlag\": \"2\"\n"
                            + "}";
                    String tag = "http://172.24.2.58:9502/CEIR/report/data?file=1&pageNumber=0&pageSize=10";
                    String reslt = httpConnection.HttpApiConnecter(tag, responseBody);
                    log.info("reslt  " + reslt);
                    JSONObject object = new JSONObject(reslt);
                    String attachment = object.getString("filePath") + object.getString("fileName");
                    String subject = object.getString("fileName")  ;
                  
                    for (String toEmail : toEmailArr) {
                        if (toEmail != null && !toEmail.isEmpty()) {
                            log.info("toSms  " + toEmail);
                            log.info("fromEmail  " + fromEmail.getValue());
                            log.info("FileFullName   " + attachment);
                            emailUtil.sendEmailWithAttactment(toEmail, fromEmail.getValue(), subject, body, attachment);
                        } else {
                            log.info("if sms msg value for this user id  not found in db ; toEmail is null ");
                        }
                    }

                }
                log.info("exit from   process");
            }
        } catch (Exception e) {
            log.error("error in sending email" + e.toString());
        }
        log.info("exit from  service");
        System.exit(0);

    }

}

//        try {
//            emailretrycountValue = Integer.parseInt(emailRetryCount.getValue());
//            log.info("sms  retry count value: " + emailRetryCount.getValue());
//            sleepTimeinMilliSec = Integer.parseInt(sleepTps.getValue());
//            log.info("sms  sleepTimeinMilliSec : " + sleepTimeinMilliSec);
//            log.info("inside  process");
//            int batchSize = 0;
//            if (batchSizeData != null) {
//                log.info("no of Sms per second value from db: " + batchSizeData.getValue());
//                batchSize = Integer.parseInt(batchSizeData.getValue());
//            } else {
//                batchSize = 1;
//            }
//        } catch (Exception e) {
//            RunningAlertDb alertDb = new RunningAlertDb("alert008", "sms retry count value not found in db", 0);
//            alertDbRepo.saveAlertDb(alertDb);
//            log.info(e.toString());
//        }
