package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.ceir.config.model.StockMgmtHistoryDb;

public interface StockMgmtHistoryRepository extends JpaRepository<StockMgmtHistoryDb, Long> {

}
