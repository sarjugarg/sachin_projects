package com.ceir.SLAModule.repoService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ceir.SLAModule.App;
import com.ceir.SLAModule.entity.StockMgmt;
import com.ceir.SLAModule.repo.StockRepo;

@Service
public class StockRepoService {

	private final static Logger log =Logger.getLogger(App.class);

	@Autowired
	StockRepo stockRepo;

	public List<StockMgmt> stockDataByStatus(int status){

		try {
			log.info("goin to fetch stock data by status");
			return stockRepo.findByStockStatus(status);
		}
		catch(Exception e) {
			log.info("error occurs when fetch stock data by status");
			log.info(e.toString());
			return new ArrayList<StockMgmt>();
		}
	}
}
