/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
/**
 *
 * @author maverick
 */
 


package com.ceir.CEIRPostman.RepositoryService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ceir.CEIRPostman.Repository.PolicyBreachNotificationRepository;
import com.ceir.CEIRPostman.model.PolicyBreachNotification;
@Service
public class PolicyBreachNotificationRepoImpl {

	@Autowired
	PolicyBreachNotificationRepository policyBreachNotificationRepository;
	
	private final Logger log = Logger.getLogger(getClass());
	public List<PolicyBreachNotification> dataByStatusAndChannelType(int status,String type) {
		try {
			List<PolicyBreachNotification> notification=policyBreachNotificationRepository.findByStatusAndChannelType(status,type);
		    return notification;
		}
		catch(Exception e) {
			log.info("error occurs while fetch notification data");
			log.info(e.toString());
            return new ArrayList<PolicyBreachNotification>();
		}
	}
	
	public List<PolicyBreachNotification> dataByStatus(int status) {
		try {
			List<PolicyBreachNotification> notification=policyBreachNotificationRepository.findByStatus(status);
		    return notification;
		}
		catch(Exception e) {
			log.info(e.toString());
            return new ArrayList<PolicyBreachNotification>();
		}
	}
}
