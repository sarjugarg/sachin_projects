package com.gl.ceir.repo;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.gl.ceir.entity.CurrencyConversionDb;

@Repository
public interface CurrencyConversionRepository extends JpaRepository<CurrencyConversionDb, Long>, 
JpaSpecificationExecutor<CurrencyConversionDb>{
	
	
}
