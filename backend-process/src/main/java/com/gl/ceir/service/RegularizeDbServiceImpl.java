package com.gl.ceir.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.configuration.PropertiesReader;
import com.gl.ceir.constant.Datatype;
import com.gl.ceir.constant.ReferTable;
import com.gl.ceir.constant.SearchOperation;
import com.gl.ceir.entity.EndUserDB;
import com.gl.ceir.entity.RegularizeDeviceDb;
import com.gl.ceir.notifier.NotifierWrapper;
import com.gl.ceir.pojo.RawMail;
import com.gl.ceir.pojo.SearchCriteria;
import com.gl.ceir.pojo.UserWiseMailCount;
import com.gl.ceir.repo.RegularizedDeviceDbRepository;
import com.gl.ceir.specification.GenericSpecificationBuilder;
import com.gl.ceir.util.Utility;

@Service
public class RegularizeDbServiceImpl {

	private static final Logger logger = LogManager.getLogger(RegularizeDbServiceImpl.class);

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	Utility utility;

	@Autowired
	RegularizedDeviceDbRepository regularizedDeviceDbRepository;
	
	@Autowired
	NotifierWrapper notifierWrapper;

	public List<RegularizeDeviceDb> getDevicesbyTaxStatusAndDateAndReminderFlag( String toDate, int taxPaidStatus,String reminderFlag){
		return regularizedDeviceDbRepository.findAll(buildSpecification(toDate, taxPaidStatus,reminderFlag).build());
	}

	public void saveAllDevices(List<RegularizeDeviceDb> regularizeDeviceDbs) {
		regularizedDeviceDbRepository.saveAll(regularizeDeviceDbs);
	}

	public GenericSpecificationBuilder<RegularizeDeviceDb> buildSpecification(String toDate, int taxPaidStatus,String reminderFlag){
		GenericSpecificationBuilder<RegularizeDeviceDb> cmsb = new GenericSpecificationBuilder<>(propertiesReader.dialect);

		// cmsb.with(new SearchCriteria("createdOn", fromDate, SearchOperation.GREATER_THAN_OR_EQUAL, Datatype.DATE));
		cmsb.with(new SearchCriteria("createdOn", toDate, SearchOperation.LESS_THAN, Datatype.DATE));
		cmsb.with(new SearchCriteria("taxPaidStatus", taxPaidStatus, SearchOperation.EQUALITY, Datatype.STRING));
		cmsb.with(new SearchCriteria("reminderFlag", reminderFlag, SearchOperation.EQUALITY, Datatype.STRING));

		return cmsb;
	}

	public List<UserWiseMailCount> getUserWiseMailCountDto(List<RegularizeDeviceDb> regularizeDeviceDbs) {
		HashMap<String, UserWiseMailCount> userWiseMailCountMap = new HashMap<>();
		try {
			EndUserDB endUserDB = null;
			Map<String, String> placeholderMap = null;
			UserWiseMailCount userWiseMailCount = null;

			for(RegularizeDeviceDb regularizeDeviceDb : regularizeDeviceDbs) {
				endUserDB = regularizeDeviceDb.getEndUserDB();
				if(Objects.isNull(userWiseMailCountMap.get(regularizeDeviceDb.getNid()))){
					userWiseMailCount = new UserWiseMailCount();
					userWiseMailCount.setUserId(endUserDB.getId());
					userWiseMailCount.setDeviceCount(1);
					userWiseMailCount.setTxnId(regularizeDeviceDb.getTxnId());
					userWiseMailCount.setPhoneNo(endUserDB.getPhoneNo());
					userWiseMailCount.setFirstImei(regularizeDeviceDb.getFirstImei());
					userWiseMailCount.setSecondImei(regularizeDeviceDb.getSecondImei());
					userWiseMailCount.setThirdImei(regularizeDeviceDb.getThirdImei());
					userWiseMailCount.setFourthImei(regularizeDeviceDb.getFourthImei());
					
					placeholderMap = userWiseMailCount.getPlaceholderMap();
					placeholderMap.put("<First name>", endUserDB.getFirstName());
					placeholderMap.put("<date>", regularizeDeviceDb.getCreatedOn().toString().substring(0, 10));
					placeholderMap.put("<count>", Integer.toString(userWiseMailCount.getDeviceCount()));

					userWiseMailCountMap.put(regularizeDeviceDb.getNid(), userWiseMailCount);
				}else {
					userWiseMailCount = userWiseMailCountMap.get(regularizeDeviceDb.getNid());
					userWiseMailCount.setDeviceCount(userWiseMailCount.getDeviceCount() + 1);
					placeholderMap = userWiseMailCount.getPlaceholderMap();
					placeholderMap.put("<count>", Integer.toString(userWiseMailCount.getDeviceCount()));
				}
			}

			List<UserWiseMailCount> userWiseMailCounts = new ArrayList<>(userWiseMailCountMap.values());
			logger.debug(userWiseMailCounts);

			return userWiseMailCounts;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<>(1);
		}
	}

	public void sendNotification(List<RegularizeDeviceDb> regularizeDeviceDbs, String tag, String subFeature) {
		List<UserWiseMailCount> userWiseMailCounts = getUserWiseMailCountDto(regularizeDeviceDbs);
		List<RawMail> rawMails = new ArrayList<>();

		for(UserWiseMailCount userWiseMailCount : userWiseMailCounts) {
			rawMails.add(new RawMail("", 
					tag, 
					userWiseMailCount.getUserId(),
					0L, // Feature Id 
					"System Process", 
					subFeature, 
					userWiseMailCount.getTxnId(),
					"", // Subject 
					userWiseMailCount.getPlaceholderMap(),  
					ReferTable.END_USER, 
					"End User",
					"End User"));
		}

		notifierWrapper.saveNotification(rawMails);
		
		logger.info("No. of notification sent is [" + userWiseMailCounts.size() + "]");
	}

}
