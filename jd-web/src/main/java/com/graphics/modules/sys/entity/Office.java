package com.graphics.modules.sys.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.graphics.common.persistence.IdEntity;

/**
 * 部门
 */
@Entity
@Table(name = "jd_sys_office")
public class Office extends IdEntity<Office> {

	private static final long serialVersionUID = 1L;

	private String pid;// 上级id
	private Office parent; // 父节点
	private String name; // 部门名称
	private String code; // 部门编码0101 在部门上下级调整后 这个值会变
	private Integer level;// 级别
	private Integer sort = 1000;// 默认1000
	
	private List<Office> children = Lists.newArrayList();// 拥有子机构列表

	public Office() {

	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Transient
	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Transient
	public List<Office> getChildren() {
		return children;
	}

	public void setChildren(List<Office> children) {
		this.children = children;
	}
	
	public void addChild(Office office){
		if(children==null){
			children = Lists.newArrayList();
		}
		children.add(office);
	}

}
