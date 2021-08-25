package com.ceir.BlackListProcess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ceir.BlackListProcess.model.GreylistDb;



public interface GreyListRepository extends JpaRepository<GreylistDb, Long>, JpaSpecificationExecutor<GreylistDb> {

	public GreylistDb save (GreylistDb greyList);

	public void deleteByImei(Long imei);
 
	public void deleteById(long id);
	
	public List<GreylistDb> findAll();
	
	
	
	

}
