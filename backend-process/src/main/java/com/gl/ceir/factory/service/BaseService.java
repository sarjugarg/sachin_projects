package com.gl.ceir.factory.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.gl.ceir.configuration.PropertiesReader;
import com.gl.ceir.entity.SystemConfigurationDb;
import com.gl.ceir.notifier.NotifierWrapper;
import com.gl.ceir.repo.SystemConfigurationDbRepository;
import com.gl.ceir.service.AlertServiceImpl;

public abstract class BaseService implements Service{

	protected Map<String, SystemConfigurationDb> systemConfigMap = new HashMap<>();
	
	protected PropertiesReader propertiesReader;
	
	protected AlertServiceImpl alertServiceImpl;
	
	protected NotifierWrapper notifierWrapper;
	
	protected SystemConfigurationDbRepository systemConfigurationDbRepository;

	@Override
	public void fetchAndProcess() {
		fetch();
	}
	
	@Override
	public void onErrorRaiseAnAlert(String alertId, Map<String, String> bodyPlaceHolderMap) {
		alertServiceImpl.raiseAnAlert(alertId, 0, bodyPlaceHolderMap);
	}

	public PropertiesReader getPropertiesReader() {
		return propertiesReader;
	}

	@Autowired
	public final void setPropertiesReader(PropertiesReader propertiesReader) {
		this.propertiesReader = propertiesReader;
	}

	@Autowired
	public void setAlertServiceImpl(AlertServiceImpl alertServiceImpl) {
		this.alertServiceImpl = alertServiceImpl;
	}

	@Autowired
	public void setNotifierWrapper(NotifierWrapper notifierWrapper) {
		this.notifierWrapper = notifierWrapper;
	}

	@Autowired
	public void setSystemConfigurationDbRepository(SystemConfigurationDbRepository systemConfigurationDbRepository) {
		this.systemConfigurationDbRepository = systemConfigurationDbRepository;
	}
	
	
	

	
}
