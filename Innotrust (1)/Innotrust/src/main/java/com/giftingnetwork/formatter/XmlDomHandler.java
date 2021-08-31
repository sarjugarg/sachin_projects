package com.giftingnetwork.formatter;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.giftingnetwork.controller.ScopesController;
import com.giftingnetwork.model.GenericModel;
import com.giftingnetwork.util.GenericFunctions;

import org.w3c.dom.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class XmlDomHandler {

    @Autowired
    ScopesController scopesController;

    @Autowired
    GenericFunctions genericFunctions;

    Logger logger = LoggerFactory.getLogger(XmlDomHandler.class);

    public GenericModel generateXmlContentForLogin(GenericModel genericModel, String session_token_timeout) {
        try {
            JSONObject record = new JSONObject();
            JSONObject item = new JSONObject();
            item.put("access_token", genericModel.getAuthorization());
            item.put("expires_in", session_token_timeout);
            item.put("token_type", "bearer");
            record.put("result", item);
            record.put("status", 1);

            genericModel.setHttpStatus(HttpStatus.OK);
            if (genericModel.getAcceptType().contains("xml")) {
                genericModel.setResult(genericFunctions.jsonToXmlConverter(record.toString()));
            } else {
                genericModel.setResult(record.toString());
            }
            return genericModel;
        } catch (Exception e) {
            logger.error(e.getMessage());
            genericModel.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            genericModel.setErrorTitle("Internal server error");
            genericModel = generateErrorXml1(genericModel);
            return genericModel;
        }
    }

    public GenericModel generateErrorXml1(GenericModel genericModel) {
        try {
            logger.info("Going to get Error Response " + genericModel.toString());
            JSONObject data = new JSONObject();
            JSONObject item = new JSONObject();
            try {
                Field changeMap = item.getClass().getDeclaredField("map");
                changeMap.setAccessible(true);
                changeMap.set(item, new LinkedHashMap<>());
                changeMap.setAccessible(false);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                logger.error(e.getMessage());
            }
            item.put("code", genericModel.getHttpStatus().value());
            item.put("title", genericModel.getErrorTitle());
            item.put("detail", genericModel.getErrorDetail());
            item.put("ref_code", genericModel.getRef_code());
            
            data.put("status", 0);
            data.put("error", item);
            if (genericModel.getAcceptType().contains("xml")) {
                genericModel.setResult(genericFunctions.jsonToXmlConverter(data.toString()));
            } else {
                genericModel.setResult(data.toString());
            }

            return genericModel;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return genericModel;
        }
    }

    public String generateLogOffXml(GenericModel genericModel) {
        try {
            if (genericModel.getAcceptType().contains("json")) {
                String messages;
                JSONObject data = new JSONObject();
                JSONObject status = new JSONObject();
                JSONObject item = new JSONObject();
                item.put("code", "logout");
                item.put("message", "done");
                item.put("type", "info");
                status.put("status", item);
                data.put("status", 1);
                messages = data.toString();
                return messages;
            } else {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.newDocument();
                Element status = doc.createElement("status");
                status.appendChild(doc.createTextNode("1"));
                doc.appendChild(status);
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
            return "false";
        }
    }

}
 