package com.ceir.SLAModule.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceir.SLAModule.entity.Grievance;

public interface GrievanceRepo extends JpaRepository<Grievance, Long>{

	public List<Grievance> findByGrievanceStatus(int status);
}
