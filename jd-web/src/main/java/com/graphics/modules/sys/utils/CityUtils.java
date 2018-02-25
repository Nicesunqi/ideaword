package com.graphics.modules.sys.utils;

import java.util.List;


import com.graphics.common.utils.StringUtils;
import com.graphics.common.service.BaseService;
import com.graphics.common.utils.SpringContextHolder;
import com.graphics.modules.sys.entity.City;
import com.graphics.modules.sys.service.CityService;

/**
 * 城市工具类
 */
public class CityUtils extends BaseService {
	
	private static CityService cityService = SpringContextHolder.getBean(CityService.class);
	
	/**
	 * id
	 */
	public static City getcity(String id){
		if(StringUtils.isNotBlank(id)){
			return cityService.get(id);
		}else{
			return null;
		}
		
	}
	/**
	 * 获取中国所有省份
	 */
	public static List<City> getProvinces() {
		List<City> lists= cityService.findByPcode("0");
		return lists;
	}
	/**
	 * 根据当前城市获取子城市列表 
	 * @param code
	 * @return
	 */
	public static List<City> getSubCitis(City city){
		if(city!=null&&city.getCode()!=null&&city.getCode().length()>0){
			return cityService.findChildren(city.getCode());
		}else{
			return null;
		}
		
	}
	
	public static City getCityByCode(String code){
		if(StringUtils.isNotBlank(code)){
			return cityService.getCityByCode(code);
		}else{
			return null;
		}
	}
}
