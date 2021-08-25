package com.gl.ceir.config.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gl.ceir.config.model.CheckImeiValuesEntity;

import com.gl.ceir.config.model.CheckImeiMess;
import com.gl.ceir.config.service.impl.CheckImeiServiceImpl;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class CheckImeiController {  //sachin

    private static final Logger logger = Logger.getLogger(GsmaValueController.class);

    @Autowired
    CheckImeiServiceImpl checkImeiServiceImpl;

    @ApiOperation(value = "Validate Imei", response = CheckImeiMess.class)
    @PostMapping(path = "cc/CheckImeI")
    public MappingJacksonValue CheckImeiValues(@RequestBody CheckImeiValuesEntity checkImeiValuesEntity) {
        String user_type = checkImeiValuesEntity.getUser_type().trim();
        String feature = checkImeiValuesEntity.getFeature().trim().replaceAll(" ", "");
        String imei = checkImeiValuesEntity.getImei();
        Long imei_type = checkImeiValuesEntity.getImei_type();
        logger.info("Feature   " + feature + user_type);
        logger.info("UsrType   " + user_type);
        logger.info("Imei_type (devIdType)   " + imei_type);
        logger.info("Imei   " + imei);
        CheckImeiMess cImsg = new CheckImeiMess();
        MappingJacksonValue mapping = null;
        String rulePass = checkImeiServiceImpl.getResult(user_type, feature, imei, imei_type);
         logger.info("rulePass Value =" + rulePass);
        if (rulePass.equalsIgnoreCase("true")) {
            cImsg.setErrorMessage("NA");
            cImsg.setStatus("Pass");
            cImsg.setDeviceId(imei.substring(0,8));
        } else {
            cImsg.setErrorMessage(rulePass);
            cImsg.setStatus("Fail");
            cImsg.setDeviceId(imei.substring(0,8));
            
        }
        mapping = new MappingJacksonValue(cImsg);
//        logger.info("Response of View =" + mapping);
        return mapping;
    }

}
