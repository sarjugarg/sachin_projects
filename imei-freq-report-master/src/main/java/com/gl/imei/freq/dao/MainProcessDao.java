package com.gl.imei.freq.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gl.imei.freq.model.ImeiDupDb;
import com.gl.imei.freq.model.ImeiFreqDb;

@Component
public class MainProcessDao {

	private static final Logger logger = Logger.getLogger(MainProcessDao.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	  
	public ImeiDupDb getImeiCount() {
		logger.info("Going to fetch imei and its count from device duplicate db");
		try {
			String query="select count(msisdn) as count,imei from device_duplicate_db where imei is not null group by imei";
			logger.info("Query: "+query);
			return jdbcTemplate.query(query, new ResultSetExtractor<ImeiDupDb>() {
				  @Override
				public ImeiDupDb extractData(ResultSet rs) throws SQLException, DataAccessException {
					ImeiDupDb imeiDupDb = new ImeiDupDb();
					String tac="";
					String tacStatus="";
					while(rs.next()) {
						imeiDupDb.setMsisdnCount(rs.getLong("count"));
						imeiDupDb.setImei(rs.getString("imei"));
						/*
						 * tac=imeiDupDb.getImei().substring(0,8); Integer valid=validTac(tac); Integer
						 * invalid=invalidTac(tac); if(valid>0) { tacStatus="V"; }else if(invalid>0) {
						 * tacStatus="I"; }else { tacStatus="U"; } insertDetails(imeiDupDb,tacStatus);
						 */
						insertDetails(imeiDupDb);
					}
					return imeiDupDb;
				}
			});
		} catch (Exception e) {
			logger.info("Exception while fetching imei and its count from device duplicate db: "+e);
			return null;
		}
	}

	public void insertDetails_w_TAC(ImeiDupDb imeiDupDb,String tacStatus) {
		logger.info("Inserting into imei_msisdn_dup_count_db");
		try {
			Long msisdnCount=Long.sum(imeiDupDb.getMsisdnCount(), 1);
			String query="insert into imei_msisdn_dup_count_db(imei,msisdn_count,created_on,modified_on,TAC_status) values('"+imeiDupDb.getImei()+"','"+msisdnCount+""
					+ "',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'"+tacStatus+"')";
			logger.info("Query: "+query);
			jdbcTemplate.execute(query);
		} catch (Exception e) {
			logger.info("Exception in inserting details into imei_msisdn_dup_count_db: "+e);
		}

	}

	public void insertDetails(ImeiDupDb imeiDupDb) {
		logger.info("Inserting into imei_msisdn_dup_count_db");
		try {
			Long msisdnCount=Long.sum(imeiDupDb.getMsisdnCount(), 1);
			String query="insert into imei_msisdn_dup_count_db(imei,msisdn_count,created_on,modified_on) values('"+imeiDupDb.getImei()+"','"+msisdnCount+""
					+ "',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
			logger.info("Query: "+query);
			jdbcTemplate.execute(query);
		} catch (Exception e) {
			logger.info("Exception in inserting details into imei_msisdn_dup_count_db: "+e);
		}

	}
	
	public ImeiFreqDb getFreqCount() {
		logger.info("Fetching frequency of imei and its count from imei_msisdn_dup_count_db");
		try {
			String query="select msisdn_count, count(*) as count from imei_msisdn_dup_count_db group by msisdn_count order by msisdn_count desc";
			logger.info("Query: "+query);
			return jdbcTemplate.query(query, new ResultSetExtractor<ImeiFreqDb>() {
				@Override
				public ImeiFreqDb extractData(ResultSet rs) throws SQLException, DataAccessException {
					ImeiFreqDb imeifreqDb=new ImeiFreqDb();
					while(rs.next()) {
						imeifreqDb.setMsisdnFreq(rs.getLong("msisdn_count"));
						imeifreqDb.setImeiCount(rs.getLong("count"));
						insertDetails(imeifreqDb);
					}
					Long countUsage=getUsageDbCount();
					Long countDuplicate=getDuplicateDbUniqueCount();
					Long totalCount=Math.subtractExact(countUsage, countDuplicate);
					insertForOne(totalCount);
					return imeifreqDb;
				}
			});
		} catch (Exception e) {
			logger.info("Exception while fetching frequency of imei and its count from imei_msisdn_dup_count_db: "+e);
			return null;
		}
	}
	
	public void insertDetails(ImeiFreqDb imeifreqDb) {
		logger.info("Inserting into imei_msisdn_freq_db");
		try {
			String query="insert into imei_msisdn_freq_db(MSISDN_FREQ,IMEI_COUNT,created_on,modified_on) values('"+imeifreqDb.getMsisdnFreq()+"','"+imeifreqDb.getImeiCount()+""
					+ "',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
			logger.info("Query: "+query);
			jdbcTemplate.execute(query);
		} catch (Exception e) {
			logger.info("Exception in inserting details into imei_msisdn_freq_db: "+e);
		}

	}
	
	public Long getUsageDbCount() {
		logger.info("Getting count of device_usage_db");
		try {
			String query = "select count(*) as count from device_usage_db";
			logger.info("Query: "+query);
			return jdbcTemplate.query(query, new ResultSetExtractor<Long>() {
				@Override
				public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
					Long count = 0L;
					while(rs.next()) {
						count=rs.getLong("count");
					}
					return count;
				}
			});
		} catch (Exception e) {
			logger.info("Exception while getting count of device_usage_db: "+e);
			return null;
		}
	}
	
