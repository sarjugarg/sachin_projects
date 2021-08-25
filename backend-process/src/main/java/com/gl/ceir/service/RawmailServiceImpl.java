package com.gl.ceir.service;

import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.pojo.MessageConfigurationDb;
import com.gl.ceir.pojo.RawMail;
import com.gl.ceir.repo.MessageConfigurationDbRepository;

@Service
public class RawmailServiceImpl {

	private static final Logger logger = LogManager.getLogger(RawmailServiceImpl.class);

	@Autowired
	MessageConfigurationDbRepository messageConfigurationDbRepository;

	public MessageConfigurationDb createMailContent(RawMail rawMail) {
		return createMailContent(rawMail.getTag(), rawMail.getPlaceholders());
	}
	
	public MessageConfigurationDb createMailContent(String tag, Map<String, String> placeholders) {
		String message = "";
		MessageConfigurationDb messageDB = messageConfigurationDbRepository.getByTagAndActive(tag, 0);
		logger.info("Message for tag [" + tag + "] " + messageDB);
		
		if(Objects.isNull(messageDB)) {
			logger.info("No mail content is configured for tag [" + tag + "]");
			return null;
		}else {
			message = messageDB.getValue();	
		}

		// Replace Placeholders from message.
		if(Objects.nonNull(placeholders)) {
			for (Map.Entry<String, String> entry : placeholders.entrySet()) {
				logger.info("Placeholder key : " + entry.getKey() + " value : " + entry.getValue());
				message = message.replaceAll(entry.getKey(), entry.getValue());
			}
		}
		
		messageDB.setValue(message);
		return messageDB;
	}
}
