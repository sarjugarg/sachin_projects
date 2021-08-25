package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.ConfigurationManagement;

public interface ConfigurationManagementRepository extends JpaRepository<ConfigurationManagement, Long> {


	public ConfigurationManagement getByName(String name);




}
