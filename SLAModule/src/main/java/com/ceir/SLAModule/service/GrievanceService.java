package com.ceir.SLAModule.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ceir.SLAModule.App;
import com.ceir.SLAModule.entity.Grievance;
import com.ceir.SLAModule.entity.SlaReport;
import com.ceir.SLAModule.entity.StatesInterpretationDb;
import com.ceir.SLAModule.entity.SystemConfigurationDb;
import com.ceir.SLAModule.model.constants.GrievanceStatus;
import com.ceir.SLAModule.repoService.GrievanceRepoService;
import com.ceir.SLAModule.repoService.SlaRepoService;
import com.ceir.SLAModule.repoService.StateInterupRepoService;
import com.ceir.SLAModule.repoService.SystemConfigRepoService;
import com.ceir.SLAModule.util.Utility;

@Service
public class GrievanceService {

     @Autowired
     GrievanceRepoService grievanceRepo;

     @Autowired
     Utility utility;

     @Autowired
     SystemConfigRepoService systemConfigRepoService;

     @Value("${griev.days}")
     String greivanceDays;

     @Value("${griev.featureId}")
     Integer featureId;

     @Autowired
     StateInterupRepoService stateInterupRepoService;

     @Autowired
     SlaRepoService slaRepoService;
     private final static Logger log = Logger.getLogger(App.class);

     public void grievanceProcess(int status) {
          log.info("inside Greivance sla process");
          log.info("now going to fetch Greivance by status: " + status);
          List<Grievance> greivanceData = new ArrayList<Grievance>();
          try {
               greivanceData = grievanceRepo.fetchGrievanceByStatus(status);
          } catch (Exception e) {
               log.info(e.toString());
               log.info(e.getMessage());
          }
          if (greivanceData.isEmpty() == false) {

               log.info("Greivance data is available and total data count is: " + greivanceData.size());
               String currentDate = utility.currentDate();
               log.info("currentDate is " + currentDate);
               Iterator<Grievance> grievIterator = greivanceData.iterator();
               log.info("now going to find number of days for approval of Greivance by  ceir");
//			log.info("greivcane pending with admin tag name: "+greivanceDays);
               SystemConfigurationDb systemconfig = systemConfigRepoService.getByTag(greivanceDays);
               List<SlaReport> slaData = new ArrayList<SlaReport>();
               if (systemconfig != null) {
                    long days = Long.parseLong(systemconfig.getValue());
                    log.info("number of days for approval of Greivance by  ceir is " + days);
                    log.info("now going to fetch state interup value ");
                    StatesInterpretationDb stateInterup = stateInterupRepoService.getByFeatureIdAndState(featureId, GrievanceStatus.PENDING_WITH_ADMIN.getCode());
                    String stateInterupValue = new String();
                    if (stateInterup.getInterp() != null) {
                         stateInterupValue = stateInterup.getInterp();
                    }
                    log.info("state interp value is: " + stateInterupValue);
                    while (grievIterator.hasNext()) {
                         Grievance grievance = grievIterator.next();
                         String modifiedDate = utility.convertlocalTimeToString(grievance.getModifiedOn());
                         log.info("Greivance modified date= " + modifiedDate);
                         if (modifiedDate != null) {
                              long dayDifferece = utility.getDifferenceDays(modifiedDate, currentDate);
                              log.info("difference between current date and greivance modified date= " + dayDifferece);
                              if (dayDifferece > days) {

                                   if (grievance.getUser() != null) {
                                        log.info("if difference greater than number of days for approval, so this data should be added in sla_report table");
                                        log.info("Grievance ID:" + grievance.getGrievanceId()  + " && " +  grievance.getUser().getUsertype().getId() );
                                        SlaReport slaRepo = new SlaReport(featureId, GrievanceStatus.PENDING_WITH_ADMIN.getCode(), stateInterupValue, grievance.getUser(),
                                                grievance.getGrievanceId(), grievance.getUser().getUsername(), grievance.getUser().getUsertype().getId());
                                        slaData.add(slaRepo);
                                   } else {
                                        log.info("user data is not found for this grivance id: " + grievance.getGrievanceId());
                                   }
                              } else {
                                   log.info("if difference less than number of days for approval, so this data will not add in sla_report table ");
                              }
                         } else {
                              log.info("greivance modified date is empty");
                         }
                    }
                    if (slaData.isEmpty() == false) {
                         log.info("now going to save SLA Data");
                         List<SlaReport> output = slaRepoService.saveSLA(slaData);
                         if (output.isEmpty() == false) {
                              log.info("sla report sucessfully save");
                         } else {
                              log.info("sla report failed to save");
                         }
                    }
               } else {
                    log.info("data failed to find by " + greivanceDays + " tag in system_configuration_db table");
               }

          } else {
               log.info("greivance data failed to find when status is pending approval for custom");
          }
          log.info("exit from greivance sla process");
     }
}


