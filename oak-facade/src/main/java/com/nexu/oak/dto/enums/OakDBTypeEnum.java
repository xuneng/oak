package com.nexu.oak.dto.enums;

public enum OakDBTypeEnum {
	MYSQL("MYSQL");

	private String value;
	private OakDBTypeEnum(String value){
		this.value=value;
	}
	public String getValue() {
		return value;
	}

}
