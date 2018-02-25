package com.graphics.modules.sys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.graphics.common.persistence.IdEntity;

/**
 * 系统用户
 */
@Entity
@Table(name = "jd_sys_user")
public class User extends IdEntity<User> {

	private static final long serialVersionUID = 1L;

	private String account;// 帐号
	private String nickname;// 昵称
	private String mobile;// 帐号手机号码
	private String password;// 密码
	private String token;
	private Date lastLoginTime;// 最后登录时间
	private Date lastVisitTime;//最后访问时间
	private String lastLoginIp;// 最近一次登录ip
	private Integer loginCount;// 登录次数
	private String email;
	private String isAdmin="0";// 1超级管理员 0不是超级管理员

	private Office office;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "officeId")
	@ManyToOne
	@JoinColumn(name = "officeId")
	@NotFound(action=NotFoundAction.IGNORE)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	@Transient
	public Date getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(Date lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(String isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	@Transient
	public boolean isAdmin(){
		if("1".equals(this.isAdmin)){
			return true;
		}
		return false;
	}

}
