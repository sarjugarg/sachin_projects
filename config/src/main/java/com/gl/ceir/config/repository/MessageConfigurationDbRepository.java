package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gl.ceir.config.model.MessageConfigurationDb;

public interface MessageConfigurationDbRepository extends JpaRepository<MessageConfigurationDb, Long>, JpaSpecificationExecutor<MessageConfigurationDb> {


	public MessageConfigurationDb getByTag(String tagValue);

	public MessageConfigurationDb getById(Long id);

	public MessageConfigurationDb getByTagAndActive(String tagValue, int active);

}

