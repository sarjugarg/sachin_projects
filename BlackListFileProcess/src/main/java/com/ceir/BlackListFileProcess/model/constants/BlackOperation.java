package com.ceir.BlackListFileProcess.model.constants;

public enum BlackOperation {

	Add(0,"Add"),DELETE(1,"Delete"),UPDATE(2,"Update");
	
	private int code;
	private String description;
	
	BlackOperation(Integer code, String description) {
		this.code = code;
		this.description = description; 
	}       

	public Integer getCode() {
		return code;
	}
            
	public String getDescription() {
		return description;
	}
	

	public static BlackOperation getUserStatusByCode(Integer code) {
		for (BlackOperation approveStatus : BlackOperation.values()) {
			if (approveStatus.getCode() == code)
				return approveStatus;
		}

		return null;
	}
	
	public static BlackOperation getUserStatusByDesc(String desc) {
		for (BlackOperation approveStatus : BlackOperation.values()) {
			if (approveStatus.getDescription().equals(desc))
				return approveStatus;
		}

		return null;
	}
}
