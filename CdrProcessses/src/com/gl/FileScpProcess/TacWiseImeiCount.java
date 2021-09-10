/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.FileScpProcess;

import java.sql.Connection;
import java.sql.Statement;
import org.apache.log4j.Logger;

/**
 *
 * @author maverick
 */
public class TacWiseImeiCount {

    static Logger log = Logger.getLogger(TacWiseImeiCount.class);

    public static void main(String[] args) {
        String time = null;
        if (args.length > 0) {
            time = args[0];
        }
        StringBuffer newString = new StringBuffer(time);
        String timeline = newString.insert(4, "-").toString();
        try {
            String query0 = " delete from tac_wise_imei_count where  file_month = '" + timeline + "'   ";

            String query1 = "insert into tac_wise_imei_count( file_month, created_on, modified_on , tac , total_imei_usage_db )"
                    + " select '" + timeline + "' ,   current_timestamp, current_timestamp  , tac , count(tac ) from device_usage_db group by tac  ";

            String query2 = "  update tac_wise_imei_count set total_imei_dublicate_db = (select count(tac) from device_duplicate_db where device_duplicate_db.tac = tac_wise_imei_count.tac group by tac ) "
                    + "        where file_month   =   '" + timeline + "'   ";

            String query3 = "  update tac_wise_imei_count set status =  (select FEATURE_NAME  from device_usage_db where device_usage_db.tac = tac_wise_imei_count.tac  fetch next 1 rows only )  "
                    + "        where file_month   =   '" + timeline + "'   ";

            String query4 = "update tac_wise_imei_count set    total_imei_usage_db_file_month = ( select count(tac) from device_usage_db where device_usage_db.tac = tac_wise_imei_count.tac and IMEI_ARRIVAL_TIME like   '" + time + "%'  ) "
                    + "        where file_month   =   '" + timeline + "'   ";

            String query5 = " update tac_wise_imei_count set total_imei_dublicate_db_file_month = ( select count(tac) from device_duplicate_db where device_duplicate_db.tac = tac_wise_imei_count.tac and IMEI_ARRIVAL_TIME like  '" + time + "%' )  "
                    + "        where file_month   =   '" + timeline + "'   ";

            String query6 = " update tac_wise_imei_count set brand_name = ( select gsma_tac_db.brand_name from gsma_tac_db where gsma_tac_db.device_id  = tac_wise_imei_count.tac ) , "
                    + "marketing_name = ( select gsma_tac_db.marketing_name from gsma_tac_db where gsma_tac_db.device_id =tac_wise_imei_count.tac )  , "
                    + "model_name  = ( select gsma_tac_db.model_name from gsma_tac_db where gsma_tac_db.device_id = tac_wise_imei_count.tac )  "
                    + "        where file_month   =   '" + timeline + "'   ";

            String query7 = "   update tac_wise_imei_count set brand_name = 'NA' , model_name ='NA' , marketing_name = 'NA' where status != 'V'  "
                    + "        where file_month   =   '" + timeline + "'   ";

            String query8 = "  update tac_wise_imei_count set total_imei_dublicate_db = 0 where total_imei_dublicate_db is null";

            Connection conn = (Connection) new com.gl.FileScpProcess.MySQLConnection().getConnection();

            String query = " ";
            for (int i = 0; i < 9; i++) {

                if (i == 0) {
                    query = query0;
                }
                if (i == 1) {
                    query = query1;
                }
                if (i == 2) {
                    query = query2;
                }
                if (i == 3) {
                    query = query3;
                }
                if (i == 4) {
                    query = query4;
                }
                if (i == 5) {
                    query = query5;
                }
                if (i == 6) {
                    query = query6;
                }
                if (i == 7) {
                    query = query7;
                }
                if (i == 8) {
                    query = query8;
                }
                Statement stmt = conn.createStatement();
                log.info(query);
                stmt.executeUpdate(query);
                stmt.close();
                conn.commit();
            }
            log.info("Done for file_month = '" + time + "'  ");
        } catch (Exception e) {
            log.info("Failed  " + e);
            e.printStackTrace();
        }
    }

}
