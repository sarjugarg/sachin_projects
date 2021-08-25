package com.ceir.GreyListProcess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ceir.GreyListProcess.model.GreylistDb;


public interface GreyListRepository extends JpaRepository<GreylistDb, Long>, JpaSpecificationExecutor<GreylistDb> {

	public GreylistDb save (GreylistDb greyList);

	public void deleteByImei(Long imei);
 
	
	
	
	

}
