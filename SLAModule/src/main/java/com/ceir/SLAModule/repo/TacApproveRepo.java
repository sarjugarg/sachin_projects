package com.ceir.SLAModule.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceir.SLAModule.entity.TypeApprovedTAC;

public interface TacApproveRepo extends JpaRepository<TypeApprovedTAC, Long>{

	public List<TypeApprovedTAC> findByStatus(Integer status);
}
