package com.graphics.shiro;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 用于授权的Token对象
 */
public class StatelessAuthenticationToken implements AuthenticationToken {
	private static final long serialVersionUID = 1L;
	private Map<String, ?> params;// 参数.
	private String token;//令牌

	public StatelessAuthenticationToken() {
	}

	public StatelessAuthenticationToken(String token,Map<String, ?> params) {
		super();
		this.params = params;
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	public Map<String, ?> getParams() {
		return params;
	}

	public void setParams(Map<String, ?> params) {
		this.params = params;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}