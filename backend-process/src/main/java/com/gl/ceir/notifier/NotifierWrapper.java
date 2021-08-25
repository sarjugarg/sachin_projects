package com.gl.ceir.notifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.entity.Notification;
import com.gl.ceir.pojo.MessageConfigurationDb;
import com.gl.ceir.pojo.RawMail;
import com.gl.ceir.repo.MessageConfigurationDbRepository;
import com.gl.ceir.service.AlertServiceImpl;
import com.gl.ceir.service.NotificationServiceImpl;
import com.gl.ceir.service.RawmailServiceImpl;

@Component
public class NotifierWrapper {

	private Logger logger = LogManager.getLogger(NotifierWrapper.class);

	@Autowired
	NotificationServiceImpl notificationServiceImpl;

	@Autowired
	MessageConfigurationDbRepository messageConfigurationDbRepository;
	
	@Autowired
	RawmailServiceImpl rawmailServiceImpl;
	
	@Autowired
	AlertServiceImpl alertServiceImpl;

	public boolean saveNotification(List<RawMail> rawMails) {
		List<Notification> notifications = new ArrayList<>();
		
		if(rawMails.isEmpty()) {
			return Boolean.TRUE;
		}
		
		try {
			for(RawMail rawMail : rawMails) {
				MessageConfigurationDb message = rawmailServiceImpl.createMailContent(rawMail);

				if(Objects.isNull(message)) {
					Map<String, String> bodyPlaceHolderMap = new HashMap<>();
					bodyPlaceHolderMap.put("<tag>", rawMail.getTag());
					alertServiceImpl.raiseAnAlert("alert_id", 0, bodyPlaceHolderMap);

					continue;
				}
				
				if(message.getChannel() == 0) {
					addNotifications(rawMail, "Email", message.getValue(), notifications);
				}else if(message.getChannel() == 1) {
					addNotifications(rawMail, "SMS", message.getValue(), notifications);
				}else {
					addNotifications(rawMail, "Email", message.getValue(), notifications);
					addNotifications(rawMail, "SMS", message.getValue(), notifications);
				}				
			}
			
			
			notificationServiceImpl.saveAllNotifications(notifications);
			
			logger.info("No. of total notification [" + notifications.size() + "] sent.");
			logger.info("No. of total notification to unique user [" + rawMails.size() + "] sent.");
			
			return Boolean.TRUE;
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Boolean.FALSE;
		}
	}
	
	private void addNotifications(RawMail rawMail, String channel, String message, List<Notification> notifications) {
		notifications.add(new Notification(channel, 
				message, 
				rawMail.getUserId(), 
				rawMail.getFeatureId(),
				rawMail.getFeatureName(), 
				rawMail.getSubFeature(), 
				rawMail.getFeatureTxnId(), 
				rawMail.getSubject(), 
				0,
				rawMail.getReferTable(),
				rawMail.getRoleType(),
				rawMail.getReceiverUserType()));
		
	}
}