package com.ceir.BlackListProcess.service;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.BlackListProcess.process.BlackListProcess;

@Service
public class BlackListService implements Runnable{

	@Autowired
	BlackListProcess blackListProcess;

	private final Logger log =Logger.getLogger(getClass());


	@Override
	public void run() {

		while(true) {
			log.info("inside blacklist service");
			blackListProcess.blackListProcess();
			log.info("exit from blacklist service");
           System.exit(0);
		}
	}
}


