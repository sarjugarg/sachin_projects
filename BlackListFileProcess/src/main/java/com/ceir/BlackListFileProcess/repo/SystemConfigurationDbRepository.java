package com.ceir.BlackListFileProcess.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ceir.BlackListFileProcess.model.SystemConfigurationDb;


@Repository
public interface SystemConfigurationDbRepository extends JpaRepository<SystemConfigurationDb, Long> {


	public SystemConfigurationDb getByTag(String tag);

	public SystemConfigurationDb getById(Long id);


}
