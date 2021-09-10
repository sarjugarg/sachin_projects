package com.ceir.SLAModule.model.constants;


public enum RequestType {

	Stolen(0, "Stolen"), Recovery(1, "Recovery"), Block(2, "Block"),Unblock(3, "Unblock");
	private Integer code;
	private String description;

	RequestType(Integer code, String description) {
		this.code = code;
		this.description = description; 
	}       

	public Integer getCode() {
		return code;
	}
            
	public String getDescription() {
		return description;
	}
	

	public static RequestType getUserStatusByCode(Integer code) {
		for (RequestType approveStatus : RequestType.values()) {
			if (approveStatus.getCode() == code)
				return approveStatus;
		}

		return null;
	}
	
	public static RequestType getUserStatusByDesc(String desc) {
		for (RequestType approveStatus : RequestType.values()) {
			if (approveStatus.getDescription().equals(desc))
				return approveStatus;
		}

		return null;
	}
}
