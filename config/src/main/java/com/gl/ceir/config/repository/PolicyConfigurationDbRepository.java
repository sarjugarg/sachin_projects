package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gl.ceir.config.model.PolicyConfigurationDb;

public interface PolicyConfigurationDbRepository extends JpaRepository<PolicyConfigurationDb, Long>, JpaSpecificationExecutor<PolicyConfigurationDb> {

	public 	PolicyConfigurationDb getByTag(String tag);

	public PolicyConfigurationDb getById(Long id);

}
