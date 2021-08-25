package com.ceir.GreyListProcess.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceir.GreyListProcess.model.SystemConfigurationDb;


public interface SystemConfigurationDbRepository extends JpaRepository<SystemConfigurationDb, Long> {


	public SystemConfigurationDb getByTag(String tag);

	public SystemConfigurationDb getById(Long id);


}
