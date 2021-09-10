package com.ceir.SLAModule.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceir.SLAModule.entity.SlaReport;

public interface SlaRepo extends JpaRepository<SlaReport, Long>{
 
	
	
}
