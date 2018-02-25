package com.graphics.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.sys.entity.City;
import com.graphics.modules.sys.service.CityService;

/**
 * 城市Controller
 */
@RestController
@RequestMapping("${adminPath}/system/city")
public class CityController extends BaseController {
	@Autowired
	private CityService cityService;

	@RequiresPermissions("permission:city:view")
	@RequestMapping(value = "/get")
	public ApiData<City> get(String id, HttpServletRequest request,
			HttpServletResponse response) {
		City city = cityService.get(id);
		ApiData<City> apiData = apiReturn(1, ApiCode.SUCCESS, city);
		return apiData;
	}

	/**
	 * 获取所有城市
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("permission:city:view")
	@RequestMapping(value = "/all")
	public ApiData<List<City>> all(HttpServletRequest request,
			HttpServletResponse response) {
		List<City> cityList = cityService.allListTree();
		ApiData<List<City>> apiData = apiReturn(1, ApiCode.SUCCESS,
				cityList);
		return apiData;
	}

	
}
