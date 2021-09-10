package com.ceir.SLAModule.repoService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.SLAModule.App;
import com.ceir.SLAModule.entity.StatesInterpretationDb;
import com.ceir.SLAModule.repo.StatesInterpretationRepository;

@Service
public class StateInterupRepoService {

	@Autowired
	StatesInterpretationRepository stateRepo;
	
	private final static Logger log =Logger.getLogger(App.class);
	
	public StatesInterpretationDb getByFeatureIdAndState(Integer featureId, int state) {
		
		try 
		{
			log.info("going to fetch StatesInterpretationDb data by featureId="+featureId+" and state= "+state+"");
			return stateRepo.findByFeatureIdAndState(featureId, state);
		}
		catch(Exception e) {
			log.info("fail to fetch StatesInterpretationDb data by featureId="+featureId+" and state= "+state+"");
			return new StatesInterpretationDb();
		}
	}
	
	
	public List<StatesInterpretationDb> getByFeatureID(Integer featureId) {
		
		try 
		{
			log.info("going to fetch StatesInterpretationDb data by featureId="+featureId);
			return stateRepo.findByFeatureId(featureId);
		}
		catch(Exception e) {
			log.info("fail to fetch StatesInterpretationDb data by featureId="+featureId);
			return new ArrayList<StatesInterpretationDb>();
		}
	}
}
