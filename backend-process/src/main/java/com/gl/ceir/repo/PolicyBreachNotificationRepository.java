package com.gl.ceir.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.entity.PolicyBreachNotification;

@Repository
public interface PolicyBreachNotificationRepository extends JpaRepository<PolicyBreachNotification, Long>, 
JpaSpecificationExecutor<PolicyBreachNotification>{
	
	public Page<PolicyBreachNotification> findByImei(String imei, Pageable pageable);
	
	public Page<PolicyBreachNotification> findByContactNumber(Long contactNumber, Pageable pageable);

}
