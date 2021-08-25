package gsmaTac;

import com.gl.utils.LogWriter;
import com.google.gson.Gson;
import java.io.InputStream;
import java.security.KeyStore;
import java.sql.Connection;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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

     static final Logger logger = Logger.getLogger(BasicApplication.class);
     private static final String KEYSTOREPATH = "/home/ubuntu/CEIR/GSMA_CLIENT/gsma_ks.jks"; // or .p12
     private static final String KEYSTOREPASS = "Hello1234";
     private static final String KEYPASS = "keypass";
     LogWriter logWriter = new LogWriter();

     public KeyStore readStore() throws Exception {
          try (InputStream keyStoreStream = this.getClass().getResourceAsStream(KEYSTOREPATH)) {
               KeyStore keyStore = KeyStore.getInstance("JKS"); // or "PKCS12"
               keyStore.load(keyStoreStream, KEYSTOREPASS.toCharArray());
               return keyStore;
          }
     }

     public String gsmaApplication(String imei_tac, Connection conn) throws InterruptedException, Exception {

          GsmaDbDao snt = new GsmaDbDao();
          String status = "Yes";
          if (imei_tac.length() != 8) {
//            status = " ";
               logger.error("DeviceId should be 1st 8 digits of an IMEI");
               status = "No";
               logger.debug(status);
          } else {
               logger.debug("Gsma Application Started");
               status = snt.getExistingGsmaDetails(imei_tac, conn);
               if (status.equalsIgnoreCase("NOTPRESENT")) {
                    Map<String, String> map = snt.getGsmaApiDetails(imei_tac, conn);
                    String APIKey = null, Password = null, Salt_String = null, Organization_Id = null, Secretkey = null, httpPostUrl = null, gsma_tac_timewait = null;
                    APIKey = map.get("gsma_tac_APIKey");
                    Password = map.get("gsma_tac_Password");
                    Salt_String = map.get("gsma_tac_Salt_String");
                    Organization_Id = map.get("gsma_tac_Organization_Id");
                    Secretkey = map.get("gsma_tac_Secretkey");
                    httpPostUrl = map.get("gsma_tac_httpPostUrl");
                    gsma_tac_timewait = map.get("gsma_tac_timewait");
                    logger.debug("httpPostUrl  " + httpPostUrl);
                    int timewait = Integer.parseInt(gsma_tac_timewait);
                    logWriter.writeLogGsma("Start imei_tac is " + imei_tac);
                    BasicApplication obj = new BasicApplication();
                    SSLContext sslContext = SSLContexts.custom()
                            .loadKeyMaterial(obj.readStore(), KEYPASS.toCharArray())
                            .build();
                    String testrequest = "{\"portName\":\"Phnom penh\",\"country\": \"Cambodia\", \"portType\": \"AIR\",\"deviceId\":\"" + imei_tac + "\"}";
                    StringEntity input = new StringEntity(testrequest);
                    input.setContentType("application/json");
                    HttpClient httpClient = HttpClients.custom().setSslcontext(sslContext).build();
                    HttpPost request = new HttpPost(httpPostUrl);
                    String auth = EncriptonService.getAuth(imei_tac, APIKey, Password, Salt_String, Organization_Id, Secretkey);
                    request.addHeader("Authorisation", auth);
                    request.addHeader("Content-Type", "application/json");
                    request.setEntity(input);
                    TimerTask task = new TimerTask() {
                         @Override
                         public void run() {
                              if (request != null) {
                                   request.abort();
                                   logWriter.writeLogGsma("Request TimeOut");
                              } else {
                              }
                         }
                    };
                    new Timer(true).schedule(task, timewait * 1000);
                    try {
                         String message = "";
                         HttpResponse response = httpClient.execute(request);
                         HttpEntity entity = response.getEntity();
                         message = EntityUtils.toString(response.getEntity());
                         EntityUtils.consume(entity);
                         logWriter.writeLogGsma("End Result for  " + imei_tac + " :: " + message);
                         Gson gson = new Gson();
                         GsmaEntity product = gson.fromJson(message, GsmaEntity.class);
                         if (product.getGsmaApprovedTac().equals("Yes")) {
                              status = snt.databaseMapper(message, conn);
                              status = "Yes";
                         } else {
                              logger.debug("GSMAINVALIDDB");
                              snt.invalidGsmaDb(imei_tac.trim(), conn);
                              status = "No";
                         }
                    } catch (Exception e) {
                         logWriter.writeLogGsma("Error in Getting Connection.." + e);
                         logger.debug("ITS NAN.." + e);
                         status = "NAN";
                    }finally{
                          logger.debug("Gsma Completed");
                    }
               }
          }
          return status;
     }
}
