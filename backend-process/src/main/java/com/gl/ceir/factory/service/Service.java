package com.gl.ceir.factory.service;

import java.util.Map;

public interface Service {
	
	public void fetchAndProcess();
	
	public void fetch();
	
	public void process(Object o);
	
	public void onErrorRaiseAnAlert(String alertId, Map<String, String> bodyPlaceHolderMap);
	
}
