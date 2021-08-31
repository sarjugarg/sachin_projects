package com.giftingnetwork.config;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.giftingnetwork.util.GenericFunctions;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
 

@EnableWebMvc
@ControllerAdvice
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @Autowired
    HttpServletRequest req;

    @Autowired
    GenericFunctions genericFunctions;

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFound(NoHandlerFoundException e, WebRequest request) {
    String title =  req.getServletPath().contains("v1")  ?  "invalid url end point "  : " version not correct" ; 
        if (     req.getHeader("Accept").contains("xml")   ) {
            return genericFunctions.jsonToXmlConverter(customResponseWithCode("404", "not found", title));
        }
        return customResponseWithCode("404", "not found", title);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String handleMethodNotAllowed(HttpRequestMethodNotSupportedException e, WebRequest request) {
        if (     req.getHeader("Accept").contains("xml")   ) {
            return genericFunctions.jsonToXmlConverter(customResponseWithCode("405", "method not allowed", " method provided is not valid"));
        }
        return customResponseWithCode("405", "method not allowed", " method provided is not valid");
    }

     
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String handleHttpMediaTypeNotSupported(Exception e, WebRequest request) {
        if (     req.getHeader("Accept").contains("xml")   ) {
            return genericFunctions.jsonToXmlConverter(customResponseWithCode("406", "not acceptable", "request format is not acceptable "));
        }
        return customResponseWithCode("406", "not acceptable", "request format is not acceptable ");
    }

    @ExceptionHandler(Exception.class) 
     @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequestException(Exception e, WebRequest request) {
        //String path = req.getServletPath();
        if (     req.getHeader("Accept").contains("xml")   ) {
      //      return genericFunctions.jsonToXmlConverter(customResponseWithCode("400", "bad request",  req.getServletPath().contains("v1") ? "id is not valid " : "version  is not valid"  ));
             return genericFunctions.jsonToXmlConverter(customResponseWithCode("400", "bad request",   "request is not valid "    ));
        }
        return customResponseWithCode("400", "bad request",  " request is not valid " );
   //   return customResponseWithCode("400", "bad request", req.getServletPath().contains("v1") ? "id is not valid" : "version is not valid " );
    }
 


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalRequestException(Exception e, WebRequest request) {
         if (     req.getHeader("Accept").contains("xml")   ) {
              return genericFunctions.jsonToXmlConverter(customResponseWithCode("400", "bad request",   "request is not valid "    ));
        }
        return customResponseWithCode("400", "bad request",  " request is not valid " );
     }

    String customResponseWithCode(String code, String title, String message) {
        JSONObject data = new JSONObject();
        JSONObject item = new JSONObject();
        String refCode = ReferenceCodeGenerator(code, req.getRemoteAddr() );
        try {
            Field changeMap = item.getClass().getDeclaredField("map");
            changeMap.setAccessible(true);
            changeMap.set(item, new LinkedHashMap<>());
            changeMap.setAccessible(false);
        } catch (IllegalAccessException | NoSuchFieldException e) {
           }
        item.put("code", code);
        item.put("title", title);
        item.put("detail", message);
        item.put("ref_code", refCode);
       


        data.put("status", 0);
        data.put("error", item);
        return data.toString();

    }

    public String ReferenceCodeGenerator(String api_name, String username) {
        try {
            String rand = String.valueOf(new Random().nextInt(1000) + 1);
            Date date = new Date();
            long timeMilli = date.getTime();
            String sessionId = encryptThisString(String.valueOf(timeMilli) + username + rand);
            return sessionId;
        } catch (Exception e) {
            return null;
        }
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
        } catch (Exception e) {
            return "false";
        }
    }

}
