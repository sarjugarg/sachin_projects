package com.ceir.BlackListFileProcess.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ceir.BlackListFileProcess.model.BlackList;


@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> , JpaSpecificationExecutor<BlackList> {
	public BlackList findByMsisdn(Long msisdn);

	public BlackList findByImei(String imei);
	
	public BlackList save(BlackList blackList);
	 
}
