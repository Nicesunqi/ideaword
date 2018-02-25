package com.graphics.modules.sys.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.graphics.common.persistence.IdEntity;

/**
 * 菜单
 */
@Entity
@Table(name = "jd_sys_menu")
public class Menu extends IdEntity<Menu> {

	private static final long serialVersionUID = 1L;

	private String pid;// 上级id
	private Menu parent; // 父节点
	private String name; // 部门名称
	private String code; // 部门编码0101 在部门上下级调整后 这个值会变
	private Integer level;// 级别
	private Integer sort = 999;// 默认999
	private String icon;
	private Integer isShow = 1;//0 不显示   1显示
	private String permission;//权限标识
	
	private List<Menu> children = Lists.newArrayList();// 拥有子机构列表

	public Menu() {

	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Transient
	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
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
	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}
	
	public void addChild(Menu office){
		if(children==null){
			children = Lists.newArrayList();
		}
		children.add(office);
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}
