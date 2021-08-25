package com.gl.ceir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gl.ceir.entity.ConsignmentMgmt;

public interface ConsignmentRepository extends JpaRepository<ConsignmentMgmt, Long>, 
JpaSpecificationExecutor<ConsignmentMgmt>{

}
