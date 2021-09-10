package com.ceir.SLAModule.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ceir.SLAModule.App;
import com.ceir.SLAModule.entity.SlaReport;
import com.ceir.SLAModule.entity.StatesInterpretationDb;
import com.ceir.SLAModule.entity.SystemConfigurationDb;
import com.ceir.SLAModule.entity.User;
import com.ceir.SLAModule.repoService.SlaRepoService;
import com.ceir.SLAModule.repoService.StateInterupRepoService;
import com.ceir.SLAModule.repoService.SystemConfigRepoService;
import com.ceir.SLAModule.repoService.UserRepoService;
import com.ceir.SLAModule.util.Utility;

@Service
public class UserService {

	

	@Autowired
	UserRepoService userRepo;

	@Autowired
	Utility utility;

	@Autowired
	SystemConfigRepoService systemConfigRepoService;


	@Autowired
	SlaRepoService slaRepoService;

	@Autowired
	StateInterupRepoService stateInterupRepoService;

	@Value("${user.days}")
	String tag;

	@Value("${user.featureId}")
	Integer featureId;

	private final static Logger log =Logger.getLogger(App.class);
	public void userProcess(int status) {
		log.info("inside user sla process");
		log.info("now going to fetch user by status: "+status);
		List<User> userData=new ArrayList<User>();
		try {
			userData=userRepo.fetchUsersByStatus(status);
		}
		catch(Exception e) {
			log.info(e.toString());
			log.info(e.getMessage());
		}
		if(userData.isEmpty()==false) {
			log.info("User data is available and total data count is: "+userData.size());
			String currentDate=utility.currentDate();
			log.info("currentDate is "+currentDate);
			Iterator<User> userIterator=userData.iterator();
			log.info("now going to find number of days for approval of user by  ceir");
			SystemConfigurationDb systemconfig=systemConfigRepoService.getByTag(tag);
			List<SlaReport> slaData=new ArrayList<SlaReport>();
			if(systemconfig!=null) {
				long days=Long.parseLong(systemconfig.getValue());
				log.info("number of days for approval of user by  ceir is "+days);
				log.info("now going to fetch state interup value ");
				StatesInterpretationDb stateInterup=stateInterupRepoService.getByFeatureIdAndState(featureId,status);
			    String stateInterupValue=new String();
				if(stateInterup.getInterp()!=null) {
					stateInterupValue=stateInterup.getInterp();
			    }
				log.info("state interp value is: "+stateInterupValue);
				while(userIterator.hasNext()) {
					User user=userIterator.next();
					String modifiedDate=utility.convertlocalTimeToString(user.getModifiedOn());
					log.info("user modified date= "+modifiedDate);
					if(modifiedDate!=null) {
						long dayDifferece=utility.getDifferenceDays(modifiedDate, currentDate);
						log.info("difference between current date and user modified date= "+dayDifferece);
						if(dayDifferece>days) {
							log.info("if difference greater than number of days for approval, so this data should be added in sla_report table");
							SlaReport  slaRepo=new SlaReport(featureId,status,stateInterupValue,user,
									user.getUsername(),user.getUsername(),user.getUsertype().getId());
							slaData.add(slaRepo);
						}
						else {
							log.info("if difference less than number of days for approval, so this data will not add in sla_report table ");	
						}
					}
					else {
						log.info("user modified date is empty");
					}
				}
				if(slaData.isEmpty()==false) {
					log.info("now going to save SLA Data");
					List<SlaReport> output=slaRepoService.saveSLA(slaData);	
					if(output.isEmpty()==false) {
						log.info("sla report sucessfully save");
					}
					else {
						log.info("sla report failed to save");
					}
				}
			}
			else {
				log.info("data failed to find by "+tag+" tag in system_configuration_db table");
			}

		}
		else {
			log.info("user data failed to find when status is pending approval for custom");
		}
		log.info("exit from user sla process");  
	}
}
