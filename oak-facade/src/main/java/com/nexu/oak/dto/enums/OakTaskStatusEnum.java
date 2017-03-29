package com.nexu.oak.dto.enums;

public enum OakTaskStatusEnum {
	
	INIT("INIT"),
	PROCESS("PROCESS"),
	SUCCESS("SUCCESS"),
	FAIL("FAIL");
	
	private String value;
	
	private OakTaskStatusEnum(String value){
		this.value=value;
	}
	
	
	public String getValue() {
		return value;
	}
	
}
