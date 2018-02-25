package com.graphics.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Parameter;
import com.graphics.modules.sys.entity.City;

/**
 * 城市DAO接口
 */
@Repository
public class CityDao extends BaseDao<City> {
	
	public List<City> findAllList(){
		return find("from City where delFlag = :p1 order by code", new Parameter(City.DEL_FLAG_NORMAL));
	}
	/**
	 * 查找子节点 
	 * @param pcode
	 * @return
	 */
	public List<City> findChildren(String pcode){
		return find("from City where  pcode = :p1 and delFlag = :p2", new Parameter(pcode, City.DEL_FLAG_NORMAL));
	}	
	
	
	
	/**
	 * 删除指定code，并且删除所有子节点
	 * @param code
	 */
	public void delAllByCode(String code){
		update("update City set delFlag='1' where code = :p1 or pcode = p2", new Parameter(code,code));
	}
	/**
	 * 
	 * @param pcode
	 * @return
	 */
	public List<City> findByPcode(String pcode) {
		return find("from City where delFlag = :p1 and pcode = :p2 order by code", new Parameter(City.DEL_FLAG_NORMAL,pcode));
	}
	public City getCityByCode(String code) {
		return getByHql("from City where code = :p1 and delFlag = :p2", new Parameter(code, City.DEL_FLAG_NORMAL));
	}
}
