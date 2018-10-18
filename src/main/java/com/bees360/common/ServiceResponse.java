package com.bees360.common;

import java.io.Serializable;

public class ServiceResponse<T> implements Serializable{
	private int status;
	private String msg;
	private T data;
	
	private ServiceResponse(int status) {
		this.status=status;
	}
	
	private ServiceResponse(int status,String msg) {
		this.status=status;
		this.msg=msg;
	}
	
	private ServiceResponse(int status,T data) {
		this.status=status;
		this.data = data;
	}
	
	private ServiceResponse(int status, String msg, T data) {
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	public boolean isSuccess() {
		return this.status==ResponseCode.SUCCESS.getCode();
	}

	public int getStatus() {
		return status;
	}


	public String getMsg() {
		return msg;
	}


	public T getData() {
		return data;
	}

	public static <T> ServiceResponse<T> createBySuccess(){
		return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode());
	}
	
	public static <T> ServiceResponse<T> createBySuccessMessage(String msg){
		return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
	}
	
	public static <T> ServiceResponse<T> createBySuccess(T data){
		return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(),data);
	}
}
