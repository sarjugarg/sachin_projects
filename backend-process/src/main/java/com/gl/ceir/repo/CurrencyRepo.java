package com.gl.ceir.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gl.ceir.entity.Currency;

public interface CurrencyRepo extends JpaRepository<Currency, Long>,JpaSpecificationExecutor<Currency>{

	public Currency save(Currency currency);
	public Currency findById(long id);
    public Currency findTopByMonthDateOrderByIdDesc(Date  date);
    boolean existsByMonthDateAndCurrency(String date,Integer currency);   
    
}
