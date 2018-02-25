package com.graphics.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.graphics.common.persistence.IdEntity;

/**
 * 角色
 */
@Entity
@Table(name = "jd_sys_role")
public class Role extends IdEntity<Role> {

	private static final long serialVersionUID = 1L;

	private String pid;// 上级id
	private Role parent; // 父节点
	private String name; // 角色名称
	private Integer status;//默认为0
	
	public  Role() {
		status = 0;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Transient
	public Role getParent() {
		return parent;
	}

	public void setParent(Role parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
