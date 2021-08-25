package com.ceir.GreyListProcess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ceir.GreyListProcess.model.GreylistDbHistory;


public interface GreyListTrackRepository extends JpaRepository<GreylistDbHistory, Long> , JpaSpecificationExecutor<GreylistDbHistory> {


	public GreylistDbHistory save(GreylistDbHistory greyListTracDetails);

}
