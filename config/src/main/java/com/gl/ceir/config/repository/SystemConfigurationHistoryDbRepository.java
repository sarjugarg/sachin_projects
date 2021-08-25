package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gl.ceir.config.model.SystemConfigurationHistoryDb;

public interface SystemConfigurationHistoryDbRepository extends JpaRepository<SystemConfigurationHistoryDb, Long>,JpaSpecificationExecutor<SystemConfigurationHistoryDb> {

}
