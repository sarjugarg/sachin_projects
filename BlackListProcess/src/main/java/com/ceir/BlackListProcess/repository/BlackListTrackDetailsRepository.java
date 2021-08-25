package com.ceir.BlackListProcess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ceir.BlackListProcess.model.BlacklistDbHistory;
import com.ceir.BlackListProcess.model.GreylistDb;


public interface BlackListTrackDetailsRepository extends JpaRepository<BlacklistDbHistory, Long> , JpaSpecificationExecutor<BlacklistDbHistory> {


	public BlacklistDbHistory save(BlacklistDbHistory blackListTrackDetails);

}
