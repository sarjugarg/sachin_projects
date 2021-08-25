package com.gl.ceir.factory.service.impl;

import com.gl.ceir.entity.SystemConfigurationDb;
import com.gl.ceir.entity.User;
import com.gl.ceir.factory.service.BaseService;
import com.gl.ceir.factory.service.transaction.RemoveInCompleteUserTransaction;
import com.gl.ceir.pojo.RawMail;
import com.gl.ceir.service.UsersServiceImpl;
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
public class RemoveIncompleteUser extends BaseService {
  private static final Logger logger = LogManager.getLogger(RemoveIncompleteUser.class);
  
  List<RawMail> rawMails = new ArrayList<>();
  
  @Autowired
  UsersServiceImpl usersServiceImpl;
  
  @Autowired
  RemoveInCompleteUserTransaction removeIncompleteUser;
  
  public void fetch() {
    try {
      Map<String, String> placeholderMapForAlert = new HashMap<>();
      SystemConfigurationDb removeInCompleteUserInDays = this.systemConfigurationDbRepository.getByTag("remove_in-complete_user_in_days");
      if (Objects.isNull(removeInCompleteUserInDays)) {
        placeholderMapForAlert.put("<tag>", "REMOVE_IN_COMPLETE_USER_IN_DAYS");
        placeholderMapForAlert.put("<process_name>", "RemoveIncompleteUser");
        onErrorRaiseAnAlert("alert003", placeholderMapForAlert);
        logger.info("Alert [ALERT_003] is raised. So, doing nothing.");
        return;
      } 
      List<User> userList = this.usersServiceImpl.getUserWithStatusPendingOtp(Integer.parseInt(removeInCompleteUserInDays.getValue()) * -1);
      List<Long> userIds = new ArrayList<>();
      Map<String, String> placeholderMap = null;
      for (User user : userList) {
        userIds.add(user.getId());
        placeholderMap = new HashMap<>();
        placeholderMap.put("<User>", user.getUserProfile().getFirstName());
        this.rawMails.add(new RawMail("Email", 
              "MAIL_TO_USER_ON_ACCOUNT_REMOVAL_FOR_OTP_PENDING", 
              user.getId().longValue(), 
              8L, 
              "Process", 
              "Removal", 
              user.getUsername(), 
              "Removing user With Username " + user.getUsername(), 
              placeholderMap, 
              "USERS", 
              user.getUsertype().getUsertypeName(), 
              user.getUsertype().getUsertypeName()));
      } 
      if (userIds.isEmpty()) {
        logger.info("No user with otp pending is there in DB today. [" + DateUtil.nextDate(0) + "]");
      } else {
        process(userIds);
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
    List<Long> userIds = (List<Long>)o;
    try {
      this.removeIncompleteUser.performTransaction(userIds, this.rawMails);
    } catch (Exception e) {
      logger.info(e.getMessage(), e);
      Map<String, String> bodyPlaceholder = new HashMap<>();
      bodyPlaceholder.put("<e>", e.getMessage());
      bodyPlaceholder.put("<process_name>", "Remove incomplete users.");
      onErrorRaiseAnAlert("alert006", bodyPlaceholder);
    } 
  }
}
