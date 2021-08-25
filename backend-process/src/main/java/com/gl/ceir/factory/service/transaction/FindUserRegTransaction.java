package com.gl.ceir.factory.service.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.entity.DaywiseUserReg;
import com.gl.ceir.entity.PolicyBreachNotification;
import com.gl.ceir.notifier.NotifierWrapper;
import com.gl.ceir.pojo.RawMail;
import com.gl.ceir.repo.DaywiseUserRegRepository;
import com.gl.ceir.repo.PolicyBreachNotificationRepository;

@Component
@Transactional(rollbackOn = Exception.class)
public class FindUserRegTransaction {
	
	@Autowired
	DaywiseUserRegRepository daywiseUserRegRepository;
	
	@Autowired
	PolicyBreachNotificationRepository policyBreachNotificationRepository;
	
	@Autowired
	NotifierWrapper notifierWrapper;
	
	public void performTransaction(List<DaywiseUserReg> daywiseUserReg, List<PolicyBreachNotification> policyBreachNotifications, List<RawMail> rawMails) {

		daywiseUserRegRepository.saveAll(daywiseUserReg);
		
		policyBreachNotificationRepository.saveAll(policyBreachNotifications);
		
		notifierWrapper.saveNotification(rawMails);
	}

}
