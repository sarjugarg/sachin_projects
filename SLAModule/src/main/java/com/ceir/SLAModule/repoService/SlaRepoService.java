package com.ceir.SLAModule.repoService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.SLAModule.App;
import com.ceir.SLAModule.entity.SlaReport;
import com.ceir.SLAModule.repo.SlaRepo;

@Service
public class SlaRepoService {

	@Autowired
	SlaRepo slaRepo;
	
	private final static Logger log =Logger.getLogger(App.class);
	
	public List<SlaReport> saveSLA(List<SlaReport> report) {
		
		try {
			log.info("going to save sla report");
			return slaRepo.saveAll(report);
		}
		catch(Exception e) {
			log.info("sla report data fail to save");
			return new ArrayList<SlaReport>();
		}
	}
}
