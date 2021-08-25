package com.gl.ceir.config.service.impl;

import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.GenricResponse;
import com.gl.ceir.config.model.StackholderPolicyMapping;
import com.gl.ceir.config.repository.StackholderPolicyMappingRepository;

@Service
public class StackholderPolicyMappingServiceImpl {

	private static final Logger logger = Logger.getLogger(StackholderPolicyMappingServiceImpl.class);

	@Autowired
	StackholderPolicyMappingRepository stackholderPolicyMappingRepository;

	public StackholderPolicyMapping getPocessListConfigDetails(StackholderPolicyMapping StackholderPolicyMapping) {
		try {
			return stackholderPolicyMappingRepository.getByListType(StackholderPolicyMapping.getListType());

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public GenricResponse updateProcessPolicy(StackholderPolicyMapping StackholderPolicyMapping) {
		try {

			stackholderPolicyMappingRepository.save(StackholderPolicyMapping);
			return new GenricResponse(0,"Update Sucessfully","");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
	}

	public List<StackholderPolicyMapping> getFileControllingDetails(){

		return 	stackholderPolicyMappingRepository.findByListTypeOrListType("BlackList", "GreyList");


	}	

}
