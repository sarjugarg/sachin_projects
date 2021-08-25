package com.ceir.GreyListProcess.process;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ceir.GreyListProcess.model.FileDumpFilter;
import com.ceir.GreyListProcess.model.FileDumpMgmt;
import com.ceir.GreyListProcess.model.GreylistDb;
import com.ceir.GreyListProcess.model.SystemConfigurationDb;
import com.ceir.GreyListProcess.model.constants.DumpType;
import com.ceir.GreyListProcess.model.constants.ServiceDump;
import com.ceir.GreyListProcess.repository.SystemConfigurationDbRepository;
import com.ceir.GreyListProcess.repositoryImpl.ConfigurationManagementServiceImpl;
import com.ceir.GreyListProcess.repositoryImpl.ListFileDetailsImpl;
import com.ceir.GreyListProcess.repositoryImpl.NationalislmServiceImpl;
import com.ceir.GreyListProcess.util.Utility;

@Service
public class FullDumpProcess {

     private final Logger log = Logger.getLogger(getClass());

     @Autowired
     SystemConfigurationDbRepository systemConfigDbRepo;

     @Autowired
     ConfigurationManagementServiceImpl configurationManagementServiceImpl;

     @Autowired
     Utility utility;

     @Autowired
     ListFileDetailsImpl listFileDetailsImpl;

     @Autowired
     NationalislmServiceImpl nationalislmServiceImpl;

     @Value("${dumpTypeFull}")
     String dumpTypeFull;

     @Value("${FullDumpInDays}")
     String fullDumpInDays;

     SystemConfigurationDb systemConfigurationDb = new SystemConfigurationDb();
     SystemConfigurationDb frequencyInDays = new SystemConfigurationDb();
     SystemConfigurationDb startWeekOfDay = new SystemConfigurationDb();
     String currentDate = new String();
     String yesterdayDate = new String();
     String currentTime = new String();
     String yesterdayTime = new String();
     FileDumpMgmt topDataForFulldump = new FileDumpMgmt();

     public void fullDumpProcess(String filePath) {
          log.info("inside full dump process");
          topDataForFulldump = listFileDetailsImpl.topDataByDumpTypeAndServiceDump(dumpTypeFull, ServiceDump.GreyListService.getCode());
          //systemConfigurationDb.setTag("GREYLIST_FILEPATH");
          log.info("now checking whether full dump data exist in file_dump_mgmt table or not" );
          if (topDataForFulldump != null) {
               log.info("topDataForFulldump type : " + topDataForFulldump.getDumpType().toString());
               log.info("topDataForFulldump fileName : " + topDataForFulldump.getFileName().toString());
               fullDumpFileProcess(filePath);
          } else {
               log.info("if file_dump_mgmt table for full dump is not found");
               log.info("So this is full dump process first time to start");
               FileDumpFilter filter = new FileDumpFilter();
               yesterdayTime = utility.getYesterdayId();
               String fileName = "GreyList_Full_" + yesterdayTime + ".csv";
               log.info("greyList full dump filename  : "+fileName);
               saveDataIntoFile(filter, fileName, filePath);
          }
          log.info("exit from full dump process");
     }

     public void fullDumpFileProcess(String filePath) {
          log.info("createdOn column value: " + topDataForFulldump.getCreatedOn());
          Date configDate = topDataForFulldump.getCreatedOn();
          String compareDate = utility.convertToDateformat(configDate);
          yesterdayDate = utility.getYesterdayDateString();
          currentDate = utility.currentDate();
          if (!compareDate.equals(currentDate)) {
               log.info("if last day file not created");
               currentDate = utility.currentDate();
               log.info("currentDate:  " + currentDate);
               String configToDate = utility.convertToDateformat(configDate);
               long differenceOfDates = utility.getDifferenceDays(configToDate, currentDate);
               log.info("difference from currentDate and dump table date: " + differenceOfDates);
               systemConfigurationDb.setTag(fullDumpInDays);
               frequencyInDays = configurationManagementServiceImpl.findByTag(systemConfigurationDb);
               log.info("total frequency In Days for full dump: " + frequencyInDays.getValue().toString());
               //int daysUnit=Integer.parseInt(frequencyInDays.getValue());
               int days = 1;
               //Date dateForConfig=topDataForIncdump.getCreatedOn();
               String DayAdded = new String();
               Date startDate = configDate;
               String endDate = new String();
               Integer frequencyDays = Integer.parseInt(frequencyInDays.getValue());
               while (differenceOfDates >= Long.parseLong(frequencyInDays.getValue())) {
                    log.info("if difference between dates greater than total days of frequency");
                    currentTime = utility.getTxnId();
                    log.info("day added + " + DayAdded);
                    log.info("days: " + days);
                    int daysSubtract = frequencyDays - 1;
                    endDate = utility.addDaysInDate(daysSubtract, startDate);
                    //log.info("day added date:  "+endDate);
                    FileDumpFilter filter = new FileDumpFilter();
                    //filter.setStartDate(utility.convertToDateformat(configDate));
                    filter.setEndDate(endDate);
                    //yesterdayId=utility.getYesterdayId();
                    log.info("fetch data from greylist db between dates : " + startDate + "to " + startDate);
                    String fileName = "GreyList_Full_" + utility.convertToDate(startDate) + ".csv";
                    log.info("file path and name is:  " + fileName);
                    saveDataIntoFile(filter, fileName, filePath);
                    startDate = utility.stringToDate(endDate);
                    DayAdded = utility.addDaysInDate(days, startDate);
                    startDate = utility.stringToDate(DayAdded);
                    differenceOfDates = differenceOfDates - frequencyDays;
                    log.info("differenceOfDates after file created: " + differenceOfDates);
               }
          }
     }

