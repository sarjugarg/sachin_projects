package com.gl.ceir.config.service.impl;

import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.exceptions.ResourceNotFoundException;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.BlackList;
import com.gl.ceir.config.model.ImeiMsisdnIdentity;
import com.gl.ceir.config.repository.BlackListRepository;
import com.gl.ceir.config.service.BlackListService;

@Service
public class BlackListServiceImpl implements BlackListService {

	private static final Logger logger = Logger.getLogger(BlackListServiceImpl.class);

	@Autowired
	private BlackListRepository blackListRepository;

	@Override
	public List<BlackList> getAll() {
		try {
			logger.info("Going to get All Black List Devices");
			return blackListRepository.findAll();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	@Override
	public BlackList save(BlackList blackList) {

		try {
			return blackListRepository.save(blackList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public BlackList get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		try {
			// blackListRepository.deleteById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

	@Override
	public BlackList update(BlackList blackList) {

		try {
			return blackListRepository.save(blackList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public BlackList getByMsisdn(Long msisdn) {

		try {
			return blackListRepository.findByImeiMsisdnIdentityMsisdn(msisdn);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public BlackList getByImei(Long imei) {

		try {
			return blackListRepository.findByImeiMsisdnIdentityImei(imei);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public BlackList getByMsisdnAndImei(ImeiMsisdnIdentity imeiMsisdnIdentity) {
		logger.info("Going to get Black List Devices by " + imeiMsisdnIdentity);
		try {
			if (imeiMsisdnIdentity.getMsisdn() == null && imeiMsisdnIdentity.getImei() == null) {
				return null;
			} else if (imeiMsisdnIdentity.getMsisdn() != null && imeiMsisdnIdentity.getImei() != null) {
				return blackListRepository.findById(imeiMsisdnIdentity).orElseThrow(
						() -> new ResourceNotFoundException("Black Device", "imeiMsisdnIdentity", imeiMsisdnIdentity));
			} else if (imeiMsisdnIdentity.getMsisdn() != null) {
				return getByMsisdn(imeiMsisdnIdentity.getMsisdn());
			} else {
				return getByImei(imeiMsisdnIdentity.getImei());
			}

		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public void deleteByMsisdnAndImei(ImeiMsisdnIdentity imeiMsisdnIdentity) {
		logger.info("Going to delete Black List Device by " + imeiMsisdnIdentity);
		if (imeiMsisdnIdentity.getMsisdn() != null && imeiMsisdnIdentity.getImei() != null) {
			blackListRepository.deleteById(imeiMsisdnIdentity);
		} else {
			logger.info("Not Deleted " + imeiMsisdnIdentity);
			return;
		}
	}

}
