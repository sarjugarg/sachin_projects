package com.ceir.BlackListFileProcess.model.constants;


public enum DumpType {
	
	FULL(0,"Full"),INCREMENTAL(1,"Incremental");
	
	private int code;
	private String description;
	
	DumpType(Integer code, String description) {
		this.code = code;
		this.description = description; 
	}       

	public Integer getCode() {
		return code;
	}
            
	public String getDescription() {
		return description;
	}
	

	public static DumpType getUserStatusByCode(Integer code) {
		for (DumpType approveStatus : DumpType.values()) {
			if (approveStatus.getCode() == code)
				return approveStatus;
		}

		return null;
	}
	
	public static DumpType getUserStatusByDesc(String desc) {
		for (DumpType approveStatus : DumpType.values()) {
			if (approveStatus.getDescription().equals(desc))
				return approveStatus;
		}

		return null;
	}
}
