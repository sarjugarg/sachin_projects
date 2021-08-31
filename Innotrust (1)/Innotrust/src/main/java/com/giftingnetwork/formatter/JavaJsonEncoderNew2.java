package com.giftingnetwork.formatter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import com.giftingnetwork.controller.ScopesController;
import com.giftingnetwork.model.GenericModel;
import com.giftingnetwork.repo.LoginRepository;
import com.giftingnetwork.util.GenericFunctions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;

@Service
public class JavaJsonEncoderNew2 {

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    ScopesController scopesController;

    @Autowired
    XmlDomHandler xmlDomHandler;

    @Autowired
    GenericFunctions genericFunctions;

    // Logger // logger = LoggerFactory.getLogger(JavaJsonEncoderNew2.class);

    Map<String, String> parentChildmap = new LinkedHashMap<String, String>();;
    Map<String, String> childSubChildMap = new LinkedHashMap<String, String>();
    Map<String, String> columnNameValuesMap = new LinkedHashMap<String, String>();
    Map<String, String> propertyTypeMapValues = new LinkedHashMap<String, String>();
    Map<String, String> apiObjectQueries = new LinkedHashMap<String, String>();

    String apiName;
    String apiId;

    public String toDocument(ResultSet rs,   String query, GenericModel genericModel) {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            // logger.info("End  Time " +  localDateTime);
            // logger.info("Json Encoder Start:   [ " + query + " ]");
            String totalCount = loginRepository.getTotalCountFromDb(query);
            if (query.contains("ORDER")) {
                query.substring(query.indexOf("ORDER BY") + 9, query.indexOf("LIMIT") + 0).trim();
            }
            apiName = genericModel.getApiName();
            String dbName = scopesController.getDBName();
            scopesController.setDBName("localDb");
            JSONArray jsonArr = new JSONArray();
            String Offset = query.substring(query.indexOf("OFFSET") + 6, query.indexOf("OFFSET") + 11);
            String limit = query.substring(query.indexOf("LIMIT") + 5, query.indexOf("LIMIT") + 9);
            int pageSize = ((Integer.parseInt(Offset.trim()) / Integer.parseInt(limit.trim())) + 1);
            // parentChildmap = loginRepository.getParentMapping(apiName);
         //   childSubChildMap = loginRepository.getChildMapping(apiName);
            propertyTypeMapValues = loginRepository.propertyTypeMapValues(apiName);
            loginRepository.apiObjectValueDetails(apiName, parentChildmap, childSubChildMap);
            loginRepository.getObjectQueries(apiName, parentChildmap, apiObjectQueries);
            // for (Map.Entry<String, String> entry : parentChildmap.entrySet()) {
            //     // logger.info(" PARENT : Key = " + entry.getKey() + " Value = " + entry.getValue());
            // }
            // for (Map.Entry<String, String> entry : childSubChildMap.entrySet()) {
            //     // logger.info(" CHILD : Key = " + entry.getKey() + " Value = " + entry.getValue());
            // }
            // for (Map.Entry<String, String> entry : apiObjectQueries.entrySet()) {
            //     // logger.info(" Queries for header = " + entry.getKey() + "  ; Values = " + entry.getValue());
         //   }

            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
             int tentativeCount = 0;
            scopesController.setDBName(dbName);
            JSONObject selector = new JSONObject();
            while (rs.next()) {
                for (int i = 1; i <= numColumns; i++) {
                    columnNameValuesMap.put(rsmd.getColumnName(i),
                            rs.getObject(i) == null ? " " : rs.getObject(i).toString());
                }
                for (Map.Entry<String, String> entry : columnNameValuesMap.entrySet()) {
                    // // logger.info(" Columns : Key = " + entry.getKey() + " Value =" +
                    // entry.getValue() + ";");
                    if (propertyTypeMapValues.containsKey(entry.getKey())) { //// Properties Fromat Changes
                        // // logger.info(" column " + entry.getKey());
                        try {
                            if (propertyTypeMapValues.get(entry.getKey()).equals("M")) {
                                apiId = entry.getValue();
                            }
                            String newValue = entry.getValue();
                            if (propertyTypeMapValues.get(entry.getKey()).equals("T")) {
                                newValue = new SimpleDateFormat("MM-dd-yyyy")
                                        .format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(entry.getValue()));
                                // // logger.info(" Changes For TimeStamp Key = " + entry.getKey() + " Value = " +
                                // newDate);
                            }
                            if (propertyTypeMapValues.get(entry.getKey()).equals("D")) {
                                newValue = new SimpleDateFormat("MM-dd-yyyy")
                                        .format(new SimpleDateFormat("yyyy-MM-dd").parse(entry.getValue()));
                            }
                            if (propertyTypeMapValues.get(entry.getKey()).equals("TX")) {
                            //    // logger.info(" Changes For Date Key = " + entry.getKey() + "  ");
                                newValue = entry.getValue().toString().replace("/", "-");
                            }
                            if (propertyTypeMapValues.get(entry.getKey()).equals("A")) {
                              //  // logger.info(entry.getKey() + " NewValue Key " + entry.getValue());
                                newValue = new DecimalFormat("#,###,##0.00").format(Double.valueOf(entry.getValue()));
                             //   // logger.info("NewValue Key = " + entry.getKey() + " Value = " + newValue);
                            }

                            columnNameValuesMap.replace(entry.getKey(), newValue);
                        } catch (Exception e) {
                            // logger.error("Not parsable:" + e.getMessage() + " for : " + entry.getKey());
                        }
                    }
                }

                selector = jsonFrameWork(genericModel.getApiName());
                jsonArr.put(selector);
                tentativeCount++;

            }
            Boolean has_more = false;
            if ((Integer.parseInt(limit.trim()) * pageSize) < Integer.parseInt(totalCount)) {
                has_more = true;
            }
            JSONObject paginators = new JSONObject();
            try {
                Field changeMap = paginators.getClass().getDeclaredField("map");
                changeMap.setAccessible(true);
                changeMap.set(paginators, new LinkedHashMap<>());
                changeMap.setAccessible(false);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                // logger.error(e.getMessage());
            }
            paginators.put("count", tentativeCount);
            paginators.put("page_number", pageSize);
            paginators.put("page_size", limit.trim());
            paginators.put("total_row", totalCount);
            paginators.put("has_more", has_more);

