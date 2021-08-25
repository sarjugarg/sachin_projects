package com.gl.ceir.factory.service.impl;

import com.gl.ceir.converter.CurrencyConverter;
import com.gl.ceir.entity.ConsignmentMgmt;
import com.gl.ceir.entity.ConsignmentRevenueDailyDb;
import com.gl.ceir.entity.Currency;
import com.gl.ceir.factory.service.BaseService;
import com.gl.ceir.repo.ConsignmentRevenueDailyRepository;
import com.gl.ceir.repo.CurrencyConversionRepository;
import com.gl.ceir.repo.SystemConfigListRepository;
import com.gl.ceir.service.ConsignmentServiceImpl;
import com.gl.ceir.service.CurrencyConversionServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsignmentRevenueService extends BaseService {
  private static final Logger logger = LogManager.getLogger(ConsignmentRevenueService.class);
  
  Map<Integer, Currency> exchangeRateMap = null;
  
  @Autowired
  SystemConfigListRepository systemConfigListRepository;
  
  @Autowired
  CurrencyConversionRepository currencyConversionRepository;
  
  @Autowired
  ConsignmentRevenueDailyRepository consignmentRevenueDailyRepository;
  
  @Autowired
  ConsignmentServiceImpl consignmentServiceImpl;
  
  @Autowired
  CurrencyConversionServiceImpl currencyConversionServiceImpl;
  
  @Autowired
  CurrencyConverter currencyConverter;
  
  public void fetch() {
    try {
      ConsignmentRevenueDailyDb consignmentRevenueDailyDb = new ConsignmentRevenueDailyDb();
      Integer count = this.consignmentServiceImpl.getCountOfRecordsForConsignmentRevenueDailyDb();
      if (count.intValue() > 0) {
        LocalDateTime fromDate = this.consignmentServiceImpl.getLastDate();
        logger.info("fromDate: " + fromDate);
        List<ConsignmentMgmt> consignmentMgmts = this.consignmentServiceImpl.getConsignmentWithTotalPriceGreaterThanZero(fromDate);
        logger.info("list: " + consignmentMgmts);
        ConsignmentRevenueDailyDb dailyCounts = this.consignmentServiceImpl.getCounts(fromDate);
        Integer countOfConsignment = this.consignmentServiceImpl.getTotalConsignmentCount(fromDate);
        this.exchangeRateMap = this.currencyConversionServiceImpl.getCurrencyRateOfCurrentMonthMap();
        if (consignmentMgmts.isEmpty()) {
          logger.info("No valid consignment of having total price greater than zero today is found.");
        } else {
          for (ConsignmentMgmt consignmentMgmt : consignmentMgmts) {
            logger.info("inside loop for consignment id: " + consignmentMgmt.getId());
            if (consignmentRevenueDailyDb.getTotalAmountInDollar() == null)
              consignmentRevenueDailyDb.setTotalAmountInDollar(Double.valueOf(0.0D)); 
            if (consignmentRevenueDailyDb.getTotalAmountInRiel() == null)
              consignmentRevenueDailyDb.setTotalAmountInRiel(Double.valueOf(0.0D)); 
            if (consignmentRevenueDailyDb.getCountOfDevicesWhenPrice() == null)
              consignmentRevenueDailyDb.setCountOfDevicesWhenPrice(Integer.valueOf(0)); 
            if (consignmentRevenueDailyDb.getCountOfImeiWhenPrice() == null)
              consignmentRevenueDailyDb.setCountOfImeiWhenPrice(Integer.valueOf(0)); 
            if (consignmentMgmt.getDeviceQuantity() == null)
              consignmentMgmt.setDeviceQuantity(Integer.valueOf(0)); 
            if (consignmentMgmt.getCurrency().intValue() == 0 || consignmentMgmt.getCurrency() == null || this.exchangeRateMap.get(consignmentMgmt.getCurrency()) == null)
              continue; 
            consignmentRevenueDailyDb.setTotalAmountInDollar(
                Double.valueOf(consignmentRevenueDailyDb.getTotalAmountInDollar().doubleValue() + this.currencyConverter.exchangeRate((
                    (Currency)this.exchangeRateMap.get(consignmentMgmt.getCurrency())).getDollar(), 
                    consignmentMgmt.getTotalPrice().doubleValue())));
            logger.info("amount in dollar: " + consignmentRevenueDailyDb.getTotalAmountInDollar());
            consignmentRevenueDailyDb.setTotalAmountInRiel(
                Double.valueOf(consignmentRevenueDailyDb.getTotalAmountInRiel().doubleValue() + this.currencyConverter.exchangeRate((
                    (Currency)this.exchangeRateMap.get(consignmentMgmt.getCurrency())).getRiel(), 
                    consignmentMgmt.getTotalPrice().doubleValue())));
            logger.info("amount in riel: " + consignmentRevenueDailyDb.getTotalAmountInRiel());
            consignmentRevenueDailyDb.setCountOfDevicesWhenPrice(
                Integer.valueOf(consignmentRevenueDailyDb.getCountOfDevicesWhenPrice().intValue() + consignmentMgmt.getDeviceQuantity().intValue()));
            logger.info("count of device: " + consignmentRevenueDailyDb.getCountOfDevicesWhenPrice());
            consignmentRevenueDailyDb.setCountOfImeiWhenPrice(
                Integer.valueOf(consignmentRevenueDailyDb.getCountOfImeiWhenPrice().intValue() + consignmentMgmt.getQuantity()));
            logger.info("count of imei: " + consignmentRevenueDailyDb.getCountOfImeiWhenPrice());
          } 
          consignmentRevenueDailyDb.setCountOfConsignmentWhenPrice(dailyCounts.getCountOfConsignment());
          logger.info("Count of consignment when price: " + consignmentRevenueDailyDb.getCountOfConsignmentWhenPrice());
          consignmentRevenueDailyDb.setCountOfConsignment(countOfConsignment);
          logger.info("Total Count of consignment: " + consignmentRevenueDailyDb.getCountOfConsignment());
          consignmentRevenueDailyDb.setCountOfDevices(dailyCounts.getCountOfDevices());
          logger.info("Count of devices when price: " + consignmentRevenueDailyDb.getCountOfDevices());
          consignmentRevenueDailyDb.setCountOfImei(dailyCounts.getCountOfImei());
          logger.info("Count of imei when price: " + consignmentRevenueDailyDb.getCountOfImei());
          process(consignmentRevenueDailyDb);
        } 
      } else {
        LocalDateTime fromDate = this.consignmentServiceImpl.getOldestDate();
        logger.info("fromDate: " + fromDate);
        List<ConsignmentMgmt> consignmentMgmts = this.consignmentServiceImpl.getConsignmentWithTotalPriceGreaterThanZero(fromDate);
        logger.info("list: " + consignmentMgmts);
        ConsignmentRevenueDailyDb dailyCounts = this.consignmentServiceImpl.getCounts(fromDate);
        Integer countOfConsignment = this.consignmentServiceImpl.getTotalConsignmentCount(fromDate);
        this.exchangeRateMap = this.currencyConversionServiceImpl.getCurrencyRateOfCurrentMonthMap();
        if (consignmentMgmts.isEmpty()) {
          logger.info("No valid consignment of having total price greater than zero today is found.");
        } else {
          for (ConsignmentMgmt consignmentMgmt : consignmentMgmts) {
            logger.info("inside loop for consignment id: " + consignmentMgmt.getId());
            if (consignmentRevenueDailyDb.getTotalAmountInDollar() == null)
              consignmentRevenueDailyDb.setTotalAmountInDollar(Double.valueOf(0.0D)); 
            if (consignmentRevenueDailyDb.getTotalAmountInRiel() == null)
              consignmentRevenueDailyDb.setTotalAmountInRiel(Double.valueOf(0.0D)); 
            if (consignmentRevenueDailyDb.getCountOfDevicesWhenPrice() == null)
              consignmentRevenueDailyDb.setCountOfDevicesWhenPrice(Integer.valueOf(0)); 
            if (consignmentRevenueDailyDb.getCountOfImeiWhenPrice() == null)
              consignmentRevenueDailyDb.setCountOfImeiWhenPrice(Integer.valueOf(0)); 
            if (consignmentMgmt.getDeviceQuantity() == null)
              consignmentMgmt.setDeviceQuantity(Integer.valueOf(0)); 
            if (consignmentMgmt.getCurrency().intValue() == 0 || consignmentMgmt.getCurrency() == null || this.exchangeRateMap.get(consignmentMgmt.getCurrency()) == null)
              continue; 
            consignmentRevenueDailyDb.setTotalAmountInDollar(
                Double.valueOf(consignmentRevenueDailyDb.getTotalAmountInDollar().doubleValue() + this.currencyConverter.exchangeRate((
                    (Currency)this.exchangeRateMap.get(consignmentMgmt.getCurrency())).getDollar(), 
                    consignmentMgmt.getTotalPrice().doubleValue())));
            logger.info("amount in dollar: " + consignmentRevenueDailyDb.getTotalAmountInDollar());
            consignmentRevenueDailyDb.setTotalAmountInRiel(
                Double.valueOf(consignmentRevenueDailyDb.getTotalAmountInRiel().doubleValue() + this.currencyConverter.exchangeRate((
                    (Currency)this.exchangeRateMap.get(consignmentMgmt.getCurrency())).getRiel(), 
                    consignmentMgmt.getTotalPrice().doubleValue())));
            logger.info("amount in riel: " + consignmentRevenueDailyDb.getTotalAmountInRiel());
            consignmentRevenueDailyDb.setCountOfDevicesWhenPrice(
                Integer.valueOf(consignmentRevenueDailyDb.getCountOfDevicesWhenPrice().intValue() + consignmentMgmt.getDeviceQuantity().intValue()));
            logger.info("count of device: " + consignmentRevenueDailyDb.getCountOfDevicesWhenPrice());
            consignmentRevenueDailyDb.setCountOfImeiWhenPrice(
                Integer.valueOf(consignmentRevenueDailyDb.getCountOfImeiWhenPrice().intValue() + consignmentMgmt.getQuantity()));
            logger.info("count of imei: " + consignmentRevenueDailyDb.getCountOfImeiWhenPrice());
          } 
          consignmentRevenueDailyDb.setCountOfConsignmentWhenPrice(dailyCounts.getCountOfConsignment());
          logger.info("Count of consignment when price: " + consignmentRevenueDailyDb.getCountOfConsignmentWhenPrice());
          consignmentRevenueDailyDb.setCountOfConsignment(countOfConsignment);
          logger.info("Total Count of consignment: " + consignmentRevenueDailyDb.getCountOfConsignment());
          consignmentRevenueDailyDb.setCountOfDevices(dailyCounts.getCountOfDevices());
          logger.info("Count of devices when price: " + consignmentRevenueDailyDb.getCountOfDevices());
          consignmentRevenueDailyDb.setCountOfImei(dailyCounts.getCountOfImei());
          logger.info("Count of imei when price: " + consignmentRevenueDailyDb.getCountOfImei());
          process(consignmentRevenueDailyDb);
        } 
      } 
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    } 
  }
  
  public void process(Object o) {
    ConsignmentRevenueDailyDb consignmentRevenueDailyDb = (ConsignmentRevenueDailyDb)o;
    if (Objects.isNull(consignmentRevenueDailyDb)) {
      logger.info("Skipping null consignmentRevenueDailyDb.");
    } else {
      logger.info("going to save entries in consignment_revenue_daily_db");
      this.consignmentRevenueDailyRepository.save(consignmentRevenueDailyDb);
    } 
  }
}