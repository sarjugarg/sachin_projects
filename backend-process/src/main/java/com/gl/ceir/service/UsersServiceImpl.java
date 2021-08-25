package com.gl.ceir.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.configuration.PropertiesReader;
import com.gl.ceir.constant.Datatype;
import com.gl.ceir.constant.SearchOperation;
import com.gl.ceir.entity.User;
import com.gl.ceir.pojo.SearchCriteria;
import com.gl.ceir.repo.UserRepository;
import com.gl.ceir.specification.GenericSpecificationBuilder;
import com.gl.ceir.util.DateUtil;
import com.gl.ceir.util.Utility;

@Service
public class UsersServiceImpl {

	private static final Logger logger = LogManager.getLogger(UsersServiceImpl.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	PropertiesReader propertiesReader;

	@Autowired
	Utility utility;

	public List<User> getUserWithStatusPendingOtp(int day) {
		try {
			String fromDate = DateUtil.nextDate(day);
			String toDate = DateUtil.nextDate(day-1);
			logger.info("fromDate[" + fromDate + "] toDate[" + toDate + "]");
			
			return userRepository.findAll(buildSpecification(fromDate, toDate).build());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<>(1);
		}
	}

	public void deleteSomeUser(List<Long> ids) {
		for(Long id : ids) {
			userRepository.deleteById(id);
		}
	}

	private GenericSpecificationBuilder<User> buildSpecification(String fromDate, String toDate){
		GenericSpecificationBuilder<User> cmsb = new GenericSpecificationBuilder<>(propertiesReader.dialect);

		cmsb.with(new SearchCriteria("createdOn", fromDate , SearchOperation.GREATER_THAN_OR_EQUAL, Datatype.DATE));
		cmsb.with(new SearchCriteria("createdOn", toDate , SearchOperation.LESS_THAN, Datatype.DATE));
		cmsb.with(new SearchCriteria("currentStatus", 1, SearchOperation.EQUALITY, Datatype.STRING));

		return cmsb;
	}

}
