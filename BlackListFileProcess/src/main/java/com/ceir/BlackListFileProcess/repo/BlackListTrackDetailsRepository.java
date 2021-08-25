package com.ceir.BlackListFileProcess.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ceir.BlackListFileProcess.model.BlackList;
import com.ceir.BlackListFileProcess.model.BlacklistDbHistory;


@Repository
public interface BlackListTrackDetailsRepository extends JpaRepository<BlacklistDbHistory, Long>,JpaSpecificationExecutor<BlacklistDbHistory> {


	public BlacklistDbHistory save(BlacklistDbHistory blackListTrackDetails);

}
