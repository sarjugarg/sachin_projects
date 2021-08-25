package com.gl.ceir.config.repository;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.gl.ceir.config.model.GsmaValueModel;
import com.gl.ceir.config.model.UsagesValueModel;

	

public  interface GsmaValueRepository extends JpaRepository<GsmaValueModel, Long>, JpaSpecificationExecutor<GsmaValueModel>  {

//	 public List<modelRepoPojo> getByIdOrderByIdDesc(Long id);


	public GsmaValueModel getByDeviceId(int device_id);



	

}

