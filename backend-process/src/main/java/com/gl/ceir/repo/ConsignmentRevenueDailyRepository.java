package com.gl.ceir.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.entity.ConsignmentRevenueDailyDb;
import com.gl.ceir.entity.Notification;

@Repository
public interface ConsignmentRevenueDailyRepository extends JpaRepository<ConsignmentRevenueDailyDb, Long>, 
JpaSpecificationExecutor<ConsignmentRevenueDailyDb>{

}
