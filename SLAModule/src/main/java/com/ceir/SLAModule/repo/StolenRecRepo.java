package com.ceir.SLAModule.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceir.SLAModule.entity.StolenandRecoveryMgmt;

public interface StolenRecRepo extends JpaRepository<StolenandRecoveryMgmt,Long>{

	public List<StolenandRecoveryMgmt> findByFileStatusAndRequestType(Integer status,Integer requestType);
}
