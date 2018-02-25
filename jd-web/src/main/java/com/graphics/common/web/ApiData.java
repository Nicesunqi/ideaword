package com.graphics.common.web;
/**
 * controller响应数据
 */
public class ApiData<T> {
	public final static int SUCCESS = 1;
	public final static int FAIL = 0;
	private int status;//1成功 或者 0失败
	private String code;//响应码
	private String message;//响应描述
	private T data;//数据
	
	public ApiData(int status, String code, String message) {
		this(status,code,message,null);
	}
	
	public ApiData(int status, String code, String message, T data) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
		this.data = data;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
