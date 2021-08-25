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
import com.gl.ceir.entity.DeviceUsageDb;
import com.gl.ceir.pojo.SearchCriteria;
import com.gl.ceir.repo.DeviceUsageDbRepository;
import com.gl.ceir.specification.GenericSpecificationBuilder;
import com.gl.ceir.util.DateUtil;
import com.gl.ceir.util.Utility;

@Service
public class DeviceUsageServiceImpl {

	private static final Logger logger = LogManager.getLogger(DeviceUsageServiceImpl.class);

	@Autowired
	PropertiesReader propertiesReader;
	
	@Autowired
	DeviceUsageDbRepository deviceUsageDbRepository;

	@Autowired
	Utility utility;

	public List<DeviceUsageDb> getDeviceUsageOfTodayHavingActionUserReg() {
		try {
			String fromDate = DateUtil.nextDate(-1);
			String toDate = DateUtil.nextDate(0);
			
			logger.info("Record of device usage fromDate[" + fromDate + "] toDate[" + toDate + "]");
			
			return deviceUsageDbRepository.findAll(buildSpecification(fromDate, toDate).build());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ArrayList<>(1);
		}
	}

	private GenericSpecificationBuilder<DeviceUsageDb> buildSpecification(String fromDate, String toDate){
		GenericSpecificationBuilder<DeviceUsageDb> cmsb = new GenericSpecificationBuilder<>(propertiesReader.dialect);

		//cmsb.with(new SearchCriteria("modifiedOn", fromDate , SearchOperation.GREATER_THAN_OR_EQUAL, Datatype.DATE));
		cmsb.with(new SearchCriteria("modifiedOn", toDate , SearchOperation.LESS_THAN, Datatype.DATE));
		cmsb.with(new SearchCriteria("action", "USER_REG", SearchOperation.EQUALITY, Datatype.STRING));

		return cmsb;
	}

}
