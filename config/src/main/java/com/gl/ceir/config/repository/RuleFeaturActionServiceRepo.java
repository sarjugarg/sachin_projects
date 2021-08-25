/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.ceir.config.repository;
 
import com.gl.ceir.config.model.RuleFeatureActionMapping;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author maverick
 */
 



public  interface RuleFeaturActionServiceRepo extends JpaRepository<RuleFeatureActionMapping, Long>, JpaSpecificationExecutor<RuleFeatureActionMapping>  {

 
     @Query("select distinct featureName from RuleFeatureActionMapping where ruleName = ?1 ")
     public List<String> getByRuleName(String ruleName );

     @Query("select distinct ruleName from RuleFeatureActionMapping where featureName = ?1 ")
     public List<String> getByFeatureName(String featureName);

     public List<RuleFeatureActionMapping> getByFeatureNameAndRuleName(String featureName, String ruleName);



	

}
