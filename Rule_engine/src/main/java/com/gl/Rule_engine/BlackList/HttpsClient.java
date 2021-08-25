package com.gl.Rule_engine.BlackList;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import org.apache.log4j.Logger;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class HttpsClient {
    
static final Logger logger = Logger.getLogger(HttpsClient.class);
	static {
		System.setProperty("javax.net.ssl.trustStore", "keypath");
		System.setProperty("javax.net.ssl.trustStorePassword", "test");
		System.setProperty("javax.net.ssl.keyStore", "keypath");
		System.setProperty("javax.net.ssl.keyStorePassword", "test");
	}

	public static void call() throws  IOException {
		String url="";
		String json = "";
		URL ur = new URL(url);
		boolean isValid = true;
		
		HostnameVerifier allHostsValid = (hostname, session) -> isValid;

		HttpsURLConnection con = (HttpsURLConnection) ur.openConnection();
		con.setHostnameVerifier(allHostsValid);
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestMethod("POST");
		con.addRequestProperty("Content-Type", "application/json");
		try (OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream())) {
			out.write(json);
			out.flush();
		}
		logger.debug("Response Code=" + con.getResponseCode());
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
			reader.lines().forEach(System.out::println);
		}
		con.disconnect();
	}
}
