package com.gl.ceir.factory.service.impl;

import com.gl.ceir.constant.Datatype;
import com.gl.ceir.constant.SearchOperation;
import com.gl.ceir.entity.EndUserDB;
import com.gl.ceir.entity.SystemConfigurationDb;
import com.gl.ceir.entity.VisaDb;
import com.gl.ceir.factory.service.BaseService;
import com.gl.ceir.factory.service.transaction.VisaExpireTransaction;
import com.gl.ceir.pojo.SearchCriteria;
import com.gl.ceir.repo.EndUserDbRepository;
import com.gl.ceir.repo.VisaDbRepository;
import com.gl.ceir.specification.GenericSpecificationBuilder;
import com.gl.ceir.util.DateUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VisaExpire extends BaseService {
  private static final Logger logger = LogManager.getLogger(VisaExpire.class);
  
  @Autowired
  EndUserDbRepository endUserDbRepository;
  
  @Autowired
  VisaDbRepository visaDbRepository;
  
  @Autowired
  VisaExpireTransaction visaExpireTransaction;
  
  public void fetch() {
    try {
      SystemConfigurationDb reminderDays = this.systemConfigurationDbRepository.getByTag("reminder_days_before");
      if (Objects.isNull(reminderDays)) {
        Map<String, String> placeholderMap = new HashMap<>();
        placeholderMap.put("<tag>", "REMINDER_DAYS");
        placeholderMap.put("<process_name>", "VisaExpire");
        onErrorRaiseAnAlert("alert003", placeholderMap);
        logger.info("Alert [ALERT_003] is raised. So, doing nothing.");
        return;
      } 
      String fromDate = DateUtil.nextDate(Integer.parseInt(reminderDays.getValue()) * -1);
      String toDate = DateUtil.nextDate((Integer.parseInt(reminderDays.getValue()) - 1) * -1);
      logger.info("fromDate[" + fromDate + "] toDate[" + toDate + "]");
      List<VisaDb> visaDbs = this.visaDbRepository.findAll(buildSpecification(fromDate, toDate).build());
      if (visaDbs.isEmpty()) {
        logger.info("No visa found to be updated today [" + DateUtil.nextDate(0) + "] ");
        return;
      } 
      for (VisaDb visaDb : visaDbs)
        process(visaDb); 
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
    VisaDb visaDb = (VisaDb)o;
    EndUserDB endUserDB = visaDb.getEndUserDB();
    try {
      if (Objects.isNull(endUserDB)) {
        logger.info("No end user is found associated with visa of id [" + visaDb.getId() + "]");
      } else {
        this.visaExpireTransaction.performTransaction(endUserDB.getNid());
        logger.info("End user is deleted successfully [" + endUserDB.getNid() + "]");
      } 
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } 
  }
  
  private GenericSpecificationBuilder<VisaDb> buildSpecification(String fromDate, String toDate) {
    GenericSpecificationBuilder<VisaDb> cmsb = new GenericSpecificationBuilder(this.propertiesReader.dialect);
    cmsb.with(new SearchCriteria("visaExpiryDate", toDate, SearchOperation.LESS_THAN, Datatype.DATE));
    return cmsb;
  }
}
