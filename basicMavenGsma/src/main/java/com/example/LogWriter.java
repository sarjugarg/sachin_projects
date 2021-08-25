package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;

import java.text.SimpleDateFormat;



public class LogWriter {
    final String logPath = ""; 
    
    
    public boolean writeLog(String log) {
        boolean result = false;
        PrintWriter pw = null;
        File file = null;
        String fileName = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fileName = "GsmaTacApiResults.log";
            file = new File(logPath);
            if (!file.exists()) {
                file.mkdir();
            }
            file = new File(logPath + fileName);
            pw = new PrintWriter(new BufferedWriter(new FileWriter(logPath + fileName, true)));
            if (file.length() == 0) {
                pw.println("Gsma Tac Logs ...");
            }
            pw.println(sdf + " : " + log);
            pw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            result = true;
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception ex) {
            }
        }
        return result;
    }

}
