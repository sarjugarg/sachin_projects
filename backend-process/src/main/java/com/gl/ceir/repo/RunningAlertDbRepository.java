package com.gl.ceir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RunningAlertDbRepository extends JpaRepository<RunningAlertDb, Long>, JpaSpecificationExecutor<RunningAlertDb> {

	public RunningAlertDb getById(long id);
	
	public RunningAlertDb getByAlertId(String alertId);
}
