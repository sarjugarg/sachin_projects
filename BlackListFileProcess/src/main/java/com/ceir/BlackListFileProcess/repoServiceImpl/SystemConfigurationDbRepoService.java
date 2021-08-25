package com.ceir.BlackListFileProcess.repoServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.BlackListFileProcess.model.SystemConfigurationDb;
import com.ceir.BlackListFileProcess.repo.SystemConfigurationDbRepository;

@Service
public class SystemConfigurationDbRepoService {

	@Autowired
	SystemConfigurationDbRepository systemConfigRepo;
	
	public SystemConfigurationDb getDataByTag(String tag) {
		
	try {
		SystemConfigurationDb systemConfigurationDb=systemConfigRepo.getByTag(tag);
		return systemConfigurationDb;
	}
		
	catch(Exception e) {
		return null;
	}
	}
	
}
