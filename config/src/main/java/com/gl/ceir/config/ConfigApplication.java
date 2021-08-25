package com.gl.ceir.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.gl.ceir.config.configuration.FileStorageProperties;
import com.gl.ceir.config.model.DeviceSnapShot;
import com.gl.ceir.config.model.DuplicateImeiMsisdn;
import com.gl.ceir.config.model.ImeiMsisdnIdentity;
import com.gl.ceir.config.model.constants.ImeiStatus; 

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class })
@EnableJpaAuditing
@EnableAutoConfiguration
@EnableCaching
public class ConfigApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ConfigApplication.class, args);
	}

	 
	private static DeviceSnapShot convertRequestToDeviceSnapShot() {
		DeviceSnapShot deviceSnapShot = new DeviceSnapShot();
		deviceSnapShot.setImei(898989L);
		// deviceSnapShot.setFailedRuleId(request.getFailRule().getId().toString());
		// deviceSnapShot.setFailedRuleName(request.getFailRule().getName());
		deviceSnapShot.setDuplicateImeiMsisdns(new ArrayList<>());
		deviceSnapShot.getDuplicateImeiMsisdns().add(convertToDuplicateImeiMsisdn());
		deviceSnapShot.getDuplicateImeiMsisdns().get(0).setDeviceSnapShot(deviceSnapShot);
		return deviceSnapShot;
	}

	private static DuplicateImeiMsisdn convertToDuplicateImeiMsisdn() {
		DuplicateImeiMsisdn duplicateImeiMsisdn = new DuplicateImeiMsisdn();
		duplicateImeiMsisdn.setImeiMsisdnIdentity(new ImeiMsisdnIdentity(898989L, 9090909L));
		duplicateImeiMsisdn.setFileName("file");

		duplicateImeiMsisdn.setImeiStatus(ImeiStatus.AUTO_REGULARIZED);
		duplicateImeiMsisdn.setImsi(3232L);
		duplicateImeiMsisdn.setRegulizedByUser(Boolean.FALSE);
		return duplicateImeiMsisdn;

	}

}
