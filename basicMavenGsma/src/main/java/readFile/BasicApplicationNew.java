package readFile;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.security.KeyStore;
import java.sql.Connection;
import java.sql.DriverManager;

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
 


public class BasicApplicationNew {

     public static void main1(String[] args) {

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
               logger.error(" Error" + e);
          }
          logger.info("Connnection created successfully : " + conn);
          BufferedWriter bw1 = null;
          try {
               String fileNameInput1 = "/home/ceirapp/BasicImeiDetails/" + args[1] + "imeiTacInformation.csv";
               File fout1 = new File(fileNameInput1);
               FileOutputStream fos1 = new FileOutputStream(fout1, true);
               bw1 = new BufferedWriter(new OutputStreamWriter(fos1));
          } catch (Exception e) {
               logger.error("e " + e);
          }
          GsmaDbDao snt = new GsmaDbDao();
          Map<String, String> map = snt.getGsmaApiDetails(conn);
          try {
               File file = new File(args[0]);
               FileReader fr = new FileReader(file);
               BufferedReader br = new BufferedReader(fr);
               String line;
               BasicApplicationNew nw = new BasicApplicationNew();
               bw1.write("ImeiTac | Brand_name | Model_name  | Equipment Type | OS Type");
               bw1.newLine();
               bw1.flush();
               while ((line = br.readLine()) != null) {
                    logger.info("TAC IS " + line);
                    if (line.trim().replace(",", "").length() <= 6) {
                         bw1.write(line + " | INVALID | INVALID  | INVALID | INVALID  ");
                         bw1.newLine();
                         bw1.flush();
                    } else {
                         nw.gsmaApplication(line.trim().replace(",", ""), conn, bw1, map);
                    }

               }
               br.close();
               bw1.close();
               conn.commit();
               conn.close();
               System.exit(0);
          } catch (Exception e) {
               logger.error(" Error" + e);
          }

     }

     static final Logger logger = Logger.getLogger(BasicApplicationNew.class);
     private static final String KEYSTOREPATH = "/home/ubuntu/CEIR/GSMA_CLIENT/gsma_ks.jks"; // or .p12
     private static final String KEYSTOREPASS = "Hello1234";
     private static final String KEYPASS = "keypass";

     public KeyStore readStore() throws Exception {
          try (InputStream keyStoreStream = this.getClass().getResourceAsStream(KEYSTOREPATH)) {
               KeyStore keyStore = KeyStore.getInstance("JKS"); // or "PKCS12"
               keyStore.load(keyStoreStream, KEYSTOREPASS.toCharArray());
               return keyStore;
          }
     }

     public String gsmaApplication(String imei_tac, Connection conn, BufferedWriter bw1, Map<String, String> map) throws InterruptedException, Exception {
          GsmaDbDao snt = new GsmaDbDao();
          LogWriterNew logWriter = new LogWriterNew();
          String status = "Yes";
          if (imei_tac.length() == 7) {
               imei_tac = "0" + imei_tac;
          }
          status = snt.getExistingGsmaDetails(imei_tac, conn, bw1);

          logger.debug("Status ..  " + status);
          if (status.equalsIgnoreCase("NAN")) {
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
               BasicApplicationNew obj = new BasicApplicationNew();
               SSLContext sslContext = SSLContexts.custom()
                       .loadKeyMaterial(obj.readStore(), KEYPASS.toCharArray()) // use null as second param if you don't have a separate key password
                       .build();
               String testrequest = "{\"portName\":\"Phnom penh\",\"country\": \"Cambodia\", \"portType\": \"AIR\",\"deviceId\":\"" + imei_tac + "\"}";
               StringEntity input = new StringEntity(testrequest);
               input.setContentType("application/json");

               HttpClient httpClient = HttpClients.custom().setSslcontext(sslContext).build();
//         HttpPost request = new HttpPost("https://devicecheck.gsma.com/imeirtl/leadclookup");        ///     lookup ... blacklist    //commented
//        HttpPost request = new HttpPost("https://imeidb.gsma.com/services/rest/GetHandSetDetails");   // original   ... tac

               HttpPost request = new HttpPost(httpPostUrl);
               String auth = EncriptonService.getAuth(imei_tac, APIKey, Password, Salt_String, Organization_Id, Secretkey);      // for original.. ta 
               request.addHeader("Authorisation", auth);
               request.addHeader("Content-Type", "application/json");     // original
               request.setEntity(input);

//                int hardTimeout = 15; // seconds
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
                         String getOperatingSystem = product.getOperatingSystem().contains("[") ? product.getOperatingSystem().replace("[", " ") : product.getOperatingSystem();
                         getOperatingSystem = getOperatingSystem.contains("]") ? getOperatingSystem.replace("]", " ") : getOperatingSystem;
//                         logger.debug("gsma_tac_db getOperatingSystem ...." + getOperatingSystem);

                         bw1.write(imei_tac + " | " + product.getBrandName() + " | " + product.getModelName() + "|" + product.getEquipmentType() + "|" + getOperatingSystem);
                         bw1.newLine();
                         bw1.flush();

                         status = snt.databaseMapper(message, conn);
                         status = "Yes";
                    } else {
                         logger.debug("GSMAINVALIDDB");
                         snt.invalidGsmaDb(product.getDeviceId(), conn);
                         status = "No";
                         bw1.write(imei_tac + " | " + product.getBrandName() + " | " + product.getModelName() + "|" + product.getEquipmentType() + "|" + product.getOperatingSystem());
                         bw1.newLine();
                         bw1.flush();
                    }
               } catch (Exception e) {
                    logWriter.writeLogGsma("Error in Getting Connection.." + e);
                    logger.debug("ITS NAN.." + e);
                    status = "NAN";
               }
          }

          return status;
     }

}
