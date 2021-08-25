package com.ceir.GreyListProcess.model;

public class FileDumpFilter {
	
	public String startDate;
	public String endDate;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "FileDumpFilter [startDate=" + startDate + ", endDate=" + endDate + "]";
	}
}
