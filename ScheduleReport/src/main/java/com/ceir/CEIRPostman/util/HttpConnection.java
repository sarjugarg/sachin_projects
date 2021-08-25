/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ceir.CEIRPostman.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author maverick
 */

@Service
public class HttpConnection {

    private final Logger logger = Logger.getLogger(getClass());

    public String HttpApiConnecter(String tag, String responseBody) {
        String result = null;
        try {
            URL url = new URL(tag);
            HttpURLConnection hurl = (HttpURLConnection) url.openConnection();
            hurl.setRequestMethod("POST");
            hurl.setDoOutput(true);
            hurl.setRequestProperty("Content-Type", "application/json");
            hurl.setRequestProperty("Accept", "application/json");
            OutputStreamWriter osw = new OutputStreamWriter(hurl.getOutputStream());
            osw.write(responseBody);
            osw.flush();
            osw.close();
            logger.info("DatA Posted");
            hurl.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(hurl.getInputStream()));
            String temp = null;
            StringBuilder sb = new StringBuilder();
            while ((temp = in.readLine()) != null) {
                sb.append(temp).append(" ");
            }
            result = sb.toString();
            in.close();
            logger.info("OUTPUT result is .." + result);
        } catch (Exception e) {
            logger.error(responseBody + "  " + e);
            result = "fail";
        }
        return result;
    }

    public HttpURLConnection getHttpConnection(String url, String type) {
        URL uri = null;
        HttpURLConnection con = null;
        try {
            uri = new URL(url);
            con = (HttpURLConnection) uri.openConnection();
            con.setRequestMethod(type); // type: POST, PUT, DELETE, GET
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setConnectTimeout(60000); // 60 secs...
            con.setReadTimeout(60000); // 60 secs
            con.setRequestProperty("Accept-Encoding", "application/json");
            con.setRequestProperty("Content-Type", "application/json");
        } catch (Exception l) {
            logger.error(l);
        }
        return con;
    }

}