	public Long getDuplicateDbUniqueCount() {
		logger.info("Getting count of unique entries of device_duplicate_db");
		try {
			String query="select count(distinct imei) as count from device_duplicate_db";
			logger.info("Query: "+query);
			return jdbcTemplate.query(query,new ResultSetExtractor<Long>() {
				@Override
				public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
					Long count = 0L;
					while(rs.next()) {
						count=rs.getLong("count");
					}
					return count;
				}
			});
		} catch (Exception e) {
			logger.info("Exception while getting count of unique entries of device_duplicate_db: "+e);
			return null;
		}
	}
	
	public void insertForOne(Long count) {
		logger.info("Inserting number of msisdn with 1 imei with them in imei_msisdn_freq_db");
		try {
			String query="insert into imei_msisdn_freq_db(MSISDN_FREQ,IMEI_COUNT,created_on,modified_on) values(1,'"+count+"',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
			logger.info("Query: "+query);
			jdbcTemplate.execute(query);
		} catch (Exception e) {
			logger.info("Exception in inserting number of msisdn with 1 imei with them in imei_msisdn_freq_db: "+e);
		}
	}
	
	public Integer validTac(String tac) {
		logger.info("Checking if tac present in gsma_tac_db");
		try {
			String query="select count(*) as count from gsma_tac_db where DEVICE_ID='"+tac+"'";
			logger.info("Query: "+query);
			return jdbcTemplate.query(query,new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					Integer count=0;
					while(rs.next()) {
						count=rs.getInt("count");
					}
					return count;
				}
			});
		} catch (Exception e) {
			logger.info("Exception while Checking if tac present in gsma_tac_db: "+e);
			return null;
		}
	}
	
	public Integer invalidTac(String tac) {
		logger.info("Checking if tac present in gsma_invalid_tac_db");
		try {
			String query="select count(*) as count from gsma_invalid_tac_db where TAC='"+tac+"'";
			logger.info("Query: "+query);
			return jdbcTemplate.query(query,new ResultSetExtractor<Integer>() {
				@Override
				public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
					Integer count=0;
					while(rs.next()) {
						count=rs.getInt("count");
					}
					return count;
				}
			});
		} catch (Exception e) {
			logger.info("Exception while Checking if tac present in gsma_invalid_tac_db: "+e);
			return null;
		}
	}
}
