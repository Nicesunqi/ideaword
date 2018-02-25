package com.graphics.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Parameter;
import com.graphics.modules.sys.entity.Office;

/**
 * 系统部门DAO接口
 */
@Repository
public class OfficeDao extends BaseDao<Office> {
	
	public List<Office> findAll(String order){
		return find("from Office where delFlag = :p1 order by :p2", new Parameter(Office.DEL_FLAG_NORMAL,order));
	}
	/**
	 * 查找子节点 包括子子节点
	 * @param code
	 * @return
	 */
	public List<Office> findChildren(String code){
		return find("from Office where code like ':p1_%' and delFlag = :p2", new Parameter(code, Office.DEL_FLAG_NORMAL));
	}
	
	public Office findByPid(String pid){
		return getByHql("from Office where pid = :p1 and delFlag = :p2", new Parameter(pid, Office.DEL_FLAG_NORMAL));
	}
	
	/**
	 * 查找指定code的最大子节点
	 * @param code
	 * @param containDel 是否包含被删除的部门  true:包含
	 * @return
	 */
	public Office findMaxChildByCode(String code,boolean containDel){
		Office office = null;
		if(containDel){
			office = getByHql("from Office where code = select max(code) from Office where code like ':p1__' ", new Parameter(code));
		}else{
			office = getByHql("from Office where code = select max(code) from Office where code like ':p1__' and delFlag = :p2", new Parameter(code,Office.DEL_FLAG_NORMAL));
		}
		return office;
	}
	
	/**
	 * 删除指定code，并且删除所有子节点
	 * @param code
	 */
	public void delAllByCode(String code){
		update("update Office set delFlag=:p1 where code like ':p2%' ", new Parameter(Office.DEL_FLAG_DELETE,code));
	}
}
