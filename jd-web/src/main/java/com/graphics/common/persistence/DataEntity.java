
package com.graphics.common.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.graphics.modules.sys.entity.User;
import com.graphics.modules.sys.utils.UserUtils;

/**
 * 数据Entity类
 */
@MappedSuperclass
public abstract class DataEntity<T> extends BaseEntity<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	protected String remark;	// 备注
	protected String createBy;	// 创建者
	protected Long timestamp;
	protected Date createDate;// 创建日期
	protected String updateBy;	// 更新者
	protected Date updateDate;// 更新日期
	protected Integer delFlag; // 删除标记（0：正常；1：删除；）
	
	public DataEntity() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}
	
	@PrePersist
	public void prePersist(){
		User user = UserUtils.getUser();
		if (user!=null&&StringUtils.isNotBlank(user.getId())){
			this.updateBy = user.getId();
			this.createBy = user.getId();
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
		this.timestamp = this.createDate.getTime();
	}
	
	@PreUpdate
	public void preUpdate(){
		User user = UserUtils.getUser();
		if (user!=null&&StringUtils.isNotBlank(user.getId())){
			this.updateBy = user.getId();
		}
		this.updateDate = new Date();
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@JsonIgnore
//	@Length(min=1, max=1)
	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
