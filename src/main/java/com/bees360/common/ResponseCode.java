package com.bees360.common;

public enum ResponseCode {
	SUCCESS(0,"SUCCESS"),
	ERROR(1,"ERROR"),
	NEED_LOGIN(10,"NEEDLOGIN"),
	ILLEGAL_ARGUMENT(2,"ILLEGAL_ARGUMENT");
	
	private final int code;
	private final String desc;
	
	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	private ResponseCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	
}
