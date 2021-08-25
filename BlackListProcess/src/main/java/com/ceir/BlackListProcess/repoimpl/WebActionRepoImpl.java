package com.ceir.BlackListProcess.repoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.BlackListProcess.repository.WebActionDbRepository;

@Service
public class WebActionRepoImpl {
	
	@Autowired
	WebActionDbRepository webActionRepo;
	
    public boolean checkFeatureExist(String feature) {
    	boolean b=false;
    	try {
    		return webActionRepo.existsByFeature(feature);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    		return b;
    	}
    }

}
