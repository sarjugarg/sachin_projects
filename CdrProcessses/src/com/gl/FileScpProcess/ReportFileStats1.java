package com.gl.FileScpProcess;

import static com.gl.FileScpProcess.CP3FileTransfer.log;
import static com.gl.FileScpProcess.ImeiDeltaMonthlyReport2.log;
import static com.gl.FileScpProcess.Opertor_model_monthly_report3.log;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author maverick
 */
public class ReportFileStats1 {

     static Logger log = Logger.getLogger(ReportFileStats1.class);
    public static void main(String[] args) {
        String operator = args[0];
        String source = args[1];
        String time = null;
        if (args.length > 2) {
            time = args[2];
        }
       try {
            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();
           
           
            String query = " delete from file_record_stats_report where  file_month  = '" + time + "' and operator=  '" + operator + "'  and source =  '" + source + "'  ";
            log.info(query);

            Statement stmtnew = conn.createStatement();
            stmtnew.executeUpdate(query);
            stmtnew.close();
                query = " insert into file_record_stats_report (created_on ,modified_on, operator ,source,file_count , file_month , avg_record_count) values"
                    + " ( current_timestamp,current_timestamp, '"+operator+"' , '"+source+"' , ( select count(*) from cdr_file_delete_aud  where operator = '"+operator+"' and source = '"+source+"' and file_date like '"+time+"%'  ) ,  '"+time+"'    , ( select sum(record_size) from cdr_file_delete_aud  where operator = '"+operator+"' and source = '"+source+"' and file_date like '"+time+"%'  )     )  ";
            log.info(query);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
                     log.info("Done for file_month = '" + time + "' and operator =    '" + operator + "' ");
     
        } catch (Exception e) {
            log.info("Failed  " + e);
            e.printStackTrace();
        }   
    }

}
