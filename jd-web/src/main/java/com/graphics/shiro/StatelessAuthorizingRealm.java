package com.graphics.shiro;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.graphics.common.utils.StringUtils;
import com.graphics.common.config.Global;
import com.graphics.common.utils.Encodes;
import com.graphics.common.utils.SpringContextHolder;
import com.graphics.modules.sys.entity.Menu;
import com.graphics.modules.sys.entity.User;
import com.graphics.modules.sys.service.UserService;
import com.graphics.modules.sys.utils.UserUtils;

public class StatelessAuthorizingRealm extends AuthorizingRealm {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 使用HashedCredentialsMatcher进行md5验证
	 * 
	 * @param hashedCredentialsMatcher
	 */
	@Autowired
	public void setHashedCredentialsMatcher(
			CredentialsMatcher hashedCredentialsMatcher) {
		super.setCredentialsMatcher(hashedCredentialsMatcher);
	}

	/**
	 * 仅支持StatelessToken 类型的Token，
	 * 那么如果在StatelessAuthcFilter类中返回的是UsernamePasswordToken，那么将会报如下错误信息： Please
	 * ensure that the appropriate Realm implementation is configured correctly
	 * or that the realm accepts AuthenticationTokens of this
	 * type.StatelessAuthcFilter.isAccessAllowed()
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof StatelessAuthenticationToken;
	}

	/**
	 * 认证回调函数, 登录时调用
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UserService userService = SpringContextHolder
				.getBean(UserService.class);
		if (Global.isDebug()) {
			logger.info("StatelessRealm.doGetAuthenticationInfo()");
		}
		StatelessAuthenticationToken statelessToken = (StatelessAuthenticationToken) token;
		String key = statelessToken.getToken();
		if(StringUtils.isNotBlank(key)){
			User user = userService.findUserByToken(key);
			String hashedCredentials = "";
			if (user != null) {
				hashedCredentials = Encodes.md5(key, null);
			}
			if (user != null) {
				SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
						key, hashedCredentials, null, getName());
				return authenticationInfo;
			}
		}
		
		return null;
	}

	/**
	 * 授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		UserService userService = SpringContextHolder
				.getBean(UserService.class);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		User user = UserUtils.getUser();
		List<Menu> list = userService.findMenusByUserId(user.getId());
		for (Menu menu : list) {
			if (StringUtils.isNotBlank(menu.getPermission())) {
				// 添加基于Permission的权限信息
				for (String permission : StringUtils.split(
						menu.getPermission(), ",")) {
					info.addStringPermission(permission);
				}
			}
		}
		return info;
	}
}
