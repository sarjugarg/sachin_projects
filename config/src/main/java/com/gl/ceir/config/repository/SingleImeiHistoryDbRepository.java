package com.gl.ceir.config.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gl.ceir.config.model.SingleImeiHistoryDb;

public interface SingleImeiHistoryDbRepository extends JpaRepository<SingleImeiHistoryDb, Long> {

}
