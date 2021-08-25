package com.gl.ceir.config.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gl.ceir.config.model.RuleEngineMapping;



public  interface CheckImeiRepository extends JpaRepository<RuleEngineMapping,Long>, JpaSpecificationExecutor<RuleEngineMapping>  {



	public List<RuleEngineMapping> getByFeatureAndUserTypeOrderByRuleOrder(String feature,String user_type);


}

