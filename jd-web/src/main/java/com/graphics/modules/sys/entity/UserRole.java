package com.graphics.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.graphics.common.persistence.IdEntity;

/**
 * 用户角色
 */
@Entity
@Table(name = "jd_sys_user_role")
public class UserRole extends IdEntity<UserRole> {

	private static final long serialVersionUID = 1L;

	private String userId;
	private User user;
	
	private String roleId;
	private Role role;
	
	
	public UserRole() {

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Transient
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	

	
}
