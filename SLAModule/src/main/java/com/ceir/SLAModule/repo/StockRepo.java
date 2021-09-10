package com.ceir.SLAModule.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ceir.SLAModule.entity.StockMgmt;

public interface StockRepo extends  JpaRepository<StockMgmt, Long>{

	public List<StockMgmt> findByStockStatus(int status);
	
}
