package com.giftingnetwork.api;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    JavaXmlDecoder javaXmlDecoder;

    @Autowired
    JavaJsonDecoder javaJsonDecoder;
    @Autowired
    ScopesController scopesController;
    @Autowired
    TokenService tokenService;

    @Autowired
    XmlDomHandler xmlDomHandler;
    Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    public String FindRecommendationService(String xmlString, String apiName, String sessionId) {
          scopesController.setDBName("localDb");
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        Set<String> whereSet = new HashSet<String>();
        String result = "";
        logger.info("Going to Fetch Data According to " + scopesController.getAcceptType());
        if (scopesController.getContentType().contains("json")) {
            result = javaJsonDecoder.decoderInit(xmlString, map, apiName, whereSet);
        } else {
            result = javaXmlDecoder.decoderInit(xmlString, map, apiName, whereSet);
        }
        if (!result.contains("Select")) {
            return xmlDomHandler.genericErrorMethod("client error", "syntax error");
        }
        int assignedPageSize = Integer.parseInt(BootApplication.MaxPageSize);
        int limitValue = Integer
                .parseInt(result.substring(result.indexOf("LIMIT") + 5, result.indexOf("LIMIT") + 9).trim());
        logger.info("Assigned Page Size in db  :  " + assignedPageSize + " : Current value in query " + limitValue);
        if (limitValue > assignedPageSize) {
            logger.error("Page Size Limit Exceeds");
           result =  result.replace( ""+limitValue , ""+assignedPageSize);
         //   return xmlDomHandler.genericErrorMethod("client error", "page size limit exceeds");
        }
        String resultValidator = recommendationRepository.selectWhereFilterValidator(map, whereSet, apiName);
        logger.info(" Result ::  " + resultValidator);
        if (!resultValidator.equals("true")) {
            return resultValidator;
        } else {
            tokenService.authenticatesessionId(sessionId);
            return recommendationRepository.getXmlFromQuery(result, map);
        }
    }

}
