package com.gl.imei.freq;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.imei.freq.dao.MainProcessDao;

@Component
public class MainProcess {

	private static final Logger logger = Logger.getLogger(MainProcess.class);
	
	@Autowired
	MainProcessDao mainProcessDao;
	
	public void start() {
		logger.info("Starting Process");
		mainProcessDao.getImeiCount();
		mainProcessDao.getFreqCount();
	}
}
