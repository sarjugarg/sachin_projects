package com.gl.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Util {

    public static String defaultDate(boolean isOracle) {
        if (isOracle) {
            return "sysdate";
        } else {
            return "now()";
        }
    }

//         String dateNow = "";
//                    if (conn.toString().contains("oracle")) {
//                        dateNow = new SimpleDateFormat("dd-MMM-YY").format(new Date());
//                    } else {
//                        dateNow = new SimpleDateFormat("YYYY-MM-dd").format(new Date());
//                    }
  

        public static String defaultDateNow(boolean isOracle) {
          if (isOracle) {

               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               String val = sdf.format(new Date());
               String date = "TO_DATE( '" + val + "','YYYY-MM-DD HH24:MI:SS')";
               return date;
          } else {
               return "now()";
          }
     }
}



