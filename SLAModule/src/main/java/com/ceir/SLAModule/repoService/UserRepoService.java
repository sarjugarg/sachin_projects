package com.ceir.SLAModule.repoService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ceir.SLAModule.App;
import com.ceir.SLAModule.entity.TypeApprovedTAC;
import com.ceir.SLAModule.entity.User;
import com.ceir.SLAModule.repo.UserRepo;

@Service
public class UserRepoService {

	@Autowired
	UserRepo userRepo;
	
	private final static Logger log =Logger.getLogger(App.class);

	
	public User getByUserId(long id) {
		
		try {
			log.info("user data going to fetch by id: "+id);
			return userRepo.findById(id);
		}
		catch(Exception e) {
			log.info("error occuring when fetch user data by id: "+id);
			log.info(e.toString());
			return new User();
		}
		
	}
	
	public List<User> fetchUsersByStatus(int status){
		try {
			log.info("now fetching user data by status");
			return userRepo.findByCurrentStatus(status);
		}
		catch(Exception e) {
			log.info("error occuring when fetch tac approve data by status");
			log.info(e.toString());
			return new ArrayList<User>();
		}
	}
}
