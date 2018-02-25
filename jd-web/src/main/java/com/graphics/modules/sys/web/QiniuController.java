package com.graphics.modules.sys.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.sys.utils.Uptoken;

@RestController
@RequestMapping("${adminPath}/system/qiniu")
public class QiniuController extends BaseController {
	/**
	 * 公共空间Token
	 * @return
	 */
	@RequestMapping(value="/upToken")
	public ApiData<String> upToken(){
		String upToken = Uptoken.makeUptoken();
		ApiData<String> apiData = apiReturn(1, ApiCode.SUCCESS, upToken);
		return apiData;
	}
	
	/**
	 * 私有空间Token
	 * @return
	 */
	@RequestMapping(value="/upPrivateToken")
	public ApiData<String> upPrivateToken(){
		String upToken = Uptoken.makePrivateUptoken();
		ApiData<String> apiData = apiReturn(1, ApiCode.SUCCESS, upToken);
		return apiData;
	}
	
	/**
	 * 返回时效加密链接Data
	 * @param url
	 * @return
	 */
	@RequestMapping(value="/privateUrl")
	public ApiData<String> privateUrl(String url){
		String upToken = Uptoken.privateDownloadUrl(url);
		ApiData<String> apiData = apiReturn(1, ApiCode.SUCCESS, upToken);
		return apiData;
	}
}