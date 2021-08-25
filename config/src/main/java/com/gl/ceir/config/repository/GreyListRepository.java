package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gl.ceir.config.model.GreylistDb;

public interface GreyListRepository extends JpaRepository<GreylistDb, Long>, JpaSpecificationExecutor<GreylistDb> {

	public GreylistDb save (GreylistDb greyList);

	public void deleteByImei(Long imei);
 
	
	
	
	

}
