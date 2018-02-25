/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.graphics.modules.sys.web;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.common.utils.DynamicBean;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.persistence.Page;
import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.sys.entity.Dict;
import com.graphics.modules.sys.service.DictService;

/**
 * 字典Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@RestController
@RequestMapping("${adminPath}/system/dict")
public class DictController extends BaseController {

	@Autowired
	private DictService dictService;
	
	@RequestMapping(value = "/get")
	public ApiData<Dict> get(String id, HttpServletRequest request,
			HttpServletResponse response) {
		Dict dict = dictService.get(id);
		ApiData<Dict> apiData = apiReturn(1, ApiCode.SUCCESS, dict);
		return apiData;
	}
	
	@RequiresPermissions("permission:dict:view")
	@RequestMapping(value = "/search")
	public ApiData<Page<Dict>> search(
			@RequestParam(required = false) String remark,
			@RequestParam(required = false) String type,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "order", defaultValue = "sort asc") String order) {
		// 设置查询条件
		Page<Dict> page = new Page<Dict>(pageNo, limit);
		page.setOrderBy("type asc,"+order);
		Dict dict = new Dict();
		dict.setRemark(remark);
		dict.setType(type);
		// 查询
		page = dictService.search(page,dict);
		// 构造返回数据
		ApiData<Page<Dict>> apiData = apiReturn(1, ApiCode.SUCCESS, page);
		return apiData;
	}
	
	@RequiresPermissions("permission:dict:del")
	@RequestMapping(value = "/delete")
	public ApiData<Object> delete(String id) {
		dictService.delete(id);
		// 构造响应数据
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
	
	@RequiresPermissions("permission:dict:edit")
	@RequestMapping(value = "/update")
	public ApiData<Object> update(
			String id,
			String label,
			String value,
			String remark,
			String type,
			Integer sort){
		try {
			dictService.update(id,label,value,remark,type,sort);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
	
	@RequiresPermissions("permission:dict:add")
	@RequestMapping(value = "/create")
	public ApiData<Object> create(
			String label,
			String value,
			String remark,
			String type,
			Integer sort){
		try {
			dictService.create(label,value,remark,type,sort);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
	
	/**
	 * 根据类型获取字典列表
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/getDictList")
	public ApiData<List<Object>> getDictList(String type){
		if(StringUtils.isNotBlank(type)){
			List<Dict> list = dictService.getDictList(type);
			List<Object> dataList = new ArrayList<Object>();
			for(Dict d:list){
				Object bean = new DynamicBean.Builder()
						.setPV("label", d.getLabel())
						.setPV("value", d.getValue())
						.build().getObject();
				dataList.add(bean);
			}
			ApiData<List<Object>> apiData = apiReturn(1, ApiCode.SUCCESS, dataList);
			return apiData;
		}else{
			return null;
		}
		
	}
	
	/**
	 * 获取字典类型
	 * @return
	 */
	@RequestMapping(value="/getDictType")
	public ApiData<List<String>> getDictType(){
			List<String> list = dictService.findTypeList();
			ApiData<List<String>> apiData = apiReturn(1, ApiCode.SUCCESS, list);
			return apiData;
	}
}
