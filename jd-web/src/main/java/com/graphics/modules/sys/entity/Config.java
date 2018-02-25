package com.graphics.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.graphics.common.persistence.IdEntity;

/**
 * 系统配置Entity
 * @author hg
 *
 */
@Entity
@Table(name = "jd_sys_config")
public class Config extends IdEntity<Config> {
	
	private static final long serialVersionUID = 1L;
	
	private String key;			//参数Key
	private String name;		//参数名称
	private String type;		//参数类型（1：课时   ....其他待添加）
	private String value;		//运行参数
	private String def;			//参数默认值
	private String typeLabel;		//参数类型Label（1：课时   ....其他待添加）
	@Column(name= "\"key\"")  
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDef() {
		return def;
	}
	public void setDef(String def) {
		this.def = def;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Transient
	public String getTypeLabel() {
		return typeLabel;
	}
	public void setTypeLabel(String typeLabel) {
		this.typeLabel = typeLabel;
	}
	
}
