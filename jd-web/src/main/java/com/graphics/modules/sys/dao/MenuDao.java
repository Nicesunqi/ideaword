package com.graphics.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Parameter;
import com.graphics.modules.sys.entity.Menu;

/**
 * 菜单DAO接口
 */
@Repository
public class MenuDao extends BaseDao<Menu> {
	
	public List<Menu> findAll(String order){
		return find("from Menu where delFlag = :p1 order by :p2", new Parameter(Menu.DEL_FLAG_NORMAL,order));
	}
	/**
	 * 查找子节点 包括子子节点
	 * @param code
	 * @return
	 */
	public List<Menu> findChildren(String code){
		return find("from Menu where  code like ':p1_%' and delFlag = :p2", new Parameter(code, Menu.DEL_FLAG_NORMAL));
	}
	
	public Menu findByPid(String pid){
		return getByHql("from Menu where pid = :p1 and delFlag = :p2", new Parameter(pid, Menu.DEL_FLAG_NORMAL));
	}
	
	/**
	 * 查找指定code的最大子节点
	 * @param code
	 * @param containDel 是否包含被删除的部门  true:包含
	 * @return
	 */
	public Menu findMaxChildByCode(String code,boolean containDel){
		Menu menu = null;
		if(containDel){
			menu = getByHql("from Menu where code = select max(code) from Menu where code like ':p1__' ", new Parameter(code));
		}else{
			menu = getByHql("from Menu where code = select max(code) from Menu where code like ':p1__' and delFlag = :p2", new Parameter(code,Menu.DEL_FLAG_NORMAL));
		}
		return menu;
	}
	/**
	 * 查询给定menu id的最大code
	 * @param code
	 * @param id menu id
	 * @return
	 */
	public String findMaxCodeById(String id){
		String maxCode = (String)findByHql("select max(code) from Menu where pid=:p1 ", new Parameter(id));
		return maxCode;
	}
	
	/**
	 * 删除指定code，并且删除所有子节点
	 * @param code
	 */
	public void delAllByCode(String code){
		update("update Menu set delFlag=:p1 where code like :p2 ", new Parameter(Menu.DEL_FLAG_DELETE,code+"%"));
	}
}
