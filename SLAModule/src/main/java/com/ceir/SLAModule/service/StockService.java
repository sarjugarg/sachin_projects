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
import com.ceir.SLAModule.entity.StockMgmt;
import com.ceir.SLAModule.entity.SystemConfigurationDb;
import com.ceir.SLAModule.model.constants.GrievanceStatus;
import com.ceir.SLAModule.model.constants.StockStatus;
import com.ceir.SLAModule.repoService.SlaRepoService;
import com.ceir.SLAModule.repoService.StateInterupRepoService;
import com.ceir.SLAModule.repoService.StockRepoService;
import com.ceir.SLAModule.repoService.SystemConfigRepoService;
import com.ceir.SLAModule.util.Utility;

@Service
public class StockService {

	@Autowired
	StockRepoService stockRepo;

	@Autowired
	Utility utility;

	@Autowired
	SystemConfigRepoService systemConfigRepoService;

	@Value("${stock.days}")
	String stockDays;
	
	@Value("${stock.featureId}")
	Integer featureId;
	
	@Autowired
	StateInterupRepoService stateInterupRepoService;
	
	
	@Autowired
	SlaRepoService slaRepoService;
	private final static Logger log =Logger.getLogger(App.class);
	public void stockProcess(int status) {
		log.info("inside Stock sla process");
		log.info("now going to fetch Stock by status: "+status);
		List<StockMgmt> stockData=new ArrayList<StockMgmt>();
		try {
			stockData=stockRepo.stockDataByStatus(status);
		}
		catch(Exception e) {
			log.info(e.toString());
			log.info(e.getMessage());
		}
		if(stockData.isEmpty()==false) {
			log.info("Stock data is available and total data count is: "+stockData.size());
			String currentDate=utility.currentDate();
			log.info("currentDate is "+currentDate);
			Iterator<StockMgmt> stockIterator=stockData.iterator();
			log.info("now going to find number of days for approval of Stock by  ceir");
			SystemConfigurationDb systemconfig=systemConfigRepoService.getByTag(stockDays);
			List<SlaReport> slaData=new ArrayList<SlaReport>();
			if(systemconfig!=null) {
				long days=Long.parseLong(systemconfig.getValue());
                log.info("number of days for approval of Stock by  ceir is "+days);
				log.info("now going to fetch state interup value ");
                StatesInterpretationDb stateInterup=stateInterupRepoService.getByFeatureIdAndState(featureId, StockStatus.SUCCESS.getCode());
			    String stateInterupValue=new String();
				if(stateInterup.getInterp()!=null) {
					stateInterupValue=stateInterup.getInterp();
			    }
				log.info("state interp value is: "+stateInterupValue);
				while(stockIterator.hasNext()) {
					StockMgmt stock=stockIterator.next();
					String modifiedDate=utility.convertlocalTimeToString(stock.getModifiedOn());
					log.info("Stock modified date= "+modifiedDate);
					if(modifiedDate!=null) {
						long dayDifferece=utility.getDifferenceDays(modifiedDate, currentDate);
						log.info("difference between current date and Stock modified date= "+dayDifferece);
						if(dayDifferece>days) {
							log.info("if difference greater than number of days for approval, so this data should be added in sla_report table");
							if(stock.getUser()!=null) {
								log.info("if difference greater than number of days for approval, so this data should be added in sla_report table");
								SlaReport  slaRepo=new SlaReport(featureId,GrievanceStatus.PENDING_WITH_ADMIN.getCode(),stateInterupValue,stock.getUser(),
										stock.getTxnId(),stock.getUser().getUsername(),stock.getUser().getUsertype().getId());
								slaData.add(slaRepo);	
							}
							else {
							log.info("user data is not found for this stock id: "+stock.getId());	
							}
						}
						else {
							log.info("if difference less than number of days for approval, so this data will not add in sla_report table ");	

						}
					}
					else {
						log.info("Stock modified date is empty");
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
				log.info("data failed to find by "+stockDays+" tag in system_configuration_db table");
			}

		}
		else {
			log.info("Stock data failed to find when status is pending approval for custom");
		}
		log.info("exit from Stock sla process");  
	}
	
}
