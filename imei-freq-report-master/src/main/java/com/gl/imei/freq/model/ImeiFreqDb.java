package com.gl.imei.freq.model;

import java.sql.Timestamp;

public class ImeiFreqDb {

	public Long msisdnFreq;
	public Long imeiCount;
	public Timestamp createdOn;
	public Timestamp modifiedOn;
	public Long getMsisdnFreq() {
		return msisdnFreq;
	}
	public void setMsisdnFreq(Long msisdnFreq) {
		this.msisdnFreq = msisdnFreq;
	}
	public Long getImeiCount() {
		return imeiCount;
	}
	public void setImeiCount(Long imeiCount) {
		this.imeiCount = imeiCount;
	}
	public Timestamp getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}
	public Timestamp getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	@Override
	public String toString() {
		return "ImeiFreqDb [msisdnFreq=" + msisdnFreq + ", imeiCount=" + imeiCount + ", createdOn=" + createdOn
				+ ", modifiedOn=" + modifiedOn + "]";
	}
	
}
