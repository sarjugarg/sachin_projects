package com.gl.ceir.factory.service.transaction;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.notifier.NotifierWrapper;
import com.gl.ceir.pojo.RawMail;
import com.gl.ceir.service.UsersServiceImpl;

@Component
@Transactional(rollbackOn = Exception.class)
public class RemoveInCompleteUserTransaction {
	
	@Autowired
	UsersServiceImpl usersServiceImpl;
	
	@Autowired
	NotifierWrapper notifierWrapper;
	
	public void performTransaction(List<Long> userIds, List<RawMail> rawMails) {

		usersServiceImpl.deleteSomeUser(userIds);
		
		notifierWrapper.saveNotification(rawMails);
	}

}