     public void saveDataIntoFile(FileDumpFilter fileDataFilter, String fileName, String filePath) {
          String header = new String();
          log.info("now fetching grey list data for full dump from table greylist_db");
          List<GreylistDb> greyListData = nationalislmServiceImpl.greyListDataByCreatedOn(fileDataFilter);
          //log.info("greyListData full dump data is fetched");
          //log.info("now going to save data into file");
          if (!greyListData.isEmpty()) {
               log.info("if grey list data is not empty");
               log.info("saving this grey list data to the file");
               log.info("filename: " + fileName);
               header = "Device ID";
               utility.writeGreyListInFile(filePath + fileName, header, greyListData);
          } else {
               log.info("if grey list is  empty");
               header = "Message";
               String record = "No data available in GreyList.";
               utility.writeInFile(filePath + fileName, header, record);
          }
          FileDumpMgmt fileDumpMgmt = new FileDumpMgmt(new Date(), new Date(), fileName, DumpType.FULL.getCode(), ServiceDump.GreyListService.getCode(), dumpTypeFull);
          listFileDetailsImpl.saveFileDumpMgmt(fileDumpMgmt);
     }
}

//	public void fullDumpFileProcess(String filePath) {
//         
//		yesterdayDate=utility.getYesterdayDateString();
//		currentDate=utility.currentDate();
//		//subtract one date from latest created on column date of fileDumpMgmt
//		Date dateSubtract=utility.subtractDays(topDataForFulldump.getCreatedOn(), 1);
//		//dateSubtract in String variable which is config date
//		String configDate=utility.convertToDateformat(dateSubtract);
//		log.info("date from file dump table if dumpType is Full: "+configDate);
//		log.info("date from file dump table if dumpType is Incremental: "+configDate);
//		//last day date
//		yesterdayDate=utility.getYesterdayDateString();
//		//current date
//		currentDate=utility.currentDate();
//		//if config date not equals to last day date
//		if(!configDate.equals(yesterdayDate)) {
//			
//			log.info("currentDate:  "+currentDate);
//			//difference between config date and current
//			long differenceOfDates=utility.getDifferenceDays(configDate,currentDate);
//			
//			log.info("difference from currentDate and dump table date: "+differenceOfDates);
//			systemConfigurationDb.setTag(fullDumpInDays);
//			//greylist full dump file creation date frequency
//			frequencyInDays=configurationManagementServiceImpl.findByTag(systemConfigurationDb);
//			log.info("total frequency In Days for full dump: "+frequencyInDays.getValue().toString());
//            //frequency date in numbers
//			int daysUnit=Integer.parseInt(frequencyInDays.getValue());
//			//Initialize day variable with 0
//			int days=0;
//			//file dumpmgmt table created date
//			Date dateForConfig=topDataForFulldump.getCreatedOn();
//			//difference between config date and current date greater than dump file creation date frequency
//			while(differenceOfDates>Long.parseLong(frequencyInDays.getValue())){
//				log.info("if difference between dates greater than total days of frequency");
//				currentTime=utility.getTxnId();
//				//days added to filedumpmgmt creation date 
//				String DayAdded=utility.addDaysInDate(days,dateForConfig);
//				log.info("day added + "+DayAdded);
//			    // convert day added above variable into date format 	
//				Date stringToDate=utility.stringToDate(DayAdded);
//				log.info("day added date:  "+stringToDate);
//				FileDumpFilter filter=new FileDumpFilter();
//				//set filter start date= filedumpmgmt creation date
//				filter.setStartDate(utility.convertToDateformat(topDataForFulldump.getCreatedOn()));
//				//set filter end date=day added date
//				filter.setEndDate(utility.convertToDateformat(stringToDate));
//				log.info("fetch data from greylist db between dates : "+topDataForFulldump.getCreatedOn() +"to "+ stringToDate);
//				//filename=date added date
//				String fileName="GreyList_Full_"+utility.convertToDateOnlyformat(stringToDate)+".csv";
//				log.info("file path and name is:  "+fileName);
//				saveDataIntoFile(filter,fileName,filePath);
//				DayAdded=utility.addDaysInDate(days, dateForConfig);
//				configDate=DayAdded;
//				//dateForConfig=stringToDate;
//				days=days+daysUnit;
//				differenceOfDates=utility.getDifferenceDays(configDate,currentDate);
//				log.info("differenceOfDates after file created: "+differenceOfDates);
//			}
//		
//		}
//	}






