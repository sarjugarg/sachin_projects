package com.ceir.GreyListProcess.repositoryImpl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.GreyListProcess.configuration.PropertiesReader;
import com.ceir.GreyListProcess.exceptions.ResourceServicesException;
import com.ceir.GreyListProcess.model.FileDumpMgmt;
import com.ceir.GreyListProcess.repository.FileDumpMgmtRepository;


@Service
public class ListFileDetailsImpl {

	private static final Logger logger = LogManager.getLogger(ListFileDetailsImpl.class);

	@Autowired
	FileDumpMgmtRepository fileDumpMgmtRepository;

	@Autowired
	PropertiesReader propertiesReader;
	
	public List<FileDumpMgmt> getByListType(String listType){
		try {

			return fileDumpMgmtRepository.getByServiceDump(listType);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}
	
	public FileDumpMgmt topDataByDumpType(String dumpType) {
		try {
			logger.info("going to fetch topDataByDumpType where dumpType = "+dumpType);
			return fileDumpMgmtRepository.findTopByDumpTypeOrderByIdDesc(dumpType);
		}
		catch(Exception e) {
			logger.info("exception occur when fetching top data by dumpType in file_dump_mgmt table ");
			e.printStackTrace();
			return null;
		}
	}
	
	public FileDumpMgmt topDataByDumpTypeAndServiceDump(String dumpType,int serviceDump) {
		try {
			logger.info("going to fetch topDataByDumpType where dumpType = "+dumpType);
			return fileDumpMgmtRepository.findTopByDumpTypeAndServiceDumpOrderByIdDesc(dumpType,serviceDump);
		}
		catch(Exception e) {
			logger.info("exception occur when fetching top data by dumpType in file_dump_mgmt table ");
			e.printStackTrace();
			return null;
		}
	}
	
	public FileDumpMgmt saveFileDumpMgmt(FileDumpMgmt fileDumpMgmt) {
		try {
			return fileDumpMgmtRepository.save(fileDumpMgmt);
		}
		catch(Exception e) {
			e.printStackTrace();
			FileDumpMgmt fileData=new FileDumpMgmt();
			return fileData;
		}
	}
	
	
	
	
}
