package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gl.ceir.config.model.BlackList;
import com.gl.ceir.config.model.ImeiMsisdnIdentity;
 
@Repository
public interface BlackListRepository extends JpaRepository<BlackList, ImeiMsisdnIdentity> {
	public BlackList findByImeiMsisdnIdentityMsisdn(Long msisdn);

	public BlackList findByImeiMsisdnIdentityImei(Long imei);
}

