package com.graphics.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Parameter;
import com.graphics.modules.sys.entity.Role;
import com.graphics.modules.sys.entity.UserRole;

/**
 * 用户角色DAO接口
 */
@Repository
public class UserRoleDao extends BaseDao<UserRole> {
	public List<UserRole> findAllByUserId(String userId) {
		return find("from UserRole where delFlag = :p1 and userId=:p2", new Parameter(UserRole.DEL_FLAG_NORMAL,userId));
	}
	
	public List<Role> findRolesByUserId(String userId) {
		return find("from Role r where r.delFlag=:p1 and r.id in (select ur.roleId from UserRole ur where ur.delFlag = :p2 and ur.userId=:p3)", new Parameter(UserRole.DEL_FLAG_NORMAL,UserRole.DEL_FLAG_NORMAL,userId));
	}
	
	public void deleteByUserId(String userId){
		update("update UserRole set delFlag=:p1 where userId =:p2 ", new Parameter(UserRole.DEL_FLAG_DELETE,userId));
	}
	
}
