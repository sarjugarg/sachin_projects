package com.giftingnetwork.service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.giftingnetwork.BootApplication;
import com.giftingnetwork.controller.ScopesController;
import com.giftingnetwork.formatter.XmlDomHandler;
import com.giftingnetwork.model.GenericModel;
import com.giftingnetwork.repo.LoginRepository;
import com.giftingnetwork.repo.RecommendationRepository;
import com.giftingnetwork.util.GenericFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    @Autowired
    RecommendationRepository recommendationRepository;

    @Autowired
    ScopesController scopesController;

    @Autowired
    GenericFunctions genericFunctions;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    GenericModel genericModel;

    @Autowired
    XmlDomHandler xmlDomHandler;
    Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    public String getDetailsById(GenericModel genericModel) {
        logger.info("Getting Details By ID  ");
        scopesController.setDBName("localDb");
    
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String query = null;
        String apiName = genericModel.getApiName();
        StringBuffer sb = new StringBuffer();
        sb.append("Select ");
        sb.append(loginRepository.getParammeterValuesFromApiTable1(map, apiName, apiName)); // get main Table
        query = sb.toString();
        query = genericFunctions.removeLastChars(query, 1);
        query += loginRepository.getExtendedQueryFromDB(apiName, apiName);
        logger.info("Query After Structured :  " + sb.toString());
        query += " where  " + loginRepository.getIdNameByApiFromDb(apiName) + "  = '" + genericModel.getRecommId()
                + "' ";
        query += "     LIMIT 1       OFFSET 0         ";
        genericModel.setSorting("");
        recommendationRepository.authenticatesessionId(genericModel);
        logger.info(" Going to  details  :  " + query);
        return recommendationRepository.getXmlFromQuery(query,   genericModel);
    }

    public String GetValuesBySearchService(GenericModel genericModel) {
        Map<String, String> queryParameters = new HashMap<>();
        Map<String, String> dbParameters = new HashMap<>();
        LinkedHashMap<String, String> apiColumnMap = new LinkedHashMap<String, String>();
        String whereQuery = "         ";
        try {
            if (genericModel.getQueryString().length() > 2) {
                String queryString = genericModel.getQueryString();
                whereQuery = "   WHERE ";
                queryString = URLDecoder.decode(queryString, StandardCharsets.UTF_8.toString());
                logger.info(" queryString :::::" + queryString + ":");
                genericModel.setQueryString(queryString);
                String[] parameters = queryString.split("&");
                for (String parameter : parameters) {
                    String[] keyValuePair = parameter.split("=");
                    logger.info("[" + parameter + " ][" + keyValuePair[0] + "][" + keyValuePair[1] + "]");
                    if (queryParameters.containsKey(keyValuePair[0])) {
                        String oldValue = queryParameters.get(keyValuePair[0]);
                        logger.info("[" + keyValuePair[0] + " ]" + oldValue + "::" + keyValuePair[1]);
                        queryParameters.replace(keyValuePair[0], oldValue + ";" + keyValuePair[1]);
                    } else {
                        queryParameters.put(keyValuePair[0], keyValuePair[1]);
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            genericModel.setHttpStatus(HttpStatus.BAD_REQUEST);
            genericModel.setErrorTitle("client error");
            xmlDomHandler.generateErrorXml1(genericModel);
            return genericModel.getResult();
        }
        scopesController.setDBName("localDb");
        dbParameters = loginRepository.getDbColumnWithParameters(genericModel.getApiName());
        String query = null;
        StringBuffer sb = new StringBuffer();
        sb.append("Select ");
        sb.append(loginRepository.getParammeterValuesFromApiTable(apiColumnMap, genericModel.getApiName()));

        query = sb.toString();
        query = genericFunctions.removeLastChars(query, 1);
        query += loginRepository.getExtendedQueryFromDB(genericModel.getApiName(), genericModel.getApiName());
        String orderBy = " ORDER BY  ";
        String orderByQuery = "";
        int orderByExist = 0;
        String additionQuery = "";


        for (Map.Entry<String, String> entry : queryParameters.entrySet()) {
            String filterName = entry.getKey();
            String filterValue = entry.getValue();
            logger.info("Query Params " + filterName + " : " + filterValue);
            if (!(filterName.contains("operator") || filterName.contains("page") || filterName.contains("offset")
                    || filterName.contains("order_by"))) {

                String filterOperator = queryParameters.get(entry.getKey() + "_operator") == null ? " = "
                        : queryParameters.get(entry.getKey() + "_operator");

                if (! apiColumnMap.containsKey(filterName) ) {
                    logger.info("Going to fetch Addition Query for " + filterName);
                    String result = loginRepository.getadditionMappedQueryByApiAndParent(genericModel.getApiName(),
                            filterName);

                    if (result == null) {
                        genericModel.setHttpStatus(HttpStatus.BAD_REQUEST);
                        genericModel.setErrorTitle(filterName + " not valid");
                        xmlDomHandler.generateErrorXml1(genericModel);
                        return genericModel.getResult();
                    } else {
                        additionQuery += result;
                    }
                    logger.info("Response is " + additionQuery);
                } else {
                    if (!dbParameters.containsKey(filterName)) {    // searchable = 'Y'
                        genericModel.setHttpStatus(HttpStatus.BAD_REQUEST);
                        genericModel.setErrorTitle(filterName + " not valid");
                        xmlDomHandler.generateErrorXml1(genericModel);
                        return genericModel.getResult();
                    }
                }


                String FieldName =   loginRepository.getColumnNameByParent(genericModel.getApiName(),  entry.getKey()  )   ;

                logger.info("Query Operator " + FieldName + "::" + filterOperator + " for " + entry.getKey() + " :: "
                        + filterValue);
                whereQuery += genericFunctions.getQueryStringFromOpertor(FieldName, filterOperator, filterValue)
                        + " and    ";

            }

            if (filterName.contains("order_by")) {
                orderByExist = 1;
                if (!dbParameters.containsKey(filterName.substring(9, filterName.length() - 1))) {
                    genericModel.setHttpStatus(HttpStatus.BAD_REQUEST);
                    genericModel.setErrorTitle(filterName.substring(9, filterName.length() - 1) + " not correct");
                    xmlDomHandler.generateErrorXml1(genericModel);
                    return genericModel.getResult();
                 }
                orderByQuery += filterName.substring(9, filterName.length() - 1) + " " + filterValue + " ,";
                 String FieldName = apiColumnMap.get(filterName.substring(9, filterName.length() - 1));
                orderBy += FieldName + " " + filterValue + " ,";
            }
        }

        whereQuery = genericFunctions.removeLastChars(whereQuery, 7);
        orderBy = genericFunctions.removeLastChars(orderBy, 1);
        // orderByQuery = GenericFunctions.removeLastChars(orderByQuery, 1);
        genericModel.setSorting("");
        query += additionQuery;
        query += whereQuery;
        if (orderByExist == 1) {
            query += orderBy;
        }

        String pageSize = queryParameters.containsKey("page_size") ? Integer
                .parseInt(queryParameters.get("page_size")) < Integer.parseInt(BootApplication.DefaultMinPageSize)
                        ? BootApplication.DefaultMinPageSize
                        : Integer.parseInt(queryParameters.get("page_size")) > Integer
                                .parseInt(BootApplication.DefaultMaxPageSize) ? BootApplication.DefaultMaxPageSize
                                        : queryParameters.get("page_size")
                : BootApplication.DefaultPageSize;
        ;

        int offset = queryParameters.containsKey("page")
                ? (Integer.parseInt(queryParameters.get("page")) - 1) * Integer.parseInt(pageSize)
                : queryParameters.containsKey("offset") ? Integer.parseInt(queryParameters.get("offset")) : 0;
        logger.info("PageSize: " + pageSize + "  & Page offset:" + offset);
        query += "     LIMIT " + pageSize + "      OFFSET " + offset + "         ";

        recommendationRepository.authenticatesessionId(genericModel);
        logger.info("Query : " + query);

        return recommendationRepository.getXmlFromQuery(query,   genericModel);
    }
 
}
