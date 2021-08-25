package com.gl.ceir.factory.service.transaction;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gl.ceir.repo.EndUserDbRepository;

@Component
@Transactional(rollbackOn = Exception.class)
public class VisaExpireTransaction {
	
	@Autowired
	EndUserDbRepository endUserDbRepository;

	public void performTransaction(String nid) {
		endUserDbRepository.deleteByNid(nid);
	}

}
