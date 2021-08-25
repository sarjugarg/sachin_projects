package com.gl.ceir.config.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.gl.ceir.config.model.DuplicateImeiMsisdn;
import com.gl.ceir.config.model.ImeiMsisdnIdentity;

public interface DuplicateImeiMsisdnRepository extends JpaRepository<DuplicateImeiMsisdn, ImeiMsisdnIdentity> {

	public List<DuplicateImeiMsisdn> findByImeiMsisdnIdentityImei(Long imei);

}
