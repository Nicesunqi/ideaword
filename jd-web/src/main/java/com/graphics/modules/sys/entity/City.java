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
@Table(name = "jd_city")
public class City extends IdEntity<City>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code ;//城市编码
	
	private String name;//城市名称
	
	private String pinyin;//汉语拼音
	
	private String pcode;//上级城市编码
	
	private List<City> children = Lists.newArrayList();// 拥有的下级城市列表

	
	public City() {
		super();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	@Transient
	public List<City> getChildren() {
		return children;
	}

	public void setChildren(List<City> children) {
		this.children = children;
	}
	
	public void addChild(City city){
		if(children==null){
			children = Lists.newArrayList();
		}
		children.add(city);
	}
	
	

}
