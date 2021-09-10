package com.ceir.SLAModule.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ceir.SLAModule.App;
import com.ceir.SLAModule.entity.ConsignmentMgmt;
import com.ceir.SLAModule.entity.SlaReport;
import com.ceir.SLAModule.entity.StatesInterpretationDb;
import com.ceir.SLAModule.entity.SystemConfigurationDb;
import com.ceir.SLAModule.model.constants.ConsignmentStatus;
import com.ceir.SLAModule.model.constants.GrievanceStatus;
import com.ceir.SLAModule.repo.ConsignmentRepo;
import com.ceir.SLAModule.repoService.ConsignmentRepoService;
import com.ceir.SLAModule.repoService.SlaRepoService;
import com.ceir.SLAModule.repoService.StateInterupRepoService;
import com.ceir.SLAModule.repoService.SystemConfigRepoService;
import com.ceir.SLAModule.util.Utility;

@Service
public class ConsignmentService {

	@Autowired
	ConsignmentRepoService consignRepo;

	@Autowired
	Utility utility;

	@Autowired
	SystemConfigRepoService systemConfigRepoService;

	@Autowired
	ConsignmentRepo consignRepository;

	@Autowired
	SlaRepoService slaRepoService;

	@Autowired
	StateInterupRepoService stateInterupRepoService;

	@Value("${consign.days}")
	String consignDays;

	@Value("${consign.featureId}")
	Integer featureId;

	private final static Logger log =Logger.getLogger(App.class);
	public void consignmentProcess(int status) {
		log.info("inside consignment sla process");
		log.info("now going to fetch consignment by status: "+status);
		List<ConsignmentMgmt> consignData=new ArrayList<ConsignmentMgmt>();
		try {
			consignData=consignRepo.fetchConsignmentByStatus(status);
		}
		catch(Exception e) {
			log.info(e.toString());
			log.info(e.getMessage());
		}
		if(consignData.isEmpty()==false) {
			log.info("consignment data is available and total data count is: "+consignData.size());
			String currentDate=utility.currentDate();
			log.info("currentDate is "+currentDate);
			Iterator<ConsignmentMgmt> consignIterator=consignData.iterator();
			log.info("now going to find number of days for approval of consignment by  ceir");
//			log.info("number of days for approval of consign by  ceir is "+consignDays);
			SystemConfigurationDb systemconfig=systemConfigRepoService.getByTag(consignDays);
			List<SlaReport> slaData=new ArrayList<SlaReport>();
			if(systemconfig!=null) {
				long days=Long.parseLong(systemconfig.getValue());
				log.info("number of days for approval of consignment by  ceir is "+days);
				log.info("now going to fetch state interup value ");
				StatesInterpretationDb stateInterup=stateInterupRepoService.getByFeatureIdAndState(featureId, ConsignmentStatus.PENDING_APPROVAL_FROM_CEIR_AUTHORITY.getCode());
			    String stateInterupValue=new String();
				if(stateInterup.getInterp()!=null) {
					stateInterupValue=stateInterup.getInterp();
			    }
				log.info("state interp value is: "+stateInterupValue);
				while(consignIterator.hasNext()) {
					ConsignmentMgmt consign=consignIterator.next();
					String modifiedDate=utility.convertlocalTimeToString(consign.getModifiedOn());
					log.info("consignment modified date= "+modifiedDate);
					if(modifiedDate!=null) {
						long dayDifferece=utility.getDifferenceDays(modifiedDate, currentDate);
						log.info("difference between current date and consignment modified date= "+dayDifferece);
						if(dayDifferece>days) {
							log.info("if difference greater than number of days for approval, so this data should be added in sla_report table");
							if(consign.getUser()!=null) {
								log.info("if difference greater than number of days for approval, so this data should be added in sla_report table");
								SlaReport  slaRepo=new SlaReport(featureId,GrievanceStatus.PENDING_WITH_ADMIN.getCode(),stateInterupValue,consign.getUser(),
										consign.getTxnId(),consign.getUser().getUsername(),consign.getUser().getUsertype().getId());
								slaData.add(slaRepo);	
							}
							else {
							log.info("user data is not found for this consignment id: "+consign.getId());	
							}
						}
						else {
							log.info("if difference less than number of days for approval, so this data will not add in sla_report table ");	
						}
					}
					else {
						log.info("consignment modified date is empty");
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
				log.info("data failed to find by "+consignDays+" tag in system_configuration_db table");
			}

		}
		else {
			log.info("consignment data failed to find when status is pending approval for custom");
		}
		log.info("exit from consignment sla process");  
	}
}
