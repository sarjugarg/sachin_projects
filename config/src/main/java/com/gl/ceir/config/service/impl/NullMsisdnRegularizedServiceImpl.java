package com.gl.ceir.config.service.impl;

import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.exceptions.ResourceNotFoundException;
import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.NullMsisdnRegularized;
import com.gl.ceir.config.repository.NullMsisdnRegularizedRepository;
import com.gl.ceir.config.service.NullMsisdnRegularizedService;

@Service
public class NullMsisdnRegularizedServiceImpl implements NullMsisdnRegularizedService {

	private static final Logger logger = Logger.getLogger(NullMsisdnRegularizedServiceImpl.class);

	@Autowired
	private NullMsisdnRegularizedRepository repo;

	@Override
	public NullMsisdnRegularized get(Long msisdn) {
		try {
			NullMsisdnRegularized nullMsisdnRegularized = repo.findById(msisdn)
					.orElseThrow(() -> new ResourceNotFoundException("NullMsisdnRegularized", "msisdn", msisdn));
			return nullMsisdnRegularized;
		} catch (ResourceNotFoundException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	@Override
	public List<NullMsisdnRegularized> saveAll(List<NullMsisdnRegularized> nullMsisdnRegularizeds) {
		return repo.saveAll(nullMsisdnRegularizeds);
	}

	@Override
	public NullMsisdnRegularized save(NullMsisdnRegularized nullMsisdnRegularized) {
		return repo.save(nullMsisdnRegularized);
	}

}
