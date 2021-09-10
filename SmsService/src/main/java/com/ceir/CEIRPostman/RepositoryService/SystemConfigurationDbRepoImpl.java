package com.ceir.CEIRPostman.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ceir.CEIRPostman.Repository.SystemConfigurationDbRepository;
import com.ceir.CEIRPostman.model.SystemConfigurationDb;

@Service
public class SystemConfigurationDbRepoImpl {

	@Autowired
	SystemConfigurationDbRepository SystemConfigurationDbRepo;
	
	public SystemConfigurationDb getDataByTag(String tag) {
	try {
		SystemConfigurationDb systemConfigDb=new SystemConfigurationDb();
		systemConfigDb=SystemConfigurationDbRepo.getByTag(tag);
		return systemConfigDb;
	}
	catch(Exception e) {
		return null;
	}
	}
}