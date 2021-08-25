package com.ceir.BlackListProcess.process;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.BlackListProcess.model.BlackList;
import com.ceir.BlackListProcess.model.BlacklistDbHistory;
import com.ceir.BlackListProcess.model.GreylistDb;
import com.ceir.BlackListProcess.model.GreylistDbHistory; 
import com.ceir.BlackListProcess.model.SystemConfigurationDb;
import com.ceir.BlackListProcess.model.constants.GreyListOperation;
import com.ceir.BlackListProcess.repoimpl.BlackListRepoImpl;
import com.ceir.BlackListProcess.repoimpl.ConfigurationManagementServiceImpl;
import com.ceir.BlackListProcess.repoimpl.ListFileDetailsImpl;
import com.ceir.BlackListProcess.repoimpl.NationalislmServiceImpl;
import com.ceir.BlackListProcess.repoimpl.WebActionRepoImpl;
import com.ceir.BlackListProcess.util.Utility;

@Service
public class BlackListProcess {

	@Autowired
	ListFileDetailsImpl listFileDetailsImpl;

	@Autowired
	Utility utility;

	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;

	@Autowired
	NationalislmServiceImpl nationalislmServiceImpl;

	@Autowired
	BlackListRepoImpl blackListRepoImpl;

	@Autowired
	WebActionRepoImpl webActionRepoImpl;
	
	private final Logger log =Logger.getLogger(getClass());
    @Transactional
	public void blackListProcess() {
		log.info("inside blacklistPro");
		//boolean checkStolenStatus=webActionRepoImpl.checkFeatureExist("Stolen");
		//if(checkStolenStatus==false) {
		SystemConfigurationDb greyToBlackListPeriodInDay=new SystemConfigurationDb();
		SystemConfigurationDb sysConfigDb=new SystemConfigurationDb();
		Date currentDdate=utility.currentOnlyDate();
		//log.info("current Date:  "+currentDdate);
		sysConfigDb.setTag("GREY_TO_BLACK_MOVE_PERIOD_IN_DAY");
		//log.info("now going to check GREY TO BLACK MOVE PERIOD IN DAY");
	//	greyToBlackListPeriodInDay=configurationManagementServiceImpl.findByTag(sysConfigDb);
		//log.info("GREY TO BLACK MOVE PERIOD IN DAY: "+greyToBlackListPeriodInDay.getValue());
		//Integer days=Integer.parseInt(greyToBlackListPeriodInDay.getValue());
		List<GreylistDb> greyListData=new ArrayList<GreylistDb>();
		log.info("going to fetch grey list data:");
		greyListData=nationalislmServiceImpl.findAllGreyListData();
                log.info("Record No :"+ greyListData.size());
		if(greyListData.isEmpty()==false) {
			greyListProcess(greyListData);
		}
		else {
			log.info("grey list data not found so process cannot go further");
		}
		//}
		//}
		/*
		 * } else {
		 * log.info("stolen data exist in web action db :  "+checkStolenStatus); }
		 */
		log.info("exit from blackList process");
	}
    
	public void greyListProcess(List<GreylistDb> greyListData) {
		log.info("if greylist data found");
		for(GreylistDb greyListDb:greyListData) {
			Date currentDate=utility.currentOnlyDate();
			log.info("grace period expiry date for particular imei: "+greyListDb.getExpiryDate());
			log.info("current date:  "+currentDate); 
			if(greyListDb.getExpiryDate()!=null) {
			if(currentDate.after(greyListDb.getExpiryDate())) {
				log.info("if grace period for this imei is completed: "+greyListDb.getImei());	
				log.info("so move this grey list data to greylist history and blacklist table");
				GreylistDbHistory greyListHistory=new GreylistDbHistory(
//                                        new Date(),new Date(),
						greyListDb.getImei(), greyListDb.getRoleType(), greyListDb.getUserId(),
						greyListDb.getTxnId(), greyListDb.getDeviceNumber(), greyListDb.getDeviceType(),
						greyListDb.getDeviceAction(),greyListDb.getDeviceStatus(),greyListDb.getDeviceLaunchDate(),
						greyListDb.getMultipleSimStatus(), greyListDb.getDeviceIdType(),greyListDb.getImeiEsnMeid(),GreyListOperation.DELETE.getCode()
						,"Moved to BlackList",greyListDb.getModeType(),greyListDb.getRequestType(),
						greyListDb.getUserType(),greyListDb.getComplainType(),greyListDb.getExpiryDate() ,greyListDb.getOperator_id() , greyListDb.getOperator_name() , greyListDb.getActualImei() );
				GreylistDbHistory greylistDbHistory=nationalislmServiceImpl.saveGreyListHistory(greyListHistory);
                
				if(greylistDbHistory!=null) {
					log.info("now data is saved into GreylistDbHistory table");


						log.info("that imei record removed from grey_List_Db table");
					    
						BlackList blackList=new BlackList(greyListDb.getImei(),0l,greyListDb.getRoleType(),
								greyListDb.getUserId(),greyListDb.getTxnId(),greyListDb.getDeviceNumber(),greyListDb.getDeviceType(),
								greyListDb.getDeviceAction(),greyListDb.getDeviceStatus(),greyListDb.getDeviceLaunchDate(),
								greyListDb.getMultipleSimStatus(),greyListDb.getDeviceIdType(),greyListDb.getImeiEsnMeid(),
								greyListDb.getModeType(),greyListDb.getRequestType(),greyListDb.getUserType(),
								greyListDb.getComplainType(),greyListDb.getExpiryDate(), greyListDb.getOperator_id() , greyListDb.getOperator_name()  , greyListDb.getActualImei() );
					
						BlackList blackListOutput=blackListRepoImpl.saveBlackList(blackList);	
						if(blackListOutput!=null) {
							log.info("data saved into black_list table");
							BlacklistDbHistory blackListHistory=new BlacklistDbHistory(
									new Date(),new Date(),
									greyListDb.getImei(), greyListDb.getRoleType(), greyListDb.getUserId(),
									greyListDb.getDeviceNumber(), greyListDb.getDeviceType(),
									greyListDb.getDeviceAction(),greyListDb.getDeviceStatus(),greyListDb.getDeviceLaunchDate(),
									greyListDb.getMultipleSimStatus(), greyListDb.getDeviceIdType(),greyListDb.getImeiEsnMeid(),GreyListOperation.Add.getCode()
									,greyListDb.getModeType(),greyListDb.getRequestType(),greyListDb.getUserType(),
									greyListDb.getComplainType(),greyListDb.getExpiryDate() , greyListDb.getOperator_id() , greyListDb.getOperator_name()  , greyListDb.getActualImei());
							BlacklistDbHistory output=blackListRepoImpl.saveBlackListHistory(blackListHistory);	
							if(output!=null) {
								System.out.println("data saved into blacklist_db_history table");
							}
						}
						log.info("going to delete greyList table data for this imei");
						int deleteGreyListById=nationalislmServiceImpl.deleteGreyListById(greyListDb.getId());
						if(deleteGreyListById==1) {
							log.info("greylist data sucessfully delete ");
						}else {
							log.info("greylist data failed to delete");
						}
				}
			}
			else{
			}
			}
			else {
				System.out.println("expiry date for imei is null");	
			}
		}
	}
}





