package com.ceir.BlackListProcess.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ceir.BlackListProcess.model.SystemConfigurationDb;

public interface SystemConfigurationDbRepository extends JpaRepository<SystemConfigurationDb, Long> {
	public SystemConfigurationDb getByTag(String tag);
	public SystemConfigurationDb getById(Long id);
}
