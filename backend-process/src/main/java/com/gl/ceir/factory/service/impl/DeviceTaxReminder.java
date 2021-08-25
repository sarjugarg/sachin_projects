package com.gl.ceir.factory.service.impl;

import com.gl.ceir.entity.EndUserDB;
import com.gl.ceir.entity.RegularizeDeviceDb;
import com.gl.ceir.entity.SystemConfigurationDb;
import com.gl.ceir.factory.service.BaseService;
import com.gl.ceir.pojo.UserWiseMailCount;
import com.gl.ceir.service.PolicyBreachNotiServiceImpl;
import com.gl.ceir.service.RegularizeDbServiceImpl;
import com.gl.ceir.util.DateUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeviceTaxReminder extends BaseService {
  private static final Logger logger = LogManager.getLogger(DeviceTaxReminder.class);
  
  @Autowired
  RegularizeDbServiceImpl regularizeDbServiceImpl;
  
  @Autowired
  PolicyBreachNotiServiceImpl policyBreachNotiServiceImpl;
  
  public void fetch() {
    try {
      SystemConfigurationDb graceDays = this.systemConfigurationDbRepository.getByTag("grace_period_for_rgister_device");
      SystemConfigurationDb reminderDays = this.systemConfigurationDbRepository.getByTag("reminder_days_before");
      SystemConfigurationDb sendNotiOnDeviceTaxNotPaid = this.systemConfigurationDbRepository.getByTag("send_noti_on_device_tax_not_paid");
      this.systemConfigMap.put("send_noti_on_device_tax_not_paid", sendNotiOnDeviceTaxNotPaid);
      if (Objects.isNull(graceDays)) {
        Map<String, String> placeholderMap = new HashMap<>();
        placeholderMap.put("<tag>", "GRACE_PERIOD_FOR_RGISTER_DEVICE");
        placeholderMap.put("<process_name>", "DeviceTaxReminder");
        onErrorRaiseAnAlert("alert003", placeholderMap);
        logger.info("Alert [ALERT_003] is raised. So, doing nothing.");
        return;
      } 
      if (Objects.isNull(reminderDays)) {
        Map<String, String> placeholderMap = new HashMap<>();
        placeholderMap.put("<tag>", "REMINDER_DAYS");
        placeholderMap.put("<process_name>", "DeviceTaxReminder");
        onErrorRaiseAnAlert("alert003", placeholderMap);
        logger.info("Alert [ALERT_003] is raised. So, doing nothing.");
        return;
      } 
      String toDate = DateUtil.nextDate((Integer.parseInt(graceDays.getValue()) - Integer.parseInt(reminderDays.getValue()) - 1) * -1);
      logger.info("Reminder will sent to user who has registered device on toDate[" + toDate + "] and not paid tax.");
      List<RegularizeDeviceDb> regularizeDeviceDbs = this.regularizeDbServiceImpl.getDevicesbyTaxStatusAndDateAndReminderFlag(toDate, 1,"N");
      List<RegularizeDeviceDb> processedDeviceDbs = new ArrayList<>();
      for (RegularizeDeviceDb regularizeDeviceDb : regularizeDeviceDbs) {
        EndUserDB endUserDB = regularizeDeviceDb.getEndUserDB();
        logger.info(endUserDB);
        if ("cambodian".equalsIgnoreCase(endUserDB.getNationality())) {
          regularizeDeviceDb.setReminderFlag("Y");
          processedDeviceDbs.add(regularizeDeviceDb);
          continue;
        } 
        logger.info("Current Device belong to a foreigner, So no need to send a reminder because of tax not paid.");
      } 
      if (processedDeviceDbs.isEmpty()) {
        logger.info("No new device of cambodian to send reminder found today[" + DateUtil.nextDate(0) + "]");
        return;
      } 
      logger.info("No. of reminders need send update today is[" + processedDeviceDbs.size() + "]");
      process(processedDeviceDbs);
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
    String tag = "REMINDER_DEVICE_TAX_NOT_PAID";
    List<RegularizeDeviceDb> regularizeDeviceDbs = (List<RegularizeDeviceDb>)o;
    logger.info("Going to set reminder flags for devices: " + regularizeDeviceDbs);
    this.regularizeDbServiceImpl.saveAllDevices(regularizeDeviceDbs);
    logger.info("Flag set for all devices.");
    logger.info("Going to send reminder for devices : " + regularizeDeviceDbs);
    List<UserWiseMailCount> userWiseMailCounts = this.regularizeDbServiceImpl.getUserWiseMailCountDto(regularizeDeviceDbs);
    this.policyBreachNotiServiceImpl.batchUpdatePolicyBreachNoti(tag, userWiseMailCounts);
    if ("Y".equalsIgnoreCase(((SystemConfigurationDb)this.systemConfigMap.get("send_noti_on_device_tax_not_paid")).getValue())) {
      this.regularizeDbServiceImpl.sendNotification(regularizeDeviceDbs, tag, "Reminder");
    } else {
      logger.info("WARN : Notification is off for reminding user on failure of tax paying of registered device.");
    } 
  }
}
