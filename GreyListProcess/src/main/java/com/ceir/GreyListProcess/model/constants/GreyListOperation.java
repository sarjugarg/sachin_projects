package com.ceir.GreyListProcess.model.constants;

public enum GreyListOperation {

ADD(0,"Add"),DELETE(1,"Delete"),UPDATE(2,"Update");
	
	private int code;
	private String description;
	
	GreyListOperation(Integer code, String description) {
		this.code = code;
		this.description = description; 
	}       

	public Integer getCode() {
		return code;
	}
            
	public String getDescription() {
		return description;
	}
	

	public static GreyListOperation getUserStatusByCode(Integer code) {
		for (GreyListOperation approveStatus : GreyListOperation.values()) {
			if (approveStatus.getCode() == code)
				return approveStatus;
		}

		return null;
	}
	
	public static GreyListOperation getUserStatusByDesc(String desc) {
		for (GreyListOperation approveStatus : GreyListOperation.values()) {
			if (approveStatus.getDescription().equals(desc))
				return approveStatus;
		}

		return null;
	}
}
