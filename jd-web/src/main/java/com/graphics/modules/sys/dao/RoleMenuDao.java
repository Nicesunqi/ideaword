package com.graphics.modules.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Parameter;
import com.graphics.modules.sys.entity.Menu;
import com.graphics.modules.sys.entity.RoleMenu;

/**
 * 角色菜单DAO接口
 */
@Repository
public class RoleMenuDao extends BaseDao<RoleMenu> {
	public List<RoleMenu> findAllByRoleId(String roleId) {
		return find("from RoleMenu rm where delFlag = :p1 and roleId=:p2", new Parameter(RoleMenu.DEL_FLAG_NORMAL,roleId));
	}
	
	public List<Menu> findMenusByRoleId(String roleId) {
		return find("from Menu m where m.delFlag=:p1 and m.id in (select rm.menuId from RoleMenu rm where rm.delFlag = :p2 and rm.roleId=:p3)", new Parameter(RoleMenu.DEL_FLAG_NORMAL,RoleMenu.DEL_FLAG_NORMAL,roleId));
	}
	
	public void deleteByRoleId(String roleId){
		update("update RoleMenu set delFlag=:p1 where roleId =:p2 ", new Parameter(RoleMenu.DEL_FLAG_DELETE,roleId));
	}
	
}
