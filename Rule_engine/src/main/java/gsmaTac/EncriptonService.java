package gsmaTac;

import java.math.BigInteger;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;

public class EncriptonService {

//	static String APIKey = "ZqXBvdRLYRiAWCO";
//	static String Password = "LdTcsQYaYuxRabQ";
//	static String Salt_String = "GSMA";
//	static String Organization_Id = "9101";
//	static String Secretkey = "imeaesencryption";
     static final Logger logger = Logger.getLogger(EncriptonService.class);

     public static String getAuth(String deviceId, String APIKey, String Password, String Salt_String, String Organization_Id, String Secretkey) {
          String abc = getSHA(APIKey + Password + deviceId);
          logger.debug("SHA output " + abc);
          String auth = encrypt(Salt_String + Organization_Id + "=" + abc, Secretkey);
          return auth;
     }

     // Java program to calculate SHA hash value 
     private static SecretKeySpec secretKey;
     private static byte[] key;

     public static String getSHA(String input) {

          try {

               // Static getInstance method is called with hashing SHA 
               MessageDigest md = MessageDigest.getInstance("SHA-256");

               // digest() method called 
               // to calculate message digest of an input 
               // and return array of byte 
               byte[] messageDigest = md.digest(input.getBytes());

               // Convert byte array into signum representation 
               BigInteger no = new BigInteger(1, messageDigest);

               // Convert message digest into hex value 
               String hashtext = no.toString(16);
               while (hashtext.length() < 64) {
                    hashtext = "0" + hashtext;
               }

               return hashtext;
          } // For specifying wrong message digest algorithms 
          catch (NoSuchAlgorithmException e) {
               logger.error("Exception thrown"
                       + " for incorrect algorithm: " + e);

               return null;
          }
     }

     public static String encrypt(String strToEncrypt, String secret) {
          try {
               setKey(secret);
               Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
               cipher.init(Cipher.ENCRYPT_MODE, secretKey);
               return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
          } catch (Exception e) {
               logger.error("Error while encrypting: " + e.toString());
          }
          return null;
     }

     public static void setKey(String myKey) {
          MessageDigest sha = null;
          try {
               key = myKey.getBytes("UTF-8");
               sha = MessageDigest.getInstance("SHA-1");
//	            key = sha.digest(key);
//	            key = Arrays.copyOf(key, 16);
               secretKey = new SecretKeySpec(key, "AES");
          } catch (NoSuchAlgorithmException e) {
               e.printStackTrace();
               logger.error(" " + e.toString());
          } catch (UnsupportedEncodingException e) {

               logger.error(" " + e.toString());
               e.printStackTrace();

          }
     }
}
