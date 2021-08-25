package com.gl.ceir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.entity.DeviceUsageDb;

@Repository
public interface DeviceUsageDbRepository extends JpaRepository<DeviceUsageDb, Long>, 
JpaSpecificationExecutor<DeviceUsageDb> {
	

}
