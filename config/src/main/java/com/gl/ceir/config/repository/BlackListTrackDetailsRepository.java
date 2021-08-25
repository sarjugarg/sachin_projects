package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.BlacklistDbHistory;

public interface BlackListTrackDetailsRepository extends JpaRepository<BlacklistDbHistory, Long> {


	public BlacklistDbHistory save(BlacklistDbHistory blackListTrackDetails);


}
