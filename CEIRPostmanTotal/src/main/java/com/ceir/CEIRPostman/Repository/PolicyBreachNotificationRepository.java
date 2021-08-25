/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 
/**
 *
 * @author maverick
 */
 



package com.ceir.CEIRPostman.Repository;
import java.util.List; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query; 

import com.ceir.CEIRPostman.model.PolicyBreachNotification;

public interface PolicyBreachNotificationRepository extends JpaRepository<PolicyBreachNotification, Long>, JpaSpecificationExecutor<PolicyBreachNotification>{

	@Query("select noti from PolicyBreachNotification noti where noti.status=?1 and upper(noti.channelType)=upper(?2)")
	public List<PolicyBreachNotification> findByStatusAndChannelType(int status,String channelType);
	public List<PolicyBreachNotification> findByStatus(int status);
}
