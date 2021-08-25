package com.example;

import com.google.gson.Gson;
import java.io.InputStream;
import java.security.KeyStore;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.SSLContext;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

//@SpringBootApplication
public class BasicApplication {

     private static final String KEYSTOREPATH = "/home/ubuntu/CEIR/GSMA_CLIENT/gsma_ks.jks"; // or .p12
     private static final String KEYSTOREPASS = "Hello1234";
     private static final String KEYPASS = "keypass";

     LogWriter logWriter = new LogWriter();

     static final Logger logger = Logger.getLogger(BasicApplication.class);

     public KeyStore readStore() throws Exception {
          try (InputStream keyStoreStream = this.getClass().getResourceAsStream(KEYSTOREPATH)) {
               KeyStore keyStore = KeyStore.getInstance("JKS"); // or "PKCS12"
               keyStore.load(keyStoreStream, KEYSTOREPASS.toCharArray());
               return keyStore;
          }
     }

 
     public static void main(String[] args) {       // 

          BasicApplication bs = new BasicApplication();
          try {
               bs.gsmaApplication();
          } catch (Exception e) {
               logger.info("Exception :  " + e);

          }

     }

     public void gsmaApplication() {
          GsmaDbDao snt = new GsmaDbDao();
          final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";
          final String DB_URL = "jdbc:oracle:thin:@dmc-prod-db:1521/dmcproddb";
          final String USER = "CRESTELCEIR";
          final String PASS = "CRESTELCEIR";
          Connection conn = null;
          try {

               Class.forName(JDBC_DRIVER);
               conn = DriverManager.getConnection(DB_URL, USER, PASS);
               conn.setAutoCommit(false);
          } catch (Exception e) {
               logger.info(" Error" + e);
          }
          logger.info("Connnection created successfully" + conn);

          Map<String, String> map = snt.getExistingGsmaDetails(conn);

          String APIKey = null, Password = null, Salt_String = null, Organization_Id = null, Secretkey = null, httpPostUrl = null, gsma_tac_timewait = null;
          APIKey = map.get("gsma_tac_APIKey");
          Password = map.get("gsma_tac_Password");
          Salt_String = map.get("gsma_tac_Salt_String");
          Organization_Id = map.get("gsma_tac_Organization_Id");
          Secretkey = map.get("gsma_tac_Secretkey");
          httpPostUrl = map.get("gsma_tac_httpPostUrl");
          gsma_tac_timewait = map.get("gsma_tac_timewait");
          logger.info("httpPostUrl." + httpPostUrl);
          BasicApplication obj = new BasicApplication();
          try {
               LogWriter ls = new LogWriter();
               Statement stmt = conn.createStatement();

  ResultSet rs1 = stmt.executeQuery("    select distinct tac   from device_usage_db where (tac not in(select device_id from gsma_tac_db) and tac not in(select tac from gsma_invalid_tac_db)) ");
               Set<String> hash_Set = new HashSet<String>();
               while (rs1.next()) {
                    hash_Set.add(rs1.getString(1));
               }
 
               rs1.close();
               logger.info("Remaining size  ." + hash_Set.size());
               for (String imei_tac : hash_Set) {

                    logger.info("imei_tac  ." + imei_tac);
                    SSLContext sslContext = SSLContexts.custom()
                            .loadKeyMaterial(obj.readStore(), KEYPASS.toCharArray()) // use null as second param if you don't have a separate key password
                            .build();
                    String testrequest = "{\"portName\":\"Phnom penh\",\"country\": \"Cambodia\", \"portType\": \"AIR\",\"deviceId\":\"" + imei_tac + "\"}";
                    StringEntity input = new StringEntity(testrequest);
                    input.setContentType("application/json");

                    HttpClient httpClient = HttpClients.custom().setSslcontext(sslContext).build();
                    HttpPost request = new HttpPost(httpPostUrl);
                    logger.info("request ." + request);
                    String auth = EncriptonService.getAuth(imei_tac, APIKey, Password, Salt_String, Organization_Id, Secretkey);      // for original.. ta 
                    request.addHeader("Authorisation", auth);
                    logger.info("auth ." + auth);
                    request.addHeader("Content-Type", "application/json");     // original
                    request.setEntity(input);

                    String message = "";
                    HttpResponse response = httpClient.execute(request);
                    HttpEntity entity = response.getEntity();
                    message = EntityUtils.toString(response.getEntity());
                    EntityUtils.consume(entity);

                    Gson gson = new Gson();
                    GsmaEntity product = gson.fromJson(message, GsmaEntity.class);
                    logger.info("Product Report : " + message);
                    ls.writeLog("Tac  : " + imei_tac + " :: ");
                    ls.writeLog("Output : " + message);

                    try {
                         if (product.getGsmaApprovedTac().equals("Yes")) {
                              snt.databaseMapper(message, conn);
                         } else {
                              snt.invalidGsmaDb(product.getDeviceId(), conn);
                         }
                    } catch (Exception e) {
                         logger.info("getGsmaApprovedTac ERror : " + e);
                    }
                    conn.commit();
               }

               logger.info(" finishesed");
               System.exit(0);
          } catch (Exception e) {
               logger.info(" Error." + e);

          }

     }

    
}
