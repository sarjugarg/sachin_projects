package com.gl.ceir.factory.service.impl;

import com.gl.ceir.constant.Datatype;
import com.gl.ceir.constant.SearchOperation;
import com.gl.ceir.entity.Grievance;
import com.gl.ceir.entity.GrievanceHistory;
import com.gl.ceir.entity.SystemConfigurationDb;
import com.gl.ceir.entity.User;
import com.gl.ceir.factory.service.BaseService;
import com.gl.ceir.factory.service.transaction.CloseGrievanceTransaction;
import com.gl.ceir.pojo.RawMail;
import com.gl.ceir.pojo.SearchCriteria;
import com.gl.ceir.repo.GrievanceRepository;
import com.gl.ceir.repo.UserRepository;
import com.gl.ceir.specification.GenericSpecificationBuilder;
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
public class CloseGrievance extends BaseService {
  private static final Logger logger = LogManager.getLogger(CloseGrievance.class);
  
  List<Grievance> processedGrievances = new ArrayList<>();
  
  List<GrievanceHistory> grievanceHistories = new ArrayList<>();
  
  List<RawMail> rawMails = new ArrayList<>();
  
  @Autowired
  CloseGrievanceTransaction closeGrievanceTransaction;
  
  @Autowired
  GrievanceRepository grievanceRepository;
  
  @Autowired
  UserRepository userRepository;
  
  public void fetch() {
    User user = null;
    try {
      SystemConfigurationDb defaultPerioToCloseGrievance = this.systemConfigurationDbRepository.getByTag("default_period_to_close_grievance");
      if (Objects.isNull(defaultPerioToCloseGrievance)) {
        Map<String, String> placeholderMapForAlert = new HashMap<>();
        placeholderMapForAlert.put("<tag>", "DEFAULT_PERIOD_TO_CLOSE_GRIEVANCE");
        placeholderMapForAlert.put("<process_name>", "DeviceTaxReminder");
        onErrorRaiseAnAlert("alert003", placeholderMapForAlert);
        logger.info("Alert [ALERT_003] is raised. So, doing nothing.");
        return;
      } 
      String fromDate = DateUtil.nextDate(Integer.parseInt(defaultPerioToCloseGrievance.getValue()) * -1);
      String toDate = DateUtil.nextDate((Integer.parseInt(defaultPerioToCloseGrievance.getValue()) - 1) * -1);
      logger.info("Close grievance raised before the date[" + toDate + "] because of inactivity.");
      List<Grievance> grievances = this.grievanceRepository.findAll(buildSpecification(toDate).build());
      Map<String, String> placeholderMap = new HashMap<>();
      placeholderMap.put("<days>", defaultPerioToCloseGrievance.getValue());
      for (Grievance grievance : grievances) {
        grievance.setGrievanceStatus(4);
        this.processedGrievances.add(grievance);
        this.grievanceHistories.add(new GrievanceHistory(grievance.getGrievanceId(), Long.valueOf(grievance.getUserId().intValue()), 
              grievance.getUserType(), 
              grievance.getGrievanceStatus(), grievance.getTxnId(), grievance.getCategoryId(), 
              grievance.getFileName(), 
              grievance.getRemarks(), 
              Long.valueOf(-1L), 
              "System"));
        user = this.userRepository.getById(grievance.getUserId().intValue());
        if (Objects.nonNull(user)) {
          placeholderMap.put("<First name>", user.getUserProfile().getFirstName());
          placeholderMap.put("<Txn id>", grievance.getTxnId());
          this.rawMails.add(new RawMail("Email", 
                "MAIL_TO_USER_ON_GRIEVANCE_CLOSURE", 
                grievance.getUserId().intValue(), 
                6L, 
                "Process", 
                "Closure", 
                grievance.getTxnId(), 
                "Closure Of Grievance With Transaction Number " + grievance.getTxnId(), 
                placeholderMap, 
                "USERS", 
                grievance.getUserType(), 
                grievance.getUserType()));
          continue;
        } 
        logger.info("ALERT : No user is found for Grievance [" + grievance.getTxnId() + "]");
        Map<String, String> bodyPlaceHolderMap = new HashMap<>();
        bodyPlaceHolderMap.put("<id>", Long.toString(grievance.getUserId().intValue()));
        this.alertServiceImpl.raiseAnAlert("alert005", 0, bodyPlaceHolderMap);
      } 
      if (this.processedGrievances.isEmpty()) {
        logger.info("No grievance to close today. [" + DateUtil.nextDate(0) + "]");
      } else {
        process(this.processedGrievances);
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
    List<Grievance> grievances = (List<Grievance>)o;
    try {
      this.closeGrievanceTransaction.performTransaction(grievances, this.grievanceHistories, this.rawMails);
    } catch (Exception e) {
      logger.info(e.getMessage(), e);
      Map<String, String> bodyPlaceholder = new HashMap<>();
      bodyPlaceholder.put("<e>", e.getMessage());
      bodyPlaceholder.put("<process_name>", "Close Grievance");
      onErrorRaiseAnAlert("alert006", bodyPlaceholder);
    } 
  }
  
  private GenericSpecificationBuilder<Grievance> buildSpecification(String toDate) {
    GenericSpecificationBuilder<Grievance> cmsb = new GenericSpecificationBuilder(this.propertiesReader.dialect);
    cmsb.with(new SearchCriteria("grievanceStatus", Integer.valueOf(2), SearchOperation.EQUALITY, Datatype.STRING));
    cmsb.with(new SearchCriteria("modifiedOn", toDate, SearchOperation.LESS_THAN, Datatype.DATE));
    return cmsb;
  }
}
