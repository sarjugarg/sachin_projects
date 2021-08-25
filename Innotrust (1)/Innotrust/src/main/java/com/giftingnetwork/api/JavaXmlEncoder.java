package com.giftingnetwork.api;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class JavaXmlEncoder {
    @Autowired
    LoginRepository loginRepository;

    @Autowired
    XmlDomHandler xmlDomHandler;

    Logger logger = LoggerFactory.getLogger(JavaXmlEncoder.class);

    public String toDocument(ResultSet rs, LinkedHashMap<String, String> map, String query) {
        try {
            String Offset = query.substring(query.indexOf("OFFSET") + 6, query.indexOf("OFFSET") + 11);
            String limit = query.substring(query.indexOf("LIMIT") + 5, query.indexOf("LIMIT") + 9);
            logger.info("Offset " + Offset);
            logger.info("limit " + limit);
            String totalCount = loginRepository.getTotalCountFromDb(query);
            int pageSize = ((Integer.parseInt(Offset.trim()) / Integer.parseInt(limit.trim())) + 1);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Map<String, String> childMap = new HashMap<String, String>();

            // Element main = doc.createElement("main");
            // Element rootElement = doc.createElement("data");
            // main.appendChild(rootElement);

            // logger.info(" ");
            // rootElement.appendChild(status); // could be move at line 105
            // logger.info(" ");

            Element rootElement = doc.createElement("data");
            doc.appendChild(rootElement);

            Element status = doc.createElement("status");
            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode("success"));
            status.appendChild(type);
            rootElement.appendChild(status);

            Element results = doc.createElement("result");
            rootElement.appendChild(results);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
            logger.info(" Column count for Query " + colCount);
            Element resultElement = doc.createElement("paging");
            Attr PageAttr = doc.createAttribute("page_number");
            PageAttr.setValue(String.valueOf(pageSize));
            Attr PageSizeAttr = doc.createAttribute("page_size");
            PageSizeAttr.setValue(limit.trim());
            Attr TotalRowAttr = doc.createAttribute("total_rows");
            TotalRowAttr.setValue(totalCount);
            resultElement.setAttributeNode(PageSizeAttr);
            resultElement.setAttributeNode(PageAttr);
            resultElement.setAttributeNode(TotalRowAttr);
            results.appendChild(resultElement);
            Element row = doc.createElement("recordset");
            results.appendChild(row);
            while (rs.next()) {
                Element recordElement = doc.createElement("record");
                for (int i = 1; i <= colCount; i++) {
                    childMap.put(rsmd.getColumnName(i), rs.getObject(i) == null ? " " : rs.getObject(i).toString());
                }
                for (Map.Entry<String, String> entry : childMap.entrySet())
                    logger.info(" childMap :::::::: Key = " + entry.getKey() + "  Value = " + entry.getValue());
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    logger.info("Key = " + entry.getKey() + "  Value = " + entry.getValue());
                    String[] arr = entry.getValue().split(",");
                    String paramValues = "";
                    for (int i = 0; i < arr.length; i++) {
                        logger.info(" Inside array:: " + arr[i].substring(arr[i].lastIndexOf(".") + 1) + " value  : "
                                + childMap.get(arr[i].substring(arr[i].lastIndexOf(".") + 1)));
                        paramValues = paramValues + childMap.get(arr[i].substring(arr[i].lastIndexOf(".") + 1)) + "";
                    }
                    logger.info("Attributes for " + entry.getKey() + " are :" + paramValues);
                    Attr attrTyp = doc.createAttribute(entry.getKey());
                    attrTyp.setValue(paramValues);
                    recordElement.setAttributeNode(attrTyp);
                }
                row.appendChild(recordElement);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult consoleResult = new StreamResult(writer);
            transformer.transform(source, consoleResult);
            String strResult = writer.toString();
            logger.info(" Result in Xml Form  " + strResult);
            return strResult;

        } catch (Exception e) {
            logger.error(e.getMessage());
            return xmlDomHandler.genericErrorMethod("Server Error", "Syntax Error");
        }
    }
}
