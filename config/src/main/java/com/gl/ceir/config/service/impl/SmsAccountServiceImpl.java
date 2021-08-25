package com.gl.ceir.config.service.impl;

import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.exceptions.ResourceNotFoundException;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.MobileOperator;
import com.gl.ceir.config.model.SmsAccount;
import com.gl.ceir.config.repository.SmsAccountRepository;
import com.gl.ceir.config.service.SmsAccountService;

@Service
public class SmsAccountServiceImpl implements SmsAccountService {

	private static final Logger logger = Logger.getLogger(SmsAccountServiceImpl.class);

	@Autowired
	private SmsAccountRepository smsAccountRepository;

	@Override
	public List<SmsAccount> getAll() {
		try {
			return smsAccountRepository.findAll();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	@Override
	public SmsAccount save(SmsAccount smsAccount) {

		try {
			return smsAccountRepository.save(smsAccount);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public SmsAccount get(Long id) {

		try {
			SmsAccount smsAccount = smsAccountRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Sms Account", "id", id));
			return smsAccount;
		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public SmsAccount update(SmsAccount smsAccount) {

		try {
			return smsAccountRepository.save(smsAccount);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public void delete(Long id) {

		try {
			smsAccountRepository.deleteById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

}
