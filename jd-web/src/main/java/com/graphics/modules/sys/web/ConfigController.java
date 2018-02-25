package com.graphics.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.common.persistence.Page;
import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.sys.entity.Config;
import com.graphics.modules.sys.service.ConfigService;

/**
 * 系统配置Controller
 * @author hg
 *
 */
@RestController
@RequestMapping("${adminPath}/system/config")
public class ConfigController extends BaseController {
	@Autowired
	private ConfigService configService;
	
	@RequiresPermissions("permission:config:view")
	@RequestMapping(value = "/get")
	public ApiData<Config> get(String id, HttpServletRequest request,
			HttpServletResponse response) {
		Config config = configService.get(id);
		ApiData<Config> apiData = apiReturn(1, ApiCode.SUCCESS, config);
		return apiData;
	}
	
	/**
	 * 
	 * @param pageNo
	 * @param limit
	 * @param order
	 * @return
	 */
	@RequiresPermissions("permission:config:view")
	@RequestMapping(value = "/search")
	public ApiData<Page<Config>> search(
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "order", defaultValue = "timestamp desc") String order) {
		// 设置查询条件
		Page<Config> page = new Page<Config>(pageNo, limit);
		page.setOrderBy(order);
		Config config = new Config();
		// 查询
		page = configService.find(page, config);
		
		// 构造返回数据
		ApiData<Page<Config>> apiData = apiReturn(1, ApiCode.SUCCESS, page);

		return apiData;
	}
	
	@RequiresPermissions("permission:config:del")
	@RequestMapping(value = "/delete")
	public ApiData<Object> delete(String id) {
		configService.delete(id);
		// 构造响应数据
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
	
	/**
	 * 
	 * @param id
	 * @param type
	 * @param key
	 * @param name
	 * @param def
	 * @param value
	 * @param remark
	 * @return
	 */
	@RequiresPermissions("permission:config:edit")
	@RequestMapping(value = "/update")
	public ApiData<Object> update(String id,String type,String key,String name,String def,String value,String remark){
		try {
			configService.update(id,type,StringEscapeUtils.unescapeHtml4(key),
					StringEscapeUtils.unescapeHtml4(name),StringEscapeUtils.unescapeHtml4(def),
					StringEscapeUtils.unescapeHtml4(value),StringEscapeUtils.unescapeHtml4(remark));
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
	
	/**
	 * 
	 * @param type
	 * @param key
	 * @param name
	 * @param def
	 * @param value
	 * @param remark
	 * @return
	 */
	@RequiresPermissions("permission:config:add")
	@RequestMapping(value = "/create")
	public ApiData<Object> create(
			@RequestParam(required = false) String type,
			@RequestParam(required = false) String key,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String def,
			@RequestParam(required = false) String value,
			@RequestParam(required = false) String remark){
		try {
			configService.create(type,StringEscapeUtils.unescapeHtml4(key),
					StringEscapeUtils.unescapeHtml4(name),StringEscapeUtils.unescapeHtml4(def),
					StringEscapeUtils.unescapeHtml4(value),StringEscapeUtils.unescapeHtml4(remark));
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
	
	@RequestMapping(value = "/keyList")
	public ApiData<List<String>> keyList(){
		List<String> keyList = configService.keyList();
		ApiData<List<String>> apiData = apiReturn(1, ApiCode.SUCCESS, keyList);
		return apiData;
	}
}
