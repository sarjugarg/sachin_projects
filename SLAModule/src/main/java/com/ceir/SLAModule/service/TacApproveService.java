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
import com.ceir.SLAModule.entity.TypeApprovedTAC;
import com.ceir.SLAModule.entity.User;
import com.ceir.SLAModule.repoService.SlaRepoService;
import com.ceir.SLAModule.repoService.StateInterupRepoService;
import com.ceir.SLAModule.repoService.SystemConfigRepoService;
import com.ceir.SLAModule.repoService.TypeAppoveRepoService;
import com.ceir.SLAModule.repoService.UserRepoService;
import com.ceir.SLAModule.util.Utility;

@Service
public class TacApproveService {

	@Autowired
	TypeAppoveRepoService typeApprovedRepo;
	
	
	@Autowired
	TypeAppoveRepoService tacRepo;

	@Autowired
	Utility utility;

	@Autowired
	SystemConfigRepoService systemConfigRepoService;


	@Autowired
	SlaRepoService slaRepoService;

	@Autowired
	StateInterupRepoService stateInterupRepoService;

	@Value("${tac.days}")
	String tacDays;

	@Value("${tac.featureId}")
	Integer featureId;
	
	@Autowired
	UserRepoService userRepo;

	private final static Logger log =Logger.getLogger(App.class);
	public void tacProcess(int status) {
		log.info("inside type approved tac  process");
		log.info("now going to fetch type approved tac by status: "+status);
		List<TypeApprovedTAC> tacData=new ArrayList<TypeApprovedTAC>();
		try {
			tacData=tacRepo.fetchTacByStatus(status);
		}
		catch(Exception e) {
			log.info(e.toString());
			log.info(e.getMessage());
		}
		if(tacData.isEmpty()==false) {
			log.info("type approved tac data is available and total data count is: "+tacData.size());
			String currentDate=utility.currentDate();
			log.info("currentDate is "+currentDate);
			Iterator<TypeApprovedTAC> tacIterator=tacData.iterator();
			log.info("now going to find number of days for approval of type approved tac by  ceir");
			SystemConfigurationDb systemconfig=systemConfigRepoService.getByTag(tacDays);
			List<SlaReport> slaData=new ArrayList<SlaReport>();
			if(systemconfig!=null) {
				long days=Long.parseLong(systemconfig.getValue());
				log.info("number of days for approval of type approved tac by  ceir is "+days);
				log.info("now going to fetch state interup value ");
				List<StatesInterpretationDb> stateInterup=stateInterupRepoService.getByFeatureID(featureId);
			    String stateInterupValue=new String();
			    for(StatesInterpretationDb statedata:stateInterup) {
			    	if(statedata.getInterp()!=null) {
			    		if(statedata.getState()==status)
						stateInterupValue=statedata.getInterp();
				    }
			    }
				log.info("state interp value is: "+stateInterupValue);
				while(tacIterator.hasNext()) {
					TypeApprovedTAC tac=tacIterator.next();
					String modifiedDate=utility.convertlocalTimeToString(tac.getModifiedOn());
					log.info("type approved tac modified date= "+modifiedDate);
					if(modifiedDate!=null) {
						long dayDifferece=utility.getDifferenceDays(modifiedDate, currentDate);
						log.info("difference between current date and type approved tac modified date= "+dayDifferece);
						log.info("going to fetch user data by userid: "+tac.getUserId());
						//User user=userRepo.getByUserId(tac.getUserId());
						if(dayDifferece>days) {
							log.info("if difference greater than number of days for approval, so this data should be added in sla_report table");
							//if(user.getId()!=0) {
								SlaReport  slaRepo=new SlaReport(featureId,status,stateInterupValue,tac.getUser(),
										tac.getTxnId(),tac.getUser().getUsername(),tac.getUser().getUsertype().getId());
								slaData.add(slaRepo);	
//							}
//							else {
//							log.info("user data is not found for this user id: "+tac.getUserId());	
//							}
							
						}
						else {
							log.info("if difference less than number of days for approval, so this data will not add in sla_report table ");	
						}
					}
					else {
						log.info("type approved tac modified date is empty");
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
				log.info("data failed to find by "+tacDays+" tag in system_configuration_db table");
			}

		}
		else {
			log.info("type approved tac data failed to find when status is pending approval for custom");
		}
		log.info("exit from type approved tac data sla process");  
	}
}

