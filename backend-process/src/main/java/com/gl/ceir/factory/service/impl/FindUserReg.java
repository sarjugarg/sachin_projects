package com.gl.ceir.factory.service.impl;

import com.gl.ceir.entity.DaywiseUserReg;
import com.gl.ceir.entity.DeviceUsageDb;
import com.gl.ceir.entity.PolicyBreachNotification;
import com.gl.ceir.factory.service.BaseService;
import com.gl.ceir.factory.service.transaction.FindUserRegTransaction;
import com.gl.ceir.pojo.MessageConfigurationDb;
import com.gl.ceir.pojo.RawMail;
import com.gl.ceir.repo.DaywiseUserRegRepository;
import com.gl.ceir.repo.MessageConfigurationDbRepository;
import com.gl.ceir.repo.PolicyBreachNotificationRepository;
import com.gl.ceir.service.DeviceUsageServiceImpl;
import com.gl.ceir.util.DateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FindUserReg extends BaseService {
  private static final Logger logger = LogManager.getLogger(FindUserReg.class);
  
  List<PolicyBreachNotification> policyBreachNotifications = new LinkedList<>();
  
  List<RawMail> rawMails = new ArrayList<>();
  
  @Autowired
  DaywiseUserRegRepository daywiseUserRegRepository;
  
  @Autowired
  PolicyBreachNotificationRepository policyBreachNotificationRepository;
  
  @Autowired
  FindUserRegTransaction findUserRegTransaction;
  
  @Autowired
  DeviceUsageServiceImpl deviceUsageServiceImpl;
  
  @Autowired
  MessageConfigurationDbRepository messageConfigurationDbRepository;
  
  public void fetch() {
    try {
      String channel = "SMS";
      String policyBreachMessage = "";
      String tag = "BLOCK_DEVICE_TAX_NOT_PAID";
      List<DeviceUsageDb> deviceUsageDbs = this.deviceUsageServiceImpl.getDeviceUsageOfTodayHavingActionUserReg();
      logger.info(deviceUsageDbs);
      if (deviceUsageDbs.isEmpty()) {
        logger.info("No User_Reg found close today. [" + DateUtil.nextDate(0) + "]");
      } else {
        List<DaywiseUserReg> daywiseUserRegs = new LinkedList<>();
        for (DeviceUsageDb deviceUsageDb : deviceUsageDbs) {
          Map<String, String> placeholderMap = new HashMap<>();
          DaywiseUserReg daywiseUserReg = this.daywiseUserRegRepository.getByImei(deviceUsageDb.getImei());
          if (Objects.isNull(daywiseUserReg)) {
            daywiseUserRegs.add(new DaywiseUserReg(deviceUsageDb.getImei(), deviceUsageDb.getMsisdn().longValue(), 1));
          } else {
            daywiseUserReg.setReminderCount(Integer.valueOf(daywiseUserReg.getReminderCount().intValue() + 1));
            daywiseUserRegs.add(daywiseUserReg);
          } 
          MessageConfigurationDb messageDB = this.messageConfigurationDbRepository.getByTagAndActive(tag, 0);
          policyBreachMessage = messageDB.getValue();
          this.policyBreachNotifications.add(new PolicyBreachNotification(channel, 
                policyBreachMessage, 
                "", 
                deviceUsageDb.getMsisdn(), 
                deviceUsageDb.getImei()));
          this.rawMails.add(new RawMail("SMS", 
                "REMINDER_DEVICE_TAX_NOT_PAID", 
                0L, 
                0L, 
                "Process", 
                "Reminder", 
                "", 
                "", 
                placeholderMap, 
                "END_USER", 
                "End User", 
                "End User"));
        } 
        process(daywiseUserRegs);
      } 
    } catch (NumberFormatException e) {
      Map<String, String> placeholderMapForAlert = new HashMap<>();
      placeholderMapForAlert.put("<e>", e.getMessage());
      placeholderMapForAlert.put("<process_name>", "FindUserReg");
      onErrorRaiseAnAlert("alert006", placeholderMapForAlert);
      logger.info("Alert [ALERT_006] is raised. So, doing nothing.");
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } 
  }
  
  public void process(Object o) {
    List<DaywiseUserReg> daywiseUserReg = (List<DaywiseUserReg>)o;
    this.findUserRegTransaction.performTransaction(daywiseUserReg, this.policyBreachNotifications, this.rawMails);
  }
}
