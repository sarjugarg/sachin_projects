package com.ceir.BlackListFileProcess.service;

//import org.apache.log4j.Logger;
//import org.apache.log4j.Logger;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ceir.BlackListFileProcess.model.SystemConfigurationDb;
import com.ceir.BlackListFileProcess.process.FullDumpProcess;
import com.ceir.BlackListFileProcess.process.IncrementalDumpProcess;
import com.ceir.BlackListFileProcess.repo.SystemConfigurationDbRepository;
import com.ceir.BlackListFileProcess.util.Utility;


@Service
public class BlackListService implements Runnable{

	@Autowired
	SystemConfigurationDbRepository systemConfigDbRepo;

	@Autowired
	Utility utility;

	@Autowired
	IncrementalDumpProcess increDumpProcess;

	@Autowired
	FullDumpProcess fullDumpProcess;
 	
	@Value
	("${filePathTag}")
	String filePathTag;
	
	@Value
	("${threadSleep}")
	Integer threadSleep;

	private final Logger log =Logger.getLogger(getClass());

	public void run() {
		while(true) {
			SystemConfigurationDb filePath=new SystemConfigurationDb();
			log.info("inside in Black List dump process");
			log.info("now going to check whether stolen data found on web_action_db table or not");   
//			boolean checkStolenStatus=webActionRepoImpl.checkFeatureExist("Stolen");
			//if(checkStolenStatus==false) {
				log.info("If stolen data doesnot exist in web action db");
				SystemConfigurationDb systemConfigurationDb=new SystemConfigurationDb();
				systemConfigurationDb.setTag(filePathTag);
				log.info("now fetching filepath to save Black list dump files");
				try{
					filePath=systemConfigDbRepo.getByTag(filePathTag);
					log.info("filePath:  "+filePath.getValue());
				}
				catch(Exception e) {
					log.info("failed to fetch Black List dump file path");
					e.printStackTrace();
					filePath=new SystemConfigurationDb();
				}
				log.info("now going to process full dump and incremental process one by one");
				fullDumpProcess.fullDumpProcess(filePath.getValue());
				increDumpProcess.incrementalDumpProcess(filePath.getValue());	
				log.info("exit from Black List dump process");
			//}
			/*
			 * else { log.info("stolen data exist in web action db");
			 * log.info("so this process cannot go further"); }
			 */
			log.info("exit from Black List dump process");
           System.exit(0);
//			try {
//				Thread.sleep(threadSleep);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}
}





