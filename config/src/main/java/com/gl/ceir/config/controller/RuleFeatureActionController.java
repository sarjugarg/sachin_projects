/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.ceir.config.controller;

import com.gl.ceir.config.model.RuleFeatureActionMapping;
import com.gl.ceir.config.service.impl.RuleFeaturActionServiceImpl;
import io.swagger.annotations.ApiOperation;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author maverick
 */
@RestController

public class RuleFeatureActionController { //sachin

     private static final Logger logger = Logger.getLogger(RuleFeatureActionController.class);

     @Autowired
     RuleFeaturActionServiceImpl ruleFeaturActionServiceImpl;

     @ApiOperation(value = "Rule FeatureAction Mapping", response = RuleFeatureActionMapping.class)
     @PostMapping(path = "rule/getRuleFeaturAction")
     public MappingJacksonValue getRuleFeaturAction(String featureName, String ruleName) {
          List<RuleFeatureActionMapping> rslt = new LinkedList<RuleFeatureActionMapping>();
          rslt = ruleFeaturActionServiceImpl.getactionbyRuleFeature(ruleName, featureName);
          MappingJacksonValue mapping = new MappingJacksonValue(rslt);
          logger.info("Response of View =" + mapping.toString() + "  ** " + rslt);
          return mapping;

     }

     @ApiOperation(value = "  Get Feature by RuleName", response = RuleFeatureActionMapping.class)
     @PostMapping(path = "rule/GetfeaturebyRuleName")
     public MappingJacksonValue getfeaturebyRuleName(String ruleName) {
          MappingJacksonValue mapping = new MappingJacksonValue(ruleFeaturActionServiceImpl.getfeaturebyRuleName(ruleName));
          return mapping;
     }

     @ApiOperation(value = "  Get Rule by FeatureName", response = RuleFeatureActionMapping.class)
     @PostMapping(path = "rule/GetRulebyFeatureName")
     public MappingJacksonValue getRulebyFeatureName(String featureName) {
          MappingJacksonValue mapping = new MappingJacksonValue(ruleFeaturActionServiceImpl.getRulebyFeatureName(featureName));
          return mapping;
     }

}
