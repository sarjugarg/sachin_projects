package com.ceir.GreyListProcess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ceir.GreyListProcess.model.WebActionDb;
public interface WebActionDbRepository extends JpaRepository<WebActionDb, Long> , JpaSpecificationExecutor<WebActionDb>
 {

	@SuppressWarnings("unchecked")
	public WebActionDb save(WebActionDb webActionDb);

	public WebActionDb getByState(int state);

	public WebActionDb findFirstByState(int state);
	
	public boolean existsByFeature(String feature);
}
 