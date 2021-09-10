package com.ceir.SLAModule.repo;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ceir.SLAModule.entity.SystemConfigurationDb;


public interface SysConfigDbRepo extends JpaRepository<SystemConfigurationDb, Long> {
	public SystemConfigurationDb findByTag(String tag);
}
