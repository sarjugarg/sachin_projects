package com.ceir.BlackListFileProcess.process;
import java.util.Date;
import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ceir.BlackListFileProcess.model.BlacklistDbHistory;
import com.ceir.BlackListFileProcess.model.FileDumpFilter;
import com.ceir.BlackListFileProcess.model.FileDumpMgmt;
import com.ceir.BlackListFileProcess.model.SystemConfigurationDb;
import com.ceir.BlackListFileProcess.model.constants.DumpType;
import com.ceir.BlackListFileProcess.model.constants.ServiceDump;
import com.ceir.BlackListFileProcess.repoServiceImpl.BlackListRepoImpl;
import com.ceir.BlackListFileProcess.repoServiceImpl.ListFileDetailsImpl;
import com.ceir.BlackListFileProcess.repoServiceImpl.SystemConfigurationDbRepoService;
import com.ceir.BlackListFileProcess.util.Utility;

@Service
public class IncrementalDumpProcess {
	@Autowired
	SystemConfigurationDbRepoService configurationManagementServiceImpl;

	@Autowired
	ListFileDetailsImpl listFileDetailsImpl;

	@Autowired 
	Utility utility;

	
	@Value("${dumpTypeInc}")
    String dumpTypeInc;
	
	@Value("${IncrDumpInDays}")
	String IncrDumpInDays;
	
	@Autowired
	BlackListRepoImpl blackListRepo;
	
	
	private final Logger log =Logger.getLogger(getClass());

	SystemConfigurationDb frequencyInDays=new SystemConfigurationDb();
	SystemConfigurationDb startWeekOfDay=new SystemConfigurationDb();
	String currentDate=new String();
	String yesterdayId=new String();
	String currentTime=new String();
	FileDumpMgmt topDataForIncdump=new FileDumpMgmt();

	public void incrementalDumpProcess(String filePath) {
			log.info("inside incremental dump process");
			topDataForIncdump=listFileDetailsImpl.topDataByDumpTypeAndServiceDump(dumpTypeInc,ServiceDump.BlackListService.getCode());	
			if(topDataForIncdump!=null) {
				incrementalDumpFileProcess(filePath);
			}
			else {
				yesterdayId=utility.getYesterdayId();
				currentTime=utility.getTxnId();
				FileDumpFilter filter=new FileDumpFilter();
				String fileName="BlackList_Incremental_"+yesterdayId+".csv";
				saveDataIntoFile(filter,fileName,filePath);	
			}
			//			startWeekOfDay=configurationManagementServiceImpl.findByTag(systemConfigurationDb);
			log.info("exit from incremental dump process");
	}

	public void incrementalDumpFileProcess(String filePath) {
		log.info("createdOn column value: "+topDataForIncdump.getCreatedOn());
//		Date dateSubtract=utility.subtractDays(topDataForIncdump.getCreatedOn(), 1);
	//	String configDate=utility.convertToDateformat(dateSubtract);
		Date configDate=topDataForIncdump.getCreatedOn();
		String compareDate=utility.convertToDateformat(configDate);
		//yesterdayDate=utility.getYesterdayDateString();
		currentDate=utility.currentDate();
			log.info("date from file dump table if dumpType is Incremental: "+configDate);
		if(!compareDate.equals(currentDate)) {
			log.info("if last day file not created");
			currentDate=utility.currentDate();
			log.info("currentDate:  "+currentDate);
			String configToDate=utility.convertToDateformat(configDate);
			long differenceOfDates=utility.getDifferenceDays(configToDate,currentDate);
			log.info("difference from currentDate and dump table date: "+differenceOfDates);
			frequencyInDays=configurationManagementServiceImpl.getDataByTag(IncrDumpInDays);
			log.info("total frequency In Days for incremental dump: "+ frequencyInDays.getValue().toString());
			//int daysUnit=Integer.parseInt(frequencyInDays.getValue());
			int days=1;
			//Date dateForConfig=topDataForIncdump.getCreatedOn();
			String DayAdded=new String();
			Date startDate=configDate;
			String endDate=new String();
			Integer frequencyDays=Integer.parseInt(frequencyInDays.getValue());
			while(differenceOfDates>=Long.parseLong(frequencyInDays.getValue())){
				log.info("if difference between dates greater than total days of frequency");
				currentTime=utility.getTxnId();
				log.info("day added + "+DayAdded);
				log.info("days: "+days);
                int daysSubtract=frequencyDays-1;
                endDate=utility.addDaysInDate(daysSubtract,startDate);
				log.info("day added date:  "+endDate);
				FileDumpFilter filter=new FileDumpFilter();
				filter.setStartDate(utility.convertToDateformat(startDate));
				filter.setEndDate(endDate);
				yesterdayId=utility.getYesterdayId();
				log.info("fetch data from black list db between dates : "+startDate +"to "+ startDate);
				String fileName="BlackList_Incremental_"+utility.convertToDate(startDate)+".csv";
				log.info("file path and name is:  "+fileName);
				saveDataIntoFile(filter,fileName,filePath);
				startDate=utility.stringToDate(endDate);
				DayAdded=utility.addDaysInDate(days,startDate);
				startDate=utility.stringToDate(DayAdded);
				differenceOfDates=differenceOfDates-frequencyDays;
				log.info("differenceOfDates after file created: "+differenceOfDates);
			} 
		}
	}
   
   
	public void saveDataIntoFile(FileDumpFilter fileDataFilter,String fileName,String filePath) {
		String header=new String();
		log.info("going to fetch data from Black list history db for date"+fileDataFilter);
		List<BlacklistDbHistory> blackListData=blackListRepo.blackListHistoryDataByExpiryDate(fileDataFilter);
		log.info("BlackListData:" +blackListData);
		if(!blackListData.isEmpty()) {
			log.info("if Black list history table is not empty");
			header="Device Id,Operation";
			utility.writeBlackListHistoryInFile(filePath+fileName, header, blackListData);        
		}
		else {
			log.info("if Black list history table is empty");
			header="Message";
			String record="No data available in BlackList.";
			utility.writeInFile(filePath+fileName, header, record);	
		}
		FileDumpMgmt fileDumpMgmt=new FileDumpMgmt(new Date(),new Date(), fileName, DumpType.INCREMENTAL.getCode(),ServiceDump.BlackListService.getCode(), dumpTypeInc);
		listFileDetailsImpl.saveFileDumpMgmt(fileDumpMgmt);
	}
}




