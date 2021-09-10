package com.ceir.SLAModule.repoService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.SLAModule.App;
import com.ceir.SLAModule.entity.StolenandRecoveryMgmt;
import com.ceir.SLAModule.repo.GrievanceRepo;
import com.ceir.SLAModule.repo.StolenRecRepo;

@Service
public class StolenRecRepoService {

	@Autowired
	StolenRecRepo stolenRepo;
	

	
private final static Logger log =Logger.getLogger(App.class);
	
	public List<StolenandRecoveryMgmt> fetchStolenByStatusAndReqType(int status,int requestType){
		try {
			log.info("Fetching grievance data from StolenandRecoveryMgmt  where  Filestatus " + status +" and RequestType : "+requestType );
			return stolenRepo.findByFileStatusAndRequestType(status, requestType);
		}
		catch(Exception e) {
			log.info("error occuring when fetch grievance data by status");
			log.info(e.toString());
			return new ArrayList<StolenandRecoveryMgmt>();
		}
	}
}


