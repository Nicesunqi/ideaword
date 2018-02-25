package com.graphics.modules.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.graphics.common.service.BaseService;
import com.graphics.modules.sys.dao.CityDao;
import com.graphics.modules.sys.entity.City;

/**
 * 城市Service
 */
@Service
@Transactional(readOnly = true)
public class CityService extends BaseService {

	@Autowired
	private CityDao cityDao;
	
	public City get(String id) {
		return cityDao.get(id);
	}
	
	public List<City> findAll(){
		return cityDao.findAll();
	}
	
	/**
	 * 返回树形结构的city，最上面的节点为pcode=0的节点
	 * @return
	 */
	public List<City> allListTree(){
		List<City> cityList = Lists.newArrayList();
		List<City> all = cityDao.findAllList();
		Map<String, City> cityMap = new HashMap<String, City>();
		for(City city:all){
			cityMap.put(city.getCode(), city);
		}
		for(City city:all){
			if("0".equals(city.getPcode())){
				cityList.add(city);
			}else{
				City parent = cityMap.get(city.getPcode());
				if(parent!=null){
					parent.addChild(city);
				}
			}
		}
		return cityList;
	}
	
	
	@Transactional(readOnly = false)
	public void save(City city) {
		cityDao.save(city);
	}
	


	public List<City> findByPcode(String pcode) {
		// TODO Auto-generated method stub
		return cityDao.findByPcode(pcode);
	}

	public List<City> findChildren(String code) {
		// TODO Auto-generated method stub
		return cityDao.findChildren(code);
	}

	public City getCityByCode(String code) {
		// TODO Auto-generated method stub
		return cityDao.getCityByCode(code);
	}
	
}
