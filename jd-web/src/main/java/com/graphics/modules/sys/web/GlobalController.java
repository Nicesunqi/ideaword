package com.graphics.modules.sys.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.common.config.Global;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;

/**
 * 获取全局配置
 */
@RestController
@RequestMapping("${adminPath}/system/global")
public class GlobalController extends BaseController{
	@RequestMapping(value="/getQiniuDomainName")
	public ApiData<String> getQiniuDomainName(){
		ApiData<String> apiData = apiReturn(1, ApiCode.SUCCESS, Global.getQiniuDomainName());
		return apiData;
	}
	
	@RequestMapping(value="/getQiniuPrivateDomainName")
	public ApiData<String> getQiniuPrivateDomainName(){
		ApiData<String> apiData = apiReturn(1, ApiCode.SUCCESS, Global.getQiniuPrivateDomainName());
		return apiData;
	}
}
