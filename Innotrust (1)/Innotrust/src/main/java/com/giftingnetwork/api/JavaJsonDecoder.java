package com.giftingnetwork.api;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


@Service
public class JavaJsonDecoder {
    @Autowired
    GenericFunctions javaDefaultConvertors;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    XmlDomHandler xmlDomHandler;
    Logger logger = LoggerFactory.getLogger(JavaJsonDecoder.class);

    public String decoderInit(String content, LinkedHashMap<String, String> map, String apiName, Set<String> whereSet) {
        try {
            JSONObject json = new JSONObject(content);

            String query = " Select   ";
            StringBuffer sb = new StringBuffer();
            sb.append("Select ");

            JSONObject selectBlock = json.getJSONObject("command").optJSONObject("select");
            String FieldName = " ";
            if (selectBlock instanceof JSONObject) {
                JSONArray selectBlockMulti = selectBlock.optJSONArray("field");
                if (selectBlockMulti instanceof JSONArray) {
                    for (int i = 0; i < selectBlockMulti.length(); i++) {
                        FieldName = (selectBlockMulti.getJSONObject(i).getString("name"));
                        logger.info(":  " + FieldName);
                        map.put(FieldName, getOriginalDbValuesWithRespectToApi(FieldName, apiName) + ",");
                        sb.append(getOriginalDbValuesWithRespectToApi(FieldName, apiName) + ",");
                    }
                } else {
                    FieldName = (selectBlock.optJSONObject("field").getString("name"));
                    logger.info(":  " + FieldName);
                    map.put(FieldName, getOriginalDbValuesWithRespectToApi(FieldName, apiName) + ",");
                    sb.append(getOriginalDbValuesWithRespectToApi(FieldName, apiName) + ",");
                }

            } else {
                logger.info("Select Block is Missing");
                sb.append(loginRepository.getParammeterValuesFromApiTable(map, apiName));
                logger.info("All Selected Values " + sb.toString());
                for (String values : map.keySet()) {
                    logger.info("Map Values are : " + values);
                }
            }
            query = sb.toString();
            query = removeLastChars(query, 1); // BootApplication.DBName= "localDb";
            query += loginRepository.getExtendedQueryFromDB(apiName);
            logger.info("Query After Structured :  " + sb.toString());
            String whereQuery = "  ";

            // NodeList whereNode = doc.getElementsByTagName(whereElement);
            // logger.info("Where Condition Length " + whereNode.getLength());

            JSONObject whereBlock = json.getJSONObject("command").optJSONObject("where");
            if (whereBlock instanceof JSONObject) {
                whereQuery = " where ";

                JSONArray whereBlockMulti = whereBlock.optJSONArray("field");
                if (whereBlockMulti instanceof JSONArray) {
                    for (int i = 0; i < whereBlockMulti.length(); i++) {
                        String fieldNameWhere = (whereBlockMulti.getJSONObject(i).getString("name"));
                        String Operator = (whereBlockMulti.getJSONObject(i).getString("operator"));
                        String fieldValue = (whereBlockMulti.getJSONObject(i).getString("content"));
                        logger.info("Name:  " + fieldNameWhere + " ,Operator  " + Operator + " ,fieldValue  "
                                + fieldValue + "");
                        String FieldNameArr = getOriginalDbValuesWithRespectToApi(fieldNameWhere, apiName);
                        whereQuery += javaDefaultConvertors.getQueryStringFromOpertor(FieldNameArr, Operator,
                                fieldValue);
                        if (i != whereBlockMulti.length() - 1 || apiName.equals("getNewApprovedAccountSetupInfo")) {
                            whereQuery += " and ";
                        }
                        whereSet.add(fieldNameWhere);
                    }
                } else {

                    String fieldNameWhere = (whereBlock.optJSONObject("field").getString("name"));
                    String Operator = (whereBlock.optJSONObject("field").getString("operator"));
                    String fieldValue = (whereBlock.optJSONObject("field").getString("content"));
                    logger.info("Name:  " + fieldNameWhere + " ,Operator  " + Operator + " ,fieldValue  " + fieldValue
                            + "");
                    String FieldNameArr = getOriginalDbValuesWithRespectToApi(fieldNameWhere, apiName);
                    whereQuery += javaDefaultConvertors.getQueryStringFromOpertor(FieldNameArr, Operator, fieldValue);
                    if (apiName.equals("getNewApprovedAccountSetupInfo")) {
                        whereQuery += " and ";
                    }
                    whereSet.add(fieldNameWhere);
                }
            }
            if (apiName.equals("getNewApprovedAccountSetupInfo")) {
                whereQuery += "   donor_approval_status = 'Approved' ";
            }

            logger.info("Query  For Where Segment  " + whereQuery);
            query += whereQuery;
            String pageSize = null;
            String pageNumber = null;
            JSONObject pagingBlock = json.getJSONObject("command").optJSONObject("paging");
            if (pagingBlock instanceof JSONObject) {
                String orderBy = " ";

                if (pagingBlock.getString("page_number") != null) {
                    pageNumber = (pagingBlock.getString("page_number"));
                } else {
                    pageNumber = "1";
                }
                if (pagingBlock.getString("page_size") != null) {
                    pageSize = (pagingBlock.getString("page_size"));
                } else {
                      pageSize = BootApplication.MaxPageSize;
                     
                }

                logger.info("PageSize: " + pageSize + "  & Page Number:" + pageNumber);
                int offset = ((Integer.parseInt(pageNumber) - 1) * Integer.parseInt(pageSize));
                logger.info((Integer.parseInt(pageNumber) - 1) + " " + Integer.parseInt(pageSize) + " : * : " + offset);
                if (pagingBlock.getString("order_by") != null) {
                    for (String orderValue : pagingBlock.getString("order_by").split(",")) {
                        orderValue = orderValue.trim();
                        String orderParameter = getOriginalDbValuesWithRespectToApi(orderValue.split(" ")[0], apiName)
                                .split(",")[0];
                        logger.info("Order Parameters :: " + orderParameter);
                        orderBy += orderParameter + " "
                                + (orderValue.split(" ").length == 1 ? " asc " : orderValue.split(" ")[1]) + " ,";
                        logger.info("ORDER by : " + orderBy);
                    }
                    orderBy = removeLastChars(orderBy, 1);
                    logger.info("Query For Order Segment  " + orderBy);
                    query += " ORDER By " + orderBy;
                }
                query += "     LIMIT " + pageSize + "      OFFSET " + offset + "         ";
            } else {
                logger.info("Paging Section Does Not Exists");
                // return xmlDomHandler.genericErrorMethod("Client Error", "Syntax Error");
                pageSize = BootApplication.MaxPageSize;
                pageNumber = "1";
                logger.info("PageSize: " + pageSize + "  & Page Number:" + pageNumber);
                int offset = ((Integer.parseInt(pageNumber) - 1) * Integer.parseInt(pageSize));
                logger.info((Integer.parseInt(pageNumber) - 1) + " " + Integer.parseInt(pageSize) + " : * : " + offset);
                query += "     LIMIT " + pageSize + "      OFFSET " + offset + "         ";

            }
            logger.info("Final Query   [   " + query + "  ] ");
            return query;
        } catch (Exception e) {
            logger.error("Decoder Exception  : " + e.getMessage());
            return xmlDomHandler.genericErrorMethod("Client Error", "Syntax Error");
        }
    }

    public static Document loadXMLFromString(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    String getOriginalDbValuesWithRespectToApi(String fieldName, String apiName) {
        return loginRepository.getColumnNameWithParameters(apiName, fieldName);
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }

}
