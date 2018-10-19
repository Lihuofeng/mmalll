package com.bees360.common;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

//保证序列化json的时候，如果是null的对象，key会消失
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
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

	//	使之不在json序列化在结果当中
	@JsonIgnore
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

	public static <T> ServiceResponse<T> createBySuccess(String msg,T data){
		return new ServiceResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
	}

	public static <T> ServiceResponse<T> createByError(){
		return new ServiceResponse<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
	}

	public static <T> ServiceResponse<T> createByErrorMessage(String errorMessage){
		return new ServiceResponse<>(ResponseCode.ERROR.getCode(), errorMessage);
	}

	public static <T> ServiceResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
		return new ServiceResponse<>(errorCode, errorMessage);
	}
}
