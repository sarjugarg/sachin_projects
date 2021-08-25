package com.giftingnetwork.api;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Service
public class GenericFunctions {

    @Autowired
    XmlDomHandler xmlDomHandler;

    @Autowired
    ScopesController scopesController;


    @Autowired
    LoginRepository  loginRepository;

    Logger logger = LoggerFactory.getLogger(GenericFunctions.class);

    public String getQueryStringFromOpertor(String fieldName, String operator, String fieldValue) {
        String result = null;
        switch (operator.trim()) {
            case "null":
                result = fieldName + " " + operator + " '" + fieldValue + "'  ";
                break;
            case "notnull":
                result = fieldName + " " + operator + " '" + fieldValue + "'  ";
                break;
            case "like":
                result = fieldName + " " + operator + " '%" + fieldValue + "%'  ";
                break;
            case "notlike":
                result = fieldName + " not  like  '%" + fieldValue + "%'  ";
                break;
            case "contain":
                result = fieldName + " " + operator + " '" + fieldValue + "'  ";
                break;
            case "notcontain":
                result = fieldName + " " + operator + " '" + fieldValue + "'  ";
                break;
            case "in":
                String fieldDetails = "";
                for (String value : fieldValue.split(",")) {
                    fieldDetails += "  '" + value + "' ,";
                }
                fieldDetails = removeLastChars(fieldDetails, 1);
                result = fieldName + " " + operator + " (  " + fieldDetails + "  )  ";
                break;
            case "notin":
                String fieldDetail = "";
                for (String value : fieldValue.split(",")) {
                    fieldDetail += "  '" + value + "' ,";
                }
                fieldDetail = removeLastChars(fieldDetail, 1);
                result = fieldName + "  not in   (" + fieldDetail + " )  ";
                break;
            default:
                result = fieldName + " " + operator + " '" + fieldValue + "'  ";
        }
        logger.info("Response after  Default  Conversion " + result);
        return result;
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }

    public String genericErrorMethodOld(String code, String message ) {
        try {
            logger.info(" Generate Error For   " + code + " With Message " + message);
             String contentType = scopesController.getAcceptType();
            logger.info ( "   ContentType to be Return in  " + contentType);
            String refCode = scopesController.getRefCode();
            logger.info(" Generated Error  for " + refCode  ) ; 

            if (contentType.contains("json")) {
                String mess;
                JSONObject data = new JSONObject();
                JSONObject status = new JSONObject();
                JSONObject item = new JSONObject();
                item.put("code", code);
                item.put("message", message);
                item.put("refcode", refCode);
                item.put("type", "Error");
                status.put("status", item);
                data.put("data", status);
                mess = data.toString();
                logger.info(" Generated Error  for json type " + mess);
                return mess;
            } else {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("data");
                doc.appendChild(rootElement);

                Element recordElement = doc.createElement("status");

                Attr typeAttr = doc.createAttribute("type");
                typeAttr.setValue("Error");

                Attr attrTyp = doc.createAttribute("code");
                attrTyp.setValue(code);

                Attr messAttr = doc.createAttribute("message");
                messAttr.setValue(message);

                Attr refCodeAttr = doc.createAttribute("refCode");
                refCodeAttr.setValue(refCode);

                recordElement.setAttributeNode(typeAttr);
                recordElement.setAttributeNode(refCodeAttr);
                recordElement.setAttributeNode(messAttr);
                recordElement.setAttributeNode(attrTyp);

                rootElement.appendChild(recordElement);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StringWriter writer = new StringWriter();
                StreamResult consoleResult = new StreamResult(writer);
                transformer.transform(source, consoleResult);
                String strResult = writer.toString();
                logger.info(" Generic Error Result In Xml " + strResult);
                return strResult;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "System Generated Error ";
        }
    }

    public HttpStatus getHttpStatusValues(String result) {
        if (result.toLowerCase().contains("client")) {
            return HttpStatus.BAD_REQUEST;
        } else if (result.toLowerCase().contains("server")) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.OK;
        }
    }

    public void setConfigurationVariables() {

        loginRepository.setSessionTimeOutConfiguration();  
        loginRepository.setMaximumPageSizeConfiguration();
    }
}
