package com.gl.imei.freq.model;

import java.sql.Timestamp;

public class ImeiDupDb {

	public String imei;
	public Long msisdnCount;
	public Timestamp createdOn;
	public Timestamp modifiedOn;
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public Long getMsisdnCount() {
		return msisdnCount;
	}
	public void setMsisdnCount(Long msisdnCount) {
		this.msisdnCount = msisdnCount;
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
		return "ImeiDupDb [imei=" + imei + ", msisdnCount=" + msisdnCount + ", createdOn=" + createdOn + ", modifiedOn="
				+ modifiedOn + "]";
	}
	
	
}
