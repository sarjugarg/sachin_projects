package com.ceir.SLAModule.repoService;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ceir.SLAModule.App;
import com.ceir.SLAModule.entity.TypeApprovedTAC;
import com.ceir.SLAModule.repo.TacApproveRepo;

@Service
public class TypeAppoveRepoService {

	@Autowired
	TacApproveRepo tacRepo;

	private final static Logger log =Logger.getLogger(App.class);

	public List<TypeApprovedTAC> fetchTacByStatus(int status){
		try {
			log.info("now fetching tac approve data by status");
			return tacRepo.findByStatus(status);
		}
		catch(Exception e) {
			log.info("error occuring when fetch tac approve data by status");
			log.info(e.toString());
			return new ArrayList<TypeApprovedTAC>();
		}
	}
}
