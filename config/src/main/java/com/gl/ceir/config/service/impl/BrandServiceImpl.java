package com.gl.ceir.config.service.impl;





import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.ceir.config.exceptions.ResourceServicesException;
import com.gl.ceir.config.model.brandRepoModel;
import com.gl.ceir.config.repository.brandRepository;
import org.springframework.data.domain.Sort;

@Service
public class BrandServiceImpl {

	@Autowired
	private brandRepository brandRepository;

	private static final Logger logger = Logger.getLogger(BrandServiceImpl.class);

	public List<brandRepoModel> getAllBrands() {
		try {
                                   
			logger.info("Going to get All Brand List ");
			return brandRepository.findAll(Sort.by(Sort.Direction.ASC, "brandName" ));
                     } catch (Exception e) {

			logger.error(e.getMessage(), e);
			throw new ResourceServicesException(this.getClass().getName(), e.getMessage());
		}
                
//                
//                 public Sort sort(){
//                        return new Sort();
//                        }
                             

	}
	//private final Path fileStorageLocation;

}
