package com.ceir.BlackListFileProcess.process;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ceir.BlackListFileProcess.model.BlackList;
import com.ceir.BlackListFileProcess.model.FileDumpFilter;
import com.ceir.BlackListFileProcess.model.FileDumpMgmt;
import com.ceir.BlackListFileProcess.model.SystemConfigurationDb;
import com.ceir.BlackListFileProcess.model.constants.DumpType;
import com.ceir.BlackListFileProcess.model.constants.ServiceDump;
import com.ceir.BlackListFileProcess.repo.SystemConfigurationDbRepository;
import com.ceir.BlackListFileProcess.repoServiceImpl.BlackListRepoImpl;
import com.ceir.BlackListFileProcess.repoServiceImpl.ListFileDetailsImpl;
import com.ceir.BlackListFileProcess.repoServiceImpl.SystemConfigurationDbRepoService;
import com.ceir.BlackListFileProcess.util.Utility;


@Service
public class FullDumpProcess {

//      private static final Logger logger = Logger.getLogger(getClass()) ;
      
	private final Logger log =Logger.getLogger(getClass());

	@Autowired
	SystemConfigurationDbRepository systemConfigDbRepo;


	@Autowired 
	Utility utility;

	@Autowired
	ListFileDetailsImpl listFileDetailsImpl;

	
	
	@Value("${dumpTypeFull}")
	String dumpTypeFull;
	
	@Value("${FullDumpInDays}")
	String fullDumpInDays;
	
	@Autowired
	BlackListRepoImpl blackListRepo;
	
	@Autowired
	SystemConfigurationDbRepoService systemConfigurationDbRepoService;

	SystemConfigurationDb systemConfigurationDb=new SystemConfigurationDb();
	SystemConfigurationDb frequencyInDays=new SystemConfigurationDb();
	SystemConfigurationDb startWeekOfDay=new SystemConfigurationDb();
	String currentDate=new String();
	String yesterdayDate=new String();
	String currentTime=new String();
	String yesterdayTime=new String();
	FileDumpMgmt topDataForFulldump=new FileDumpMgmt();
    
	public void fullDumpProcess(String filePath) {
		log.info("inside full dump process");
		topDataForFulldump=listFileDetailsImpl.topDataByDumpTypeAndServiceDump(dumpTypeFull,ServiceDump.BlackListService.getCode());
		//systemConfigurationDb.setTag("BlackLIST_FILEPATH");
		log.info("now checking whether full dump data exist in file_dump_mgmt table or not");
		if(topDataForFulldump!=null) {
			log.info("topDataForFulldump: "+topDataForFulldump.toString());
			fullDumpFileProcess(filePath);
		}
		else {
			log.info("if file_dump_mgmt table for full dump is not found");
			log.info("So this is full dump process first time to start");
			FileDumpFilter filter=new FileDumpFilter();
			yesterdayTime=utility.getYesterdayId();
			String fileName="BlackList_Full_"+yesterdayTime+".csv";
			//log.info("BlackList full dump filename: "+yesterdayTime);
			saveDataIntoFile(filter,fileName,filePath);
		}
		log.info("exit from full dump process");
	}

	public void fullDumpFileProcess(String filePath) {
		log.info("createdOn column value: "+topDataForFulldump.getCreatedOn());
		Date configDate=topDataForFulldump.getCreatedOn();
		String compareDate=utility.convertToDateformat(configDate);
		yesterdayDate=utility.getYesterdayDateString();
		currentDate=utility.currentDate();
		if(!compareDate.equals(currentDate)) {
			log.info("if last day file not created");
			currentDate=utility.currentDate();
			log.info("currentDate:  "+currentDate);
			String configToDate=utility.convertToDateformat(configDate);
			long differenceOfDates=utility.getDifferenceDays(configToDate,currentDate);
			log.info("difference from currentDate and dump table date: "+differenceOfDates);
			systemConfigurationDb.setTag(fullDumpInDays);
			frequencyInDays=systemConfigurationDbRepoService.getDataByTag(fullDumpInDays);
			log.info("total frequency In Days for full dump: "+frequencyInDays.getValue().toString());
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
				//log.info("day added date:  "+endDate);
				FileDumpFilter filter=new FileDumpFilter();
				//filter.setStartDate(utility.convertToDateformat(configDate));
				filter.setEndDate(endDate);
				//yesterdayId=utility.getYesterdayId();
				log.info("fetch data from blacklist db between dates : "+startDate +"to "+ startDate);
				String fileName="BlackList_Full_"+utility.convertToDate(startDate)+".csv";
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
		log.info("now fetching Black list data for full dump from table Blacklist_db");
		List<BlackList> blckListData=blackListRepo.blackListDataByexpiryDate(fileDataFilter);
		//log.info("BlackListData full dump data is fetched");
		//log.info("now going to save data into file");
		if(!blckListData.isEmpty()) {
			log.info("if Black list data is not empty");
			log.info("saving this Black list data to the file");
			log.info("filename: "+fileName);
			header="Device ID";
			utility.writeBlackListInFile(filePath+fileName, header, blckListData);        
		}
		else {
			log.info("if Black list is  empty");
			header="Message";
			String record="No data available in BlackList.";
			utility.writeInFile(filePath+fileName, header, record);	
		}
		FileDumpMgmt fileDumpMgmt=new FileDumpMgmt(new Date(),new Date(), fileName, DumpType.FULL.getCode(),ServiceDump.BlackListService.getCode(),dumpTypeFull);
		listFileDetailsImpl.saveFileDumpMgmt(fileDumpMgmt);
	}
}




