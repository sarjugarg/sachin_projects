package com.ceir.GreyListProcess.process;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ceir.GreyListProcess.model.FileDumpFilter;
import com.ceir.GreyListProcess.model.FileDumpMgmt;
import com.ceir.GreyListProcess.model.GreylistDbHistory;
import com.ceir.GreyListProcess.model.SystemConfigurationDb;
import com.ceir.GreyListProcess.model.constants.DumpType;
import com.ceir.GreyListProcess.model.constants.ServiceDump;
import com.ceir.GreyListProcess.repositoryImpl.ConfigurationManagementServiceImpl;
import com.ceir.GreyListProcess.repositoryImpl.ListFileDetailsImpl;
import com.ceir.GreyListProcess.repositoryImpl.NationalislmServiceImpl;
import com.ceir.GreyListProcess.util.Utility;
@Service
public class IncrementalDumpProcess {
	@Autowired
	ConfigurationManagementServiceImpl configurationManagementServiceImpl;

	@Autowired
	ListFileDetailsImpl listFileDetailsImpl;

	@Autowired 
	Utility utility;

	@Autowired
	NationalislmServiceImpl nationalislmServiceImpl;
	
	@Value("${dumpTypeInc}")
    String dumpTypeInc;
	
	@Value("${IncrDumpInDays}")
	String IncrDumpInDays;
	
	
	private final Logger log =Logger.getLogger(getClass());

	SystemConfigurationDb systemConfigurationDb=new SystemConfigurationDb();
	SystemConfigurationDb frequencyInDays=new SystemConfigurationDb();
	SystemConfigurationDb startWeekOfDay=new SystemConfigurationDb();
	String currentDate=new String();
	String yesterdayId=new String();
	String currentTime=new String();
	FileDumpMgmt topDataForIncdump=new FileDumpMgmt();

	public void incrementalDumpProcess(String filePath) {
			log.info("inside incremental dump process");
			topDataForIncdump=listFileDetailsImpl.topDataByDumpTypeAndServiceDump(dumpTypeInc,ServiceDump.GreyListService.getCode());	
			if(topDataForIncdump!=null) {
				incrementalDumpFileProcess(filePath);
			}
			else {
				yesterdayId=utility.getYesterdayId();
				currentTime=utility.getTxnId();
				FileDumpFilter filter=new FileDumpFilter();
				String fileName="GreyList_Incremental_"+yesterdayId+".csv";
				saveDataIntoFile(filter,fileName,filePath);	
			}
			//			startWeekOfDay=configurationManagementServiceImpl.findByTag(systemConfigurationDb);
			log.info("exit from incremental dump process");
	}

//	public void incrementalDumpFileProcess(String filePath) {
//		log.info("createdOn column value: "+topDataForIncdump.getCreatedOn());
//		Date dateSubtract=utility.subtractDays(topDataForIncdump.getCreatedOn(), 1);
//		String configDate=utility.convertToDateformat(dateSubtract);
//		log.info("date from file dump table if dumpType is Incremental: "+configDate);
//		yesterdayDate=utility.getYesterdayDateString();
//		currentDate=utility.currentDate();
//		if(!configDate.equals(yesterdayDate)) {
//			log.info("if last day file not created");
//			currentDate=utility.currentDate();
//			log.info("currentDate:  "+currentDate);
//			long differenceOfDates=utility.getDifferenceDays(configDate,currentDate);
//			log.info("difference from currentDate and dump table date: "+differenceOfDates);
//			systemConfigurationDb.setTag(IncrDumpInDays);
//			frequencyInDays=configurationManagementServiceImpl.findByTag(systemConfigurationDb);
//			log.info("total frequency In Days for incremental dump: "+frequencyInDays.getValue().toString());
//			int daysUnit=Integer.parseInt(frequencyInDays.getValue());
//			int days=0;
//			Date dateForConfig=topDataForIncdump.getCreatedOn();
//			while(differenceOfDates>Long.parseLong(frequencyInDays.getValue())){
//				log.info("if difference between dates greater than total days of frequency");
//				currentTime=utility.getTxnId();
//				String DayAdded=utility.addDaysInDate(days,dateForConfig);
//				log.info("day added + "+DayAdded);
//				log.info("days: "+days);
//				Date stringToDate=utility.stringToDate(DayAdded);
//				log.info("day added date:  "+stringToDate);
//				FileDumpFilter filter=new FileDumpFilter();
//				filter.setStartDate(utility.convertToDateformat(dateForConfig));
//				filter.setEndDate(utility.convertToDateformat(stringToDate));
//				yesterdayId=utility.getYesterdayId();
//				log.info("fetch data from greylist db between dates : "+topDataForIncdump.getCreatedOn() +"to "+ stringToDate);
//				String fileName="GreyList_Incremental_"+utility.convertToDate(stringToDate)+".csv";
//				log.info("file path and name is:  "+fileName);
//				saveDataIntoFile(filter,fileName,filePath);
//				DayAdded=utility.addDaysInDate(days, dateForConfig);
//				configDate=DayAdded;
//				//dateForConfig=stringToDate;
//				days=days+daysUnit;
//				differenceOfDates=utility.getDifferenceDays(configDate,currentDate);
//				log.info("differenceOfDates after file created: "+differenceOfDates);
//			}
//		}
//	}
	
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
			systemConfigurationDb.setTag(IncrDumpInDays);
			frequencyInDays=configurationManagementServiceImpl.findByTag(systemConfigurationDb);
			log.info("total frequency In Days for incremental dump: "+frequencyInDays.getValue().toString());
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
				log.info("fetch data from greylist db  history between dates : "+startDate +"to "+ startDate);
				String fileName="GreyList_Incremental_"+utility.convertToDate(startDate)+".csv";
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
		log.info("going to fetch data from grey list history db for date"+fileDataFilter);
		List<GreylistDbHistory> greyListData=nationalislmServiceImpl.greyListHistoryDataByCreatedOn(fileDataFilter);
		log.info("greyListData:" +greyListData);
		if(!greyListData.isEmpty()) {
			log.info("if grey list history table is not empty");
			header="Device ID,Operation";
			utility.writeGreyListHistoryInFile(filePath+fileName, header, greyListData);        
		}
		else {
			log.info("if grey list history table is empty");
			header="Message";
			String record="No data available in GreyList.";
			utility.writeInFile(filePath+fileName, header, record);	
		}
		FileDumpMgmt fileDumpMgmt=new FileDumpMgmt(new Date(),new Date(), fileName, DumpType.INCREMENTAL.getCode(),ServiceDump.GreyListService.getCode(), dumpTypeInc);
		listFileDetailsImpl.saveFileDumpMgmt(fileDumpMgmt);
	}
}