            JSONObject result = new JSONObject();
            Field changeMap = result.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(result, new LinkedHashMap<>());
            changeMap.setAccessible(false);
            if (genericModel.getApiType().equals("search")) {
                result.put("query", genericModel.getQueryString());
                result.put(genericModel.getApiName(), jsonArr); // if count == 1 //selector
                result.put("paginators", paginators);
            } else {
                result.put(genericModel.getApiName(), selector);
            }
            JSONObject data = new JSONObject();
            data.put("result", result);
            data.put("status", 1);
            String message = data.toString().replace("fund_history_id", "gift_id ").replace("fund_grant_history_id",
                    "grant_id");    // if more to go ; get Param Values  // get via propertyTypeMapValues map
            genericModel.setHttpStatus(HttpStatus.OK);
            if (genericModel.getAcceptType().contains("xml")) {
                message = genericFunctions.jsonToXmlConverter(message);
            }
            return message;
        } catch (Exception e) {
            // logger.error(e.getCause() + ":error: " + e.getMessage());
            genericModel.setErrorDetail("Server Error");
            genericModel.setErrorTitle("Internal Error");
            genericModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            xmlDomHandler.generateErrorXml1(genericModel);
            return genericModel.getResult();
        }
    }

    public JSONObject jsonFrameWork(String header) {
        JSONObject apiValues2 = new JSONObject();
        try {
            Field changeMap = apiValues2.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(apiValues2, new LinkedHashMap<>());
            changeMap.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            // logger.error(e.getMessage());
        }
        if (apiName.equals(header)) {
            String[] parentArry = (parentChildmap.containsKey(header)) ? parentChildmap.get(header).split(",") : null;
            for (int j = 0; j < parentArry.length; j++) {
               //   // logger.info(" !!!! " + parentArry[j] + " values " +
               //  columnNameValuesMap.get(parentArry[j]));
                apiValues2.put(parentArry[j], columnNameValuesMap.get(parentArry[j]));
            }
            if (apiName.equals("fund-statement")) {
                loginRepository.getDynamicValuesForFund(apiValues2, apiId);
            }
        }
        String[] childArr = (childSubChildMap.containsKey(header)  ) ? childSubChildMap.get(header).split(",")
                : new String[0] ;
       //   // logger.info("Fetching New Object With Multiple Details " +  childArr.length );
        
          for (int i = 0; i < childArr.length; i++) {
        //    // logger.info("  Going to  fetch next Details  " + childArr[i] );
            header = childArr[i];
            JSONObject apiValues3 = new JSONObject();
            try {
                Field changeMap = apiValues3.getClass().getDeclaredField("map");
                changeMap.setAccessible(true);
                changeMap.set(apiValues3, new LinkedHashMap<>());
                changeMap.setAccessible(false);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                // logger.error(e.getMessage());
            }
            apiValues3 = loginRepository.getRecordsFromDb(apiName, header, apiId, childSubChildMap, apiObjectQueries  );
            for (String key : JSONObject.getNames(apiValues3)) {
                Object value = apiValues3.get(key);
                 if (!apiValues2.has(key)) {
                    apiValues2.put(key, value);
                }
            }
        }
        return apiValues2;
    }

}
