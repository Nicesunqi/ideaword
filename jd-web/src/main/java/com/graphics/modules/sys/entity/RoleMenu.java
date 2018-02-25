package com.graphics.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.graphics.common.persistence.IdEntity;

/**
 * 角色菜单
 */
@Entity
@Table(name = "jd_sys_role_menu")
public class RoleMenu extends IdEntity<RoleMenu> {

	private static final long serialVersionUID = 1L;

	private String roleId;
	private Role role;
	
	private String menuId;
	private Menu menu;
	
	public RoleMenu() {

	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Transient
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	@Transient
	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	
}
