package com.gl.ceir.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.entity.Notification;
import com.gl.ceir.pojo.GenricResponse;
import com.gl.ceir.repo.NotificationRepository;

@Service
public class NotificationServiceImpl {

	private static final Logger logger = LogManager.getLogger(NotificationServiceImpl.class);

	@Autowired
	NotificationRepository notificationRepository;

	public GenricResponse saveAllNotifications(List<Notification> notifications) {
		try {

			List<Notification> notifications2 = notificationRepository.saveAll(notifications);

			return new GenricResponse(0, "Notification have been saved Sucessfully", "", notifications2);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new GenricResponse(1);
		}
	}
}