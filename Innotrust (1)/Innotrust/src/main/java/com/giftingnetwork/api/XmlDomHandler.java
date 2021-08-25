package com.giftingnetwork.api;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class XmlDomHandler {

    @Autowired
    ScopesController scopesController;

    @Autowired
    GenericModel genericModel;

    Logger logger = LoggerFactory.getLogger(XmlDomHandler.class);

    public GenericModel generateXmlContentForLogin(String sessionId, String respose, String ContentType) {

        Calendar now = Calendar.getInstance();
       int value =  Integer.parseInt(BootApplication.LoginSessionTimeOut);
        now.add(Calendar.MINUTE, value);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            if (ContentType.contains("json")) {
                String message;
                JSONObject data = new JSONObject();
                 JSONObject record = new JSONObject();
                JSONObject item = new JSONObject();

                JSONObject status = new JSONObject();
                status.put("type", respose);
                
                item.put("access_token", sessionId);
                item.put("expires_after", df.format(now.getTime()));
                record.put("result", item);

                // recordSet.put("recordSet", record);
                // recordSet.put("page_number", "1");
                // result.put("result", recordSet);

                data.put("status", status);
                data.put("data", record);

                message = data.toString();
                // return message;
                genericModel.setResult(message);
                genericModel.setHttpStatus(HttpStatus.OK);
                return genericModel;
            } else {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("data");
                doc.appendChild(rootElement);
              
                Element status = doc.createElement("status");
                Element type = doc.createElement("type");
                type.appendChild(doc.createTextNode("success"));
                status.appendChild(type);
                rootElement.appendChild(status);  
                
                Element resultElement = doc.createElement("result");

                Element access_token = doc.createElement("access_token");
                access_token.appendChild(doc.createTextNode(sessionId));
                resultElement.appendChild(access_token);

                Element expires_after = doc.createElement("expires_after");
                expires_after.appendChild(doc.createTextNode(df.format(now.getTime())));
                resultElement.appendChild(expires_after);
                logger.info("  ");
            
                logger.info(" ");
                rootElement.appendChild(resultElement);
 

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StringWriter writer = new StringWriter();
                StreamResult consoleResult = new StreamResult(writer);
                transformer.transform(source, consoleResult);
                String Result = writer.toString(); // return Result;
                genericModel.setHttpStatus(HttpStatus.OK);
                genericModel.setResult(Result);
                return genericModel;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            genericModel.setResult(genericErrorMethod("Server Error", "Internal Error"));
            return genericModel;
            // return genericErrorMethod("Server Error", "Internal Error");
        }
    }

    public GenericModel generateErrorXml(String code, String message, String refCode) {
        try {
            refCode = scopesController.getRefCode(); // BootApplication.encrRefCode;
            logger.info(" Generic err for  " + refCode + " in format " + scopesController.getAcceptType());

            if (code.toLowerCase().contains("client")) {
                genericModel.setHttpStatus(HttpStatus.BAD_REQUEST);
            }
            if (code.toLowerCase().contains("server")) {
                genericModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (scopesController.getAcceptType().contains("json")) {
                String messages;
                JSONObject data = new JSONObject();
                JSONObject status = new JSONObject();
                JSONObject ite = new JSONObject();
                ite.put("code", code);
                ite.put("message", message);
                ite.put("ref_code", refCode);
                ite.put("type", "error");
                status.put("status", ite);
                data.put("data", status);
                messages = data.toString();
                genericModel.setResult(messages);

                return genericModel;
            } else {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("data");
                doc.appendChild(rootElement);
                Element recordElement = doc.createElement("status");
                Attr typeAttr = doc.createAttribute("type");
                typeAttr.setValue("error");
                Attr attrTyp = doc.createAttribute("code");
                attrTyp.setValue(code);
                Attr messAttr = doc.createAttribute("message");
                messAttr.setValue(message);
                Attr refCodeAttr = doc.createAttribute("ref_code");
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
                logger.info(" Generic Result In Xml " + strResult);
                genericModel.setResult(strResult);
                return genericModel;
                // return strResult;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            genericModel.setResult(genericErrorMethod("Server Error", "Internal Error"));
            return genericModel;
        }

    }

    public String generateLogOffXml() {
        try {

            if (scopesController.getAcceptType().contains("json")) {
                String messages;
                JSONObject data = new JSONObject();
                JSONObject status = new JSONObject();
                JSONObject item = new JSONObject();
                item.put("code", "logout");
                item.put("message", "done");
                item.put("type", "info");
                status.put("status", item);
                data.put("data", status);
                messages = data.toString();
                return messages;
            } else {

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.newDocument();
                Element rootElement = doc.createElement("data");
                doc.appendChild(rootElement);

                Element recordElement = doc.createElement("status");
                Attr type = doc.createAttribute("type");
                type.setValue("info");
                Attr attrTyp = doc.createAttribute("code");
                attrTyp.setValue("logout");
                Attr attrmsg = doc.createAttribute("message");
                attrmsg.setValue("done");
                recordElement.setAttributeNode(attrmsg);
                recordElement.setAttributeNode(attrTyp);
                recordElement.setAttributeNode(type);
                rootElement.appendChild(recordElement);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StringWriter writer = new StringWriter();
                StreamResult consoleResult = new StreamResult(writer);
                transformer.transform(source, consoleResult);
                String strResult = writer.toString();
                return strResult;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return genericErrorMethod("Server Error", "Internal Error");
        }
    }

    public String genericErrorMethod(String code, String message) {
        try {
            code = code.toLowerCase();
            message = message.toLowerCase();
            logger.info("Generating Error String");
            String contentType = scopesController.getAcceptType();
            logger.info("ContentType to be Return in  " + contentType);
            String refCode = scopesController.getRefCode();
            logger.info(" Generated Error  for " + refCode);
            logger.info(" Generate Error For   " + code + " With Message " + message);
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
            return "System   Error ";
        }

    }

}
