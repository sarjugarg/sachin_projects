package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gl.ceir.config.model.GrievanceHistory;

public interface GrievanceHistoryRepository extends JpaRepository<GrievanceHistory, Long>, JpaSpecificationExecutor<GrievanceHistory> {
	public GrievanceHistory save(GrievanceHistory history);
}
