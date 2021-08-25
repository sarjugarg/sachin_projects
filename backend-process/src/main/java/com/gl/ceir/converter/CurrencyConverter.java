package com.gl.ceir.converter;

import org.springframework.stereotype.Component;

@Component
public class CurrencyConverter {
	
	public double exchangeRate(double outCurrency, double amount) {
		return outCurrency * amount;
	}

}
