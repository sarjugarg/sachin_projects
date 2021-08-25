package com.gl.ceir.pojo;

import java.util.HashMap;
import java.util.Map;

public class UserWiseMailCount {

	private Long userId;
	private int deviceCount;
	private String txnId;
	private String phoneNo;
	private String firstImei;
	private String secondImei;
	private String thirdImei;
	private String fourthImei;
	Map<String, String> placeholderMap;
	
	public UserWiseMailCount() {
		this.placeholderMap = new HashMap<>();
	}

	public String getFirstImei() {
		return firstImei;
	}

	public void setFirstImei(String firstImei) {
		this.firstImei = firstImei;
	}

	public String getSecondImei() {
		return secondImei;
	}

	public void setSecondImei(String secondImei) {
		this.secondImei = secondImei;
	}

	public String getThirdImei() {
		return thirdImei;
	}

	public void setThirdImei(String thirdImei) {
		this.thirdImei = thirdImei;
	}

	public String getFourthImei() {
		return fourthImei;
	}

	public void setFourthImei(String fourthImei) {
		this.fourthImei = fourthImei;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public int getDeviceCount() {
		return deviceCount;
	}

	public void setDeviceCount(int deviceCount) {
		this.deviceCount = deviceCount;
	}

	public String getTxnId() {
		return txnId;
	}

	public void setTxnId(String txnId) {
		this.txnId = txnId;
	}

	public Map<String, String> getPlaceholderMap() {
		return placeholderMap;
	}

	public void setPlaceholderMap(Map<String, String> placeholderMap) {
		this.placeholderMap = placeholderMap;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserWiseMailCount [userId=");
		builder.append(userId);
		builder.append(", deviceCount=");
		builder.append(deviceCount);
		builder.append(", txnId=");
		builder.append(txnId);
		builder.append(", firstImei=");
		builder.append(firstImei);
		builder.append(", secondImei=");
		builder.append(secondImei);
		builder.append(", thirdImei=");
		builder.append(thirdImei);
		builder.append(", fourthImei=");
		builder.append(fourthImei);
		builder.append(", placeholderMap=");
		builder.append(placeholderMap);
		builder.append("]");
		return builder.toString();
	}

}
