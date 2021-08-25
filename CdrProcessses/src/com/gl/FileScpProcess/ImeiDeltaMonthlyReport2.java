/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

import static com.gl.FileScpProcess.Opertor_model_monthly_report3.log;
import java.sql.Connection;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author maverick
 */
public class ImeiDeltaMonthlyReport2 {

    static Logger log = Logger.getLogger(ImeiDeltaMonthlyReport2.class);

    public static void main(String[] args) {
        String operator = args[0];
        String source = args[1];
        String time = null;
        if (args.length > 2) {
            time = args[2];
        }
        try {

            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();

            String query = " delete from imei_delta_monthly_report where  file_month = '" + time + "' and operator =    '" + operator + "' ";
            log.info(query);

            Statement stmtnew = conn.createStatement();
            stmtnew.executeUpdate(query);
            stmtnew.close();

            query = "insert into imei_delta_monthly_report (created_on ,modified_on, operator , total_update_imei_in_usg , total_update_imei_in_dup , total_arrival_imei_in_usg , total_arrival_imei_in_dup ,  file_month  )"
                    + " values ( current_timestamp,current_timestamp, '" + operator + "'  , \n"
                    + " ( select count(*) from device_usage_db  where MOBILE_OPERATOR = UPPER('" + operator + "') and UPDATE_IMEI_ARRIVAL_TIME  like '" + time + "%'  ) , \n"
                    + " ( select count(*) from device_duplicate_db  where MOBILE_OPERATOR =  UPPER('" + operator + "') and UPDATE_IMEI_ARRIVAL_TIME  like'" + time + "%'  ) , \n"
                    + "  ( select count(*) from device_usage_db  where MOBILE_OPERATOR =  UPPER('" + operator + "') and IMEI_ARRIVAL_TIME  like '" + time + "%'  ) , \n"
                    + " ( select count(*) from device_duplicate_db  where MOBILE_OPERATOR =  UPPER('" + operator + "') and IMEI_ARRIVAL_TIME  like '" + time + "%'  ) , \n"
                    + "  '" + time + "' ) ";

            log.info(query);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();

            Statement stmt2 = conn.createStatement();
            String newquery = " update imei_delta_monthly_report  set total_update_imei = ( to_number(total_update_imei_in_usg) +  to_number(total_update_imei_in_dup) ) ,  total_arrival_imei  = ( to_number(total_arrival_imei_in_usg ) +  to_number(total_arrival_imei_in_dup) )  where total_update_imei is null  ";

            log.info(newquery);
            stmt2.executeUpdate(newquery);
            stmt2.close();
            log.info("Done for file_month = '" + time + "' and operator =    '" + operator + "' ");
        } catch (Exception e) {
            log.info("Failed  " + e);
        }
    }

}
