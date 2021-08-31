package com.giftingnetwork.util;

import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.giftingnetwork.formatter.XmlDomHandler;
import com.giftingnetwork.repo.LoginRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenericFunctions {

    @Autowired
    XmlDomHandler xmlDomHandler;

    
    @Autowired
    LoginRepository loginRepository;

    Logger logger = LoggerFactory.getLogger(GenericFunctions.class);

    public String getQueryStringFromOpertor(String fieldName, String operator, String fieldValues) {
        String result = "";
        if (fieldValues.contains(";")) {
            int i = 0;
            for (String fieldValue : fieldValues.split(";")) {
                result += getQueryStringFromOpertorCont(fieldName,
                        operator.split(";").length-1 < i ? " = " : operator.split(";")[i]   , fieldValue) + " and ";
                i++;
            }
        } else {
            result = getQueryStringFromOpertorCont(fieldName, operator, fieldValues) + "     ";
        }
        return removeLastChars(result, 4);
    }

    public String getQueryStringFromOpertorCont(String fieldName, String operator, String fieldValue) {
        logger.info("Qury Operator " + fieldName + ":: " + operator + "  :: " + fieldValue);
        String result = null;
        switch (operator.trim()) {
            case "gt":
                result = fieldName + "  > '" + fieldValue + "'  ";
                break;

            case "lt":
                result = fieldName + "  < '" + fieldValue + "'  ";
                break;

            case "gte":
                result = fieldName + "  >= '" + fieldValue + "'  ";
                break;

            case "lte":
                result = fieldName + "  <= '" + fieldValue + "'  ";
                break;

            case "eq":
                result = fieldName + "  = '" + fieldValue + "'  ";
                break;

            case "neq":
                result = fieldName + "  != '" + fieldValue + "'  ";
                break;

            case "null":
                result = fieldName + " " + operator + " '" + fieldValue + "'  ";
                break;
            case "notnull":
                result = fieldName + " " + operator + " '" + fieldValue + "'  ";
                break;
            case "like":
                result = fieldName + "  ilike '%" + fieldValue + "%'  ";
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

    public   String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    } 
  
    public void setConfigurationVariables() {
        loginRepository.setSessionTimeOutConfiguration();
         loginRepository.setDefaultPageSizeConfiguration();
        loginRepository.setDefaultMinPageSizeConfiguration();
        loginRepository.setDefaultMaxPageSizeConfiguration();
    }
    

    public String jsonToXmlConverter(String result) {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            JsonNode node = jsonMapper.readValue(result, JsonNode.class);
            XmlMapper xmlMapper = new XmlMapper();
            xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
            xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_1_1, true);
            StringWriter w = new StringWriter();
            xmlMapper.writeValue(w, node);
            result = w.toString().replace("ObjectNode", "data");
        } catch (Exception e) {
            logger.error(e.getMessage() + "," + e.getLocalizedMessage());
        }
        return result;
    }



    public String ReferenceCodeGenerator(String api_name, String username) {
        try {
          
          logger.info("generate Reference Code with api_name " + api_name + " username : " + username);
          String rand = String.valueOf(new Random().nextInt(1000) + 1);
          String encrpRefCode = generateHashValue(username, api_name, rand);
          MDC.put("first", "RefCode: " + encrpRefCode);
          return encrpRefCode;
        } catch (Exception e) {
          logger.error(e.getMessage());
          return null;
        }
      }
      public String generateHashValue(String username, String password, String clientID) {
        Date date = new Date();
        long timeMilli = date.getTime();
        String sessionId = encryptThisString(String.valueOf(timeMilli) + username);
        logger.info("  Generated  : " + sessionId + " for username " + username);
        return sessionId;
      }
    
      public String encryptThisString(String input) {
        try {
          MessageDigest md = MessageDigest.getInstance("MD5");
          byte[] messageDigest = md.digest(input.getBytes());
          BigInteger no = new BigInteger(1, messageDigest);
          String hashtext = no.toString(16);
          while (hashtext.length() < 32) {
            hashtext = "0" + hashtext;
          }
          return hashtext;
        } catch (NoSuchAlgorithmException e) {
          logger.error(e.getMessage());
          return "false";
        }
      }
}
