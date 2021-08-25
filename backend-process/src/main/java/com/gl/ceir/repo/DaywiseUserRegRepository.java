package com.gl.ceir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.entity.DaywiseUserReg;

@Repository
public interface DaywiseUserRegRepository extends JpaRepository<DaywiseUserReg, Long>, 
JpaSpecificationExecutor<DaywiseUserReg> {
	
	public DaywiseUserReg getByImei(String imei);

}
