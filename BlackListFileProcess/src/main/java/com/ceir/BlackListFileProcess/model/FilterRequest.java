package com.ceir.BlackListFileProcess.model;

import java.util.Date;

public class FilterRequest {
	public Date startDate;
	public Date endDate;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Override
	public String toString() {
		return "FilterRequest [startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
