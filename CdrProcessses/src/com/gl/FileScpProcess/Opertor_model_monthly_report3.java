/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

import static com.gl.FileScpProcess.ImeiDeltaMonthlyReport2.log;
import java.sql.Connection;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author maverick
 */
public class Opertor_model_monthly_report3 {

    static Logger log = Logger.getLogger(Opertor_model_monthly_report3.class);

    public static void main(String[] args) {
        String operator = args[0];
        String source = args[1];
        String time = null;
        if (args.length > 2) {
            time = args[2];
        }
        try {
            String query = " delete from opertor_model_monthly_report where  file_month = '" + time + "' and operator =    '" + operator + "' ";
            log.info(query);
            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();

            Statement stmtnew = conn.createStatement();
            stmtnew.executeUpdate(query);
            stmtnew.close();
            query = " insert into opertor_model_monthly_report (created_on ,modified_on, operator , model_name , model_count ,file_month  )  \n"
                    + "  select current_timestamp ,current_timestamp , MOBILE_OPERATOR , MODEL_NAME , count(tac) , '" + time + "' from device_usage_Db inner join gsma_tac_db on   device_usage_Db.tac =  gsma_tac_db.device_id where MOBILE_OPERATOR =  UPPER('" + operator + "')  and IMEI_ARRIVAL_TIME like '" + time + "%'  group by model_name ,MOBILE_OPERATOR    ";
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
