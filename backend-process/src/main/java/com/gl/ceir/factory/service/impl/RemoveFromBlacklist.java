/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.ceir.factory.service.impl;

import com.gl.ceir.entity.Grievance;
import com.gl.ceir.entity.SystemConfigurationDb;
import com.gl.ceir.entity.User;
import com.gl.ceir.factory.service.BaseService;
import com.gl.ceir.repo.RemoveBlacklistRepo;
import com.gl.ceir.util.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author maverick
 */

@Component
public class RemoveFromBlacklist extends BaseService {

     private static final Logger logger = LogManager.getLogger(RemoveFromBlacklist.class);

     @Autowired
     RemoveBlacklistRepo removeBlacklistRepo;

     public void fetch() {
          try {
               SystemConfigurationDb defaultPerioremoveBlacklist = this.systemConfigurationDbRepository.getByTag("GSMA_BL_MAX_RETENTION_PERIOD_DAYS_IN_DB");
               String fromDate = DateUtil.nextDate(Integer.parseInt(defaultPerioremoveBlacklist.getValue()) * -1);
               SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-YY");
               fromDate = sdf.format(fromDate);
               logger.info(" fromDatefromDate :" + fromDate);
               process(fromDate);
          } catch (Exception e) {
               logger.info(e.getMessage(), e);
               Map<String, String> bodyPlaceholder = new HashMap<>();
               bodyPlaceholder.put("<e>", e.getMessage());
               bodyPlaceholder.put("<process_name>", "Remove From Blacklist ");
               onErrorRaiseAnAlert("alert006", bodyPlaceholder);
          }

     }

     public void process(Object o) {
          String date = (String) o;
          System.out.println("finalPrevoiusDate :" + date);
          logger.info(" finalPrevoiusDate :" + date);
          try {
               removeBlacklistRepo.deleteByDate(date);
          } catch (Exception e) {
               logger.info(e.getMessage(), e);
               Map<String, String> bodyPlaceholder = new HashMap<>();
               bodyPlaceholder.put("<e>", e.getMessage());
               bodyPlaceholder.put("<process_name>", "Remove From Blacklist");
               onErrorRaiseAnAlert("alert006", bodyPlaceholder);
          }
     }
}
