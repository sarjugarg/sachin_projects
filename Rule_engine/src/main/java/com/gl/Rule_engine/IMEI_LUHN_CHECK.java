/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.Rule_engine;

import java.sql.Connection;
import java.io.BufferedWriter;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
class IMEI_LUHN_CHECK {

     static final Logger logger = Logger.getLogger(IMEI_LUHN_CHECK.class);

     static String executeRule(String[] args, Connection conn) {
          String res = "Yes";
          if (args[9].trim().equalsIgnoreCase("IMEI") || args[10].trim().equalsIgnoreCase("GSM")) {
               if (args[3].length() == 16) {
                    res = "Yes";
               } else {
                    res = ExecuteLuhnAlgorithm(args[3]);
               }
          } else {
               res = "Yes";
          }
          return res;
     }

     static String executeAction(String[] args, Connection conn, BufferedWriter bw) {
          logger.debug(" IMEI_LUHN_CHECK  executeAction");

          try {
               switch (args[13]) {
                    case "Allow": {
                         logger.debug("Action is Allow");
                    }
                    break;
                    case "Skip": {
                         logger.debug("Action is Skip");
                    }
                    break;
                    case "Reject": {
                         logger.debug("Action is Reject");
                         String fileString = args[15] + " ,Error Code :CON_RULE_0005,  Error Description : IMEI does not pass the Checksum algorithm  ";
                         bw.write(fileString);
                         bw.newLine();
                    }
                    break;
                    case "Block": {
                         logger.debug("Action is Block");
                    }
                    break;
                    case "Report": {
                         logger.debug("Action is Report");
                    }
                    break;

                    case "SYS_REG": {
                         logger.debug("Action is SYS_REG");
                    }
                    break;
                    case "USER_REG": {
                         logger.debug("Action is USER_REG");
                    }
                    break;
                    default:
                         logger.debug(" The Action " + args[13] + "  is Not Defined  ");
               }

               return "Success";
          } catch (Exception e) {
               logger.error(" Error " + e);
               return "Failure";
          } finally {
               logger.debug("Luhn Check Finished ");
          }

     }

     static String ExecuteLuhnAlgorithm(String ImeiNo) {
          String ress = "No";

          if (ImeiNo.length() != 15) {
               logger.debug("IMEI Number should contain 15 characters"  + ImeiNo);
          } else {
               int sum = 0;
               boolean errorflag = false;
               for (int i = 0; i <= 14; i++) {
                    //getting ascii value for each character  
                    char c = ImeiNo.charAt(i);
                    int number = c;
                    //Assigning number values to corrsponding Ascii value  
                    if (number < 48 || number > 57) {
                         logger.debug("Enter only numerals");
                         errorflag = true;
                         break;
                    } else {
                         switch (number) {
                              case 48:
                                   number = 0;
                                   break;
                              case 49:
                                   number = 1;
                                   break;
                              case 50:
                                   number = 2;
                                   break;
                              case 51:
                                   number = 3;
                                   break;
                              case 52:
                                   number = 4;
                                   break;
                              case 53:
                                   number = 5;
                                   break;
                              case 54:
                                   number = 6;
                                   break;
                              case 55:
                                   number = 7;
                                   break;
                              case 56:
                                   number = 8;
                                   break;
                              case 57:
                                   number = 9;
                                   break;
                         }
                         //Double the even number and divide it by 10. add quotient and remainder  
                         if ((i + 1) % 2 == 0) {
                              number = number * 2;
                              number = number / 10 + number % 10;
                         }
                         sum = sum + number;
                    }
               }
               // Check the error flag to avoid overWriting of Warning Lable  
               if (!errorflag) {
                    if (sum % 10 == 0) {
                         logger.debug("Valid");
                         ress = "Yes";
                    } else {
                         logger.debug("Invalid");
                         ress = "No";
                    }
               }

          }
          return ress;
     }

}
