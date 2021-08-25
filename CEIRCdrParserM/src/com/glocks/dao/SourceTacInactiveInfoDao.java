package com.glocks.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class SourceTacInactiveInfoDao {
	static Logger logger = Logger.getLogger(SourceTacInactiveInfoDao.class);
	static String GENERIC_DATE_FORMAT = "dd-MM-yyyy";

	public int deleteFromSourceTacInactiveInfo(Connection conn, String txnId) {
		String query = "";
		Statement stmt = null;
		int executeStatus = 0;

		query = "delete from source_tac_inactive_info where txn_id='" + txnId + "'";	
		logger.info("delete source_tac_inactive_info ["+query+"]");
		 // System.out.println("delete device_importer_db ["+query+"]");

		try {
			stmt = conn.createStatement();
			executeStatus = stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return executeStatus;
	}  

}