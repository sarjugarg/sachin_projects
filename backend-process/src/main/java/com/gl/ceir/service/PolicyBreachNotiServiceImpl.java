package com.gl.ceir.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.entity.PolicyBreachNotification;
import com.gl.ceir.pojo.MessageConfigurationDb;
import com.gl.ceir.pojo.UserWiseMailCount;
import com.gl.ceir.repo.MessageConfigurationDbRepository;
import com.gl.ceir.repo.PolicyBreachNotificationRepository;

@Service
public class PolicyBreachNotiServiceImpl {

	private static final Logger logger = LogManager.getLogger(PolicyBreachNotiServiceImpl.class);
	
	@Autowired
	MessageConfigurationDbRepository messageConfigurationDbRepository;
	
	@Autowired
	PolicyBreachNotificationRepository policyBreachNotificationRepository;

	
	public void batchUpdatePolicyBreachNoti(String tag, List<UserWiseMailCount> userWiseMailCounts) {

		String channel = "SMS";
		String policyBreachMessage = "";
		
		try {
			// Check if user has email.
			MessageConfigurationDb messageDB = messageConfigurationDbRepository.getByTagAndActive(tag, 0);
			policyBreachMessage = messageDB.getValue();
			
			// Add Entry In Policy Breach Table.
			List<PolicyBreachNotification> policyBreachNotifications = new ArrayList<>();
			
			for(UserWiseMailCount userWiseMailCount : userWiseMailCounts) {
				Map<String, String>  placeholders = userWiseMailCount.getPlaceholderMap();

				// Replace Placeholders from message.
				if(Objects.nonNull(placeholders)) {
					for (Map.Entry<String, String> entry : placeholders.entrySet()) {
						logger.debug("Placeholder key : " + entry.getKey() + " value : " + entry.getValue());
						policyBreachMessage = policyBreachMessage.replaceAll(entry.getKey(), entry.getValue());
					}
				}

				policyBreachNotifications.add(new PolicyBreachNotification(
						channel, 
						policyBreachMessage, 
						"", 
						Long.parseLong(userWiseMailCount.getPhoneNo()), 
						userWiseMailCount.getFirstImei()));
				if(Objects.nonNull(userWiseMailCount.getSecondImei()))
					policyBreachNotifications.add(new PolicyBreachNotification(
							channel, 
							policyBreachMessage, 
							"", 
							Long.parseLong(userWiseMailCount.getPhoneNo()), 
							userWiseMailCount.getSecondImei()));

				if(Objects.nonNull(userWiseMailCount.getThirdImei()))
					policyBreachNotifications.add(new PolicyBreachNotification(
							channel, 
							policyBreachMessage, 
							"", 
							Long.parseLong(userWiseMailCount.getPhoneNo()), 
							userWiseMailCount.getThirdImei()));

				if(Objects.nonNull(userWiseMailCount.getFourthImei()))
					policyBreachNotifications.add(new PolicyBreachNotification(
							channel, 
							policyBreachMessage, 
							"", 
							Long.parseLong(userWiseMailCount.getPhoneNo()), 
							userWiseMailCount.getFourthImei()));
			}

			logger.info(policyBreachNotifications);

			policyBreachNotificationRepository.saveAll(policyBreachNotifications);
			logger.info("Entry added in policy_breach_notification.");

			
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}