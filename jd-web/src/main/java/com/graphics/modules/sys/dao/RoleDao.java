package com.graphics.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Parameter;
import com.graphics.modules.sys.entity.Role;

/**
 * 系统角色DAO接口
 */
@Repository
public class RoleDao extends BaseDao<Role> {
	
	public List<Role> findAll(String order){
		return find("from Role where delFlag = :p1 order by :p2", new Parameter(Role.DEL_FLAG_NORMAL,order));
	}
	
	public Role findByPid(String pid){
		return getByHql("from Role where pid = :p1 and delFlag = :p2", new Parameter(pid, Role.DEL_FLAG_NORMAL));
	}
	
}
