package com.graphics.common.service;

import com.graphics.common.web.ApiCode;

/**
 * Service层公用的Exception, 从由Spring管理事务的函数中抛出时会触发事务回滚.
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private ApiCode apiCode;//异常码

	public ServiceException() {
		super();
	}
	public ServiceException(ApiCode apiCode) {
		super(apiCode.getMsg());
		this.apiCode = apiCode;
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(ApiCode apiCode, Throwable cause) {
		super(apiCode.getMsg(), cause);
		this.apiCode = apiCode;
	}
	public ApiCode getApiCode() {
		return apiCode;
	}

	
}
