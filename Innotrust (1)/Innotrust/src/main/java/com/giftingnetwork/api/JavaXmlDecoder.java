package com.giftingnetwork.api;

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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

@Service
public class JavaXmlDecoder {

    @Autowired
    GenericFunctions javaDefaultConvertors;

    @Autowired
    ScopesController scopesController;

    @Autowired
    XmlDomHandler xmlDomHandler;

    @Autowired
    LoginRepository loginRepository;
    Logger logger = LoggerFactory.getLogger(JavaXmlDecoder.class);

    public String decoderInit(String content, LinkedHashMap<String, String> map, String apiName, Set<String> whereSet) {
        try {

            logger.info("Xml Decoder Started for " + scopesController.getRefCode());
            Document doc = loadXMLFromString(content);
            doc.getDocumentElement().normalize();
            String pointElement = "select";
            NodeList selectNode = doc.getElementsByTagName(pointElement);
            String query = " Select   ";
            StringBuffer sb = new StringBuffer();
            sb.append("Select ");
            if (selectNode.getLength() == 0) {
                logger.info("Select Block is Missing");
                sb.append(loginRepository.getParammeterValuesFromApiTable(map, apiName));
                logger.info("All Selected Values " + sb.toString());
                for (String values : map.keySet()) {
                    logger.info("Map Values are : " + values);
                }
            }

            for (int p = 0; p < selectNode.getLength(); p++) {
                Node node = selectNode.item(p);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    NodeList selectNodeList = element.getElementsByTagName("field");
                    String FieldName = " ";
                    for (int j = 0; j < selectNodeList.getLength(); j++) {
                        FieldName = selectNodeList.item(j).getAttributes().getNamedItem("name").getTextContent();
                        logger.info(pointElement + ":  " + FieldName);
                        map.put(FieldName, getOriginalDbValuesWithRespectToApi(FieldName, apiName) + ",");
                        sb.append(getOriginalDbValuesWithRespectToApi(FieldName, apiName) + ",");
                    }
                }
            }
            query = sb.toString();
            query = removeLastChars(query, 1); // BootApplication.DBName= "localDb";
            query += loginRepository.getExtendedQueryFromDB(apiName);
            logger.info("Query After Structured :  " + sb.toString());
            String whereQuery = "  ";
            String whereElement = "where";
            NodeList whereNode = doc.getElementsByTagName(whereElement);
            logger.info("Where Node Length " + whereNode.getLength());
            for (int k = 0; k < whereNode.getLength(); k++) {
                Node node1 = whereNode.item(k);
                if (node1.getNodeType() == Node.ELEMENT_NODE) {
                    whereQuery = " where ";
                    Element element1 = (Element) node1;
                    NodeList whereNodeList = element1.getElementsByTagName("field");
                    logger.info(" List  Length " + whereNodeList.getLength());
                    for (int x = 0; x < whereNodeList.getLength(); x++) {
                        logger.info("Inside Where Block:  ");
                        String FieldName1 = whereNodeList.item(x).getAttributes().getNamedItem("name").getTextContent();
                        logger.info("Name:  " + FieldName1);

                        String Operator = whereNodeList.item(x).getAttributes().getNamedItem("operator")
                                .getTextContent();
                        logger.info("Operator:  " + Operator);
                        NodeList fieldNodeList = element1.getElementsByTagName("field");
                        String fieldValue = fieldNodeList.item(x).getTextContent();
                        logger.info("fieldValue:  " + fieldValue);
                        logger.info("Name:  " + FieldName1 + " ,Operator  " + Operator + " ,fieldValue  " + fieldValue
                                + "");
                        String FieldNameArr = getOriginalDbValuesWithRespectToApi(FieldName1, apiName);
                        whereSet.add(FieldName1);
                        whereQuery += javaDefaultConvertors.getQueryStringFromOpertor(FieldNameArr, Operator,
                                fieldValue);
                        if (x != whereNodeList.getLength() - 1) {
                            whereQuery += " and ";
                        }
                    }
                }
            }
            if (apiName.equals("getNewApprovedAccountSetupInfo")) {
                whereQuery += " and donor_approval_status = 'Approved' ";
            }
            logger.info("Query  For Where Segment  " + whereQuery);
            query += whereQuery;
            String pagingElement = "paging";
            String pageSize = null;
            String pageNumber = null;
            NodeList pagelist = doc.getElementsByTagName(pagingElement);
            if (pagelist.getLength() == 0) {
                logger.info("Paging Section Does Not Exists");
                pageSize = BootApplication.MaxPageSize;
                pageNumber = "1";
                logger.info("PageSize: " + pageSize + "  & Page Number:" + pageNumber);
                int offset = ((Integer.parseInt(pageNumber) - 1) * Integer.parseInt(pageSize));
                logger.info((Integer.parseInt(pageNumber) - 1) + " " + Integer.parseInt(pageSize) + " : * : " + offset);
                query += "     LIMIT " + pageSize + "      OFFSET " + offset + "         ";
            }
            for (int t = 0; t < pagelist.getLength(); t++) {
                Node node = pagelist.item(t);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String orderBy = " ";
                    if (element.getAttribute("page_size").isBlank()) {
                        pageSize = BootApplication.MaxPageSize;
                        logger.info("Page Size not found");
                    } else {
                        pageSize = element.getAttribute("page_size");
                    }
                    if (element.getAttribute("page_number").isBlank()) {
                        logger.info("Page Number not found");
                        pageNumber = "1";
                    } else {
                        pageNumber = element.getAttribute("page_number");
                    }

                    logger.info("PageSize: " + pageSize + "  & Page Number:" + pageNumber);
                    int offset = ((Integer.parseInt(pageNumber) - 1) * Integer.parseInt(pageSize));
                    logger.info(
                            (Integer.parseInt(pageNumber) - 1) + " " + Integer.parseInt(pageSize) + " : * : " + offset);

                    if (!element.getAttribute("order_by").isBlank()) {
                        for (String orderValue : element.getAttribute("order_by").split(",")) {
                            orderValue = orderValue.trim();
                            String orderParameter = getOriginalDbValuesWithRespectToApi(orderValue.split(" ")[0],
                                    apiName).split(",")[0];
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
                }
            }
            logger.info("Final Query   [   " + query + "  ] ");
            return query;
        } catch (Exception e) {
            logger.info("Xml Decoder Exceptin for " + scopesController.getRefCode());
            logger.error("Decoder Exception : " + e.getMessage());
            return xmlDomHandler.genericErrorMethod("Client Error", "Syntax Error");
            // return xmlDomHandler.genericErrorMethod("Client Error", "Syntax Error" ,
            // scopesController.getRefCode() ,scopesController.getContentType() );
        }
    }

    public   Document loadXMLFromString(String xml) throws Exception {
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
