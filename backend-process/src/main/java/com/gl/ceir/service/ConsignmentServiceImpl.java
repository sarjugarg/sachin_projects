package com.gl.ceir.service;

import com.gl.ceir.configuration.PropertiesReader;
import com.gl.ceir.constant.Datatype;
import com.gl.ceir.constant.SearchOperation;
import com.gl.ceir.entity.ConsignmentMgmt;
import com.gl.ceir.entity.ConsignmentRevenueDailyDb;
import com.gl.ceir.pojo.SearchCriteria;
import com.gl.ceir.repo.ConsignmentRepository;
import com.gl.ceir.specification.GenericSpecificationBuilder;
import com.gl.ceir.util.DateUtil;
import com.gl.ceir.util.Utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;

@Service
public class ConsignmentServiceImpl {
  private static final Logger logger = LogManager.getLogger(com.gl.ceir.service.ConsignmentServiceImpl.class);
  
  @Autowired
  ConsignmentRepository consignmentRepository;
  
  @Autowired
  PropertiesReader propertiesReader;
  
  @Autowired
  Utility utility;
  
  @Value("${spring.jpa.properties.hibernate.dialect}")
  private String dialect;
  
  @Autowired
  private JdbcTemplate jdbcTemplate;
  
  public Integer getCountOfRecordsForConsignmentRevenueDailyDb() {
    logger.info("Getting count of number of record in ConsignmentRevenueDailyDb");
    String query = "select count(*) as count from consignment_revenue_daily_db";
    logger.info("query: " + query);
    return jdbcTemplate.query(query, new ResultSetExtractor<Integer>(){
    	@Override
    	public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
    		Integer count =0;
    		while(rs.next()) {
    			count=rs.getInt("count");
    		}
    		return count;
    	}
    });
  }
  
  public LocalDateTime getOldestDate() {
    String query = "";
    logger.info("Getting date of oldest Consignment in ConsignmentMgmt table");
    if (this.dialect.contains("Oracle12cDialect")) {
      query = "SELECT modified_on FROM consignment_mgmt ORDER BY modified_on ASC  FETCH FIRST 1 ROW ONLY";
    } else {
      query = "SELECT modified_on FROM consignment_mgmt ORDER BY modified_on ASC limit 1";
    } 
    logger.info("query: " + query);
    return jdbcTemplate.query(query, new ResultSetExtractor<LocalDateTime>() {
    	@Override
    	public LocalDateTime extractData(ResultSet rs) throws SQLException, DataAccessException {
    		LocalDateTime dt = null;
    		while(rs.next()) {
    			dt=rs.getTimestamp("modified_on").toLocalDateTime();
    		}
    		return dt;
    	}
    });
  }
  
  public LocalDateTime getLastDate() {
    String query = "";
    logger.info("Getting date of last record in ConsignmentRevenueDailyDb table");
    if (this.dialect.contains("Oracle12cDialect")) {
      query = "SELECT created_on FROM consignment_revenue_daily_db ORDER BY created_on DESC  FETCH FIRST 1 ROW ONLY";
    } else {
      query = "SELECT created_on FROM consignment_revenue_daily_db ORDER BY created_on DESC limit 1";
    } 
    logger.info("query: " + query);
    return jdbcTemplate.query(query, new ResultSetExtractor<LocalDateTime>() {
    	@Override
    	public LocalDateTime extractData(ResultSet rs) throws SQLException, DataAccessException {
    		LocalDateTime dt = null;
    		while(rs.next()) {
    			dt=rs.getTimestamp("created_on").toLocalDateTime();
    		}
    		return dt;
    	}
    });
  }
  
  public ConsignmentRevenueDailyDb getCounts(LocalDateTime d1) {
    logger.info("Getting total count of consignments, devices and imei");
    String query = "";
    String fromDate = "";
    String substr = d1.toString();
    String toDate = DateUtil.nextDate(0);
    fromDate = substr.substring(0, 10);
    if (this.dialect.contains("Oracle12cDialect")) {
      query = "SELECT count(*)as count,sum(quantity) as sumImei,sum(device_quantity) as sumDevice FROM consignment_mgmt where total_price>0 and to_char(created_on,'yyyy-mm-dd')>='" + fromDate + "' and to_char(created_on,'yyyy-mm-dd')<'" + toDate + "'";
    } else {
      query = "SELECT count(*)as count,sum(quantity) as sumImei,sum(device_quantity) as sumDevice FROM consignment_mgmt where total_price>0 and created_on>='" + fromDate + "' and created_on<'" + toDate + "'";
    } 
    logger.info("query: " + query);
    return jdbcTemplate.query(query, new ResultSetExtractor<ConsignmentRevenueDailyDb>() {
    	@Override
    	public ConsignmentRevenueDailyDb extractData(ResultSet rs) throws SQLException, DataAccessException {
    		ConsignmentRevenueDailyDb consignmentRevenueDailyDb = new ConsignmentRevenueDailyDb();
    		while(rs.next()) {
    			consignmentRevenueDailyDb.setCountOfConsignmentWhenPrice(rs.getInt("count"));
    			consignmentRevenueDailyDb.setCountOfDevicesWhenPrice(rs.getInt("sumDevice"));
    			consignmentRevenueDailyDb.setCountOfImeiWhenPrice(rs.getInt("sumImei"));
    		}
    		return consignmentRevenueDailyDb;
    	}
    });
  }
  
  public Integer getTotalConsignmentCount(LocalDateTime d1) {
    logger.info("Getting total count of consignments");
    String query = "";
    String fromDate = "";
    String substr = d1.toString();
    String toDate = DateUtil.nextDate(0);
    fromDate = substr.substring(0, 10);
    if (this.dialect.contains("Oracle12cDialect")) {
      query = "Select count(*) as count from consignment_mgmt where to_char(created_on,'yyyy-mm-dd')>='" + fromDate + "' and to_char(created_on,'yyyy-mm-dd')<'" + toDate + "'";
    } else {
      query = "Select count(*) as count from consignment_mgmt where created_on>='" + fromDate + "' and created_on<'" + toDate + "'";
    } 
    logger.info("Query: " + query);
    return jdbcTemplate.query(query, new ResultSetExtractor<Integer>() {
    	@Override
    	public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
    		Integer count =0;
    		while(rs.next()) {
    			count=rs.getInt("count");
    		}
    		return count;
    	}
    });
  }
  
  public List<ConsignmentMgmt> getConsignmentWithTotalPriceGreaterThanZero(LocalDateTime date) {
    try {
      String fromDate = "";
      String substr = date.toString();
      String toDate = DateUtil.nextDate(0);
      fromDate = substr.substring(0, 10);
      logger.info("inside from date: " + fromDate + " and toDate: " + toDate);
      return this.consignmentRepository.findAll(buildSpecification(fromDate, toDate).build());
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      return new ArrayList<>(1);
    } 
  }
  
  private GenericSpecificationBuilder<ConsignmentMgmt> buildSpecification(String fromDate, String toDate) {
    GenericSpecificationBuilder<ConsignmentMgmt> cmsb = new GenericSpecificationBuilder(this.propertiesReader.dialect);
    cmsb.with(new SearchCriteria("createdOn", fromDate, SearchOperation.GREATER_THAN_OR_EQUAL, Datatype.DATE));
    cmsb.with(new SearchCriteria("createdOn", toDate, SearchOperation.LESS_THAN, Datatype.DATE));
    cmsb.with(new SearchCriteria("totalPrice", Integer.valueOf(0), SearchOperation.GREATER_THAN, Datatype.STRING));
    return cmsb;
  }
}
