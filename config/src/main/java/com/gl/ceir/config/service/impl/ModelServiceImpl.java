package com.gl.ceir.config.service.impl;

import java.util.List;
import java.util.Optional;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.brandRepoModel;
import com.gl.ceir.config.model.modelRepoPojo;
import com.gl.ceir.config.repository.ModelRepository;

@Service
public class ModelServiceImpl {
	private static final Logger logger = Logger.getLogger(ModelServiceImpl.class);

	//private final Path fileStorageLocation;

	@Autowired
	ModelRepository modelRepository;

	public List<modelRepoPojo> getAll(int brandNameId) {
		try {
			logger.info("Going to get All Model  List for id;; "+ brandNameId);
			return modelRepository.getByBrandNameIdOrderByModelNameAsc(brandNameId);
//                             getByBrandNameId
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}

	}

}
