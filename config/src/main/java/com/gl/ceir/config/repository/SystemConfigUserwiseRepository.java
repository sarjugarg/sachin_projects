package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.SystemConfigUserwiseDb;

public interface SystemConfigUserwiseRepository extends JpaRepository<SystemConfigUserwiseDb, Long> {
	
	public List<SystemConfigUserwiseDb> findByTagIdAndUserTypeId(String tagId, int userTypeId);
	
	public List<SystemConfigUserwiseDb> findByTagIdAndFeatureId(String tagId, int featureId);
}
