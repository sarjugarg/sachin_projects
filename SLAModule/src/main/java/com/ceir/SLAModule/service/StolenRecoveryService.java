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
import com.ceir.SLAModule.entity.StolenandRecoveryMgmt;
import com.ceir.SLAModule.entity.SystemConfigurationDb;
import com.ceir.SLAModule.entity.User;
import com.ceir.SLAModule.repoService.SlaRepoService;
import com.ceir.SLAModule.repoService.StateInterupRepoService;
import com.ceir.SLAModule.repoService.StolenRecRepoService;
import com.ceir.SLAModule.repoService.SystemConfigRepoService;
import com.ceir.SLAModule.repoService.TypeAppoveRepoService;
import com.ceir.SLAModule.repoService.UserRepoService;
import com.ceir.SLAModule.util.Utility;

@Service
public class StolenRecoveryService {


	@Autowired
	TypeAppoveRepoService typeApprovedRepo;
	
	
	@Autowired
	StolenRecRepoService stolenRepo;

	@Autowired
	Utility utility;

	@Autowired
	SystemConfigRepoService systemConfigRepoService;


	@Autowired
	SlaRepoService slaRepoService;

	@Autowired
	StateInterupRepoService stateInterupRepoService;

	

	
	@Autowired
	UserRepoService userRepo;

	private final static Logger log =Logger.getLogger(App.class);
//   	Stolen(0, "Stolen"), Recovery(1, "Recovery"), Block(2, "Block"),Unblock(3, "Unblock");
	public void stolenRecProcess(int state,String requestType,int featureId,Integer requestTypeId,String tag) {
		log.info("inside "+requestType+"   process");
		log.info("now going to fetch type "+requestType+" by status: "+state + " and requestTypeId: " + requestTypeId);
		List<StolenandRecoveryMgmt> data=new ArrayList<StolenandRecoveryMgmt>();
		try {
			data=stolenRepo.fetchStolenByStatusAndReqType(state, requestTypeId);
		}
		catch(Exception e) {
			log.info(e.toString());
			log.info(e.getMessage());
		}
		if(data.isEmpty()==false) {
			log.info(" "+requestType+" data is available and total data count is: "+data.size());
			String currentDate=utility.currentDate();
			log.info("currentDate is "+currentDate);
			Iterator<StolenandRecoveryMgmt> dataIterator=data.iterator();
			log.info("now going to find number of days for "+requestType+" of  approving by  ceir");
//			log.info("number of days for approval of consign by  ceir is "+consignDays);
			SystemConfigurationDb systemconfig=systemConfigRepoService.getByTag(tag);
			List<SlaReport> slaData=new ArrayList<SlaReport>();
			if(systemconfig!=null) {
				long days=Long.parseLong(systemconfig.getValue());
				log.info("number of days for approval of "+requestType+" by  ceir is "+days);
				log.info("now going to fetch state interup value for FeatureId " + featureId);
				List<StatesInterpretationDb> stateInterup=stateInterupRepoService.getByFeatureID(featureId);
			    String stateInterupValue=new String();
			    for(StatesInterpretationDb statedata:stateInterup) {
			    	if(statedata.getInterp()!=null) {
			    		if(statedata.getState()==state)
						stateInterupValue=statedata.getInterp();
				    }
			    }
				
				log.info("state interp value is: "+stateInterupValue);
				while(dataIterator.hasNext()) {
					StolenandRecoveryMgmt tac=dataIterator.next();
					String modifiedDate=utility.convertlocalTimeToString(tac.getModifiedOn());
					log.info(""+requestType+" modified date= "+modifiedDate);
					if(modifiedDate!=null) {
						long dayDifferece=utility.getDifferenceDays(modifiedDate, currentDate);
						log.info("difference between current date and "+requestType+" modified date= "+dayDifferece);
						log.info("going to fetch user data by userid: "+tac.getUserId());
						//User user=userRepo.getByUserId(tac.getUserId());
						if(dayDifferece>days) {
							log.info("if difference greater than number of days for approval, so this data should be added in sla_report table");
							//if(user.getId()!=0) {
								SlaReport  slaRepo=new SlaReport(featureId,state,stateInterupValue,tac.getUser(),
										tac.getTxnId(),tac.getUser().getUsername(),tac.getUser().getUsertype().getId());
								slaData.add(slaRepo);	
//							}
//							else {
//							log.info("user data is not found for this "+requestType+" id: "+tac.getId());	
//							}
							
						}
						else {
							log.info("if difference less than number of days for approval, so this data will not add in sla_report table ");	
						}
					}
					else {
						log.info(""+requestType+" modified date is empty");
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
			log.info(""+requestType+" data failed to find when status is pending approval for custom");
		}
		log.info("exit from "+requestType+" sla process");  
	}
}





