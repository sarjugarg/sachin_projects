package com.ceir.SLAModule.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceir.SLAModule.entity.ConsignmentMgmt;

public interface ConsignmentRepo extends JpaRepository<ConsignmentMgmt, Long>{

	public List<ConsignmentMgmt> findByConsignmentStatus(int status);
}
