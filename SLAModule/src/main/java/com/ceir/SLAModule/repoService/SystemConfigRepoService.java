package com.ceir.SLAModule.repoService;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.SLAModule.App;
import com.ceir.SLAModule.entity.SystemConfigurationDb;
import com.ceir.SLAModule.repo.SysConfigDbRepo;

@Service
public class SystemConfigRepoService {

	@Autowired
	SysConfigDbRepo systemConfigRepo;
	
	
	private final static Logger log =Logger.getLogger(App.class);
	public SystemConfigurationDb getByTag(String tag){
		try {
			return systemConfigRepo.findByTag(tag);
		}
		catch(Exception e) {
			log.info("error occuring when fetch system configuration data by tag");
			log.info(e.toString());
			return null;
		}
	}
}
