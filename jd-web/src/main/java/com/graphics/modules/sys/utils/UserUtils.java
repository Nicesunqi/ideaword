package com.graphics.modules.sys.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import com.graphics.common.service.BaseService;
import com.graphics.common.utils.SpringContextHolder;
import com.graphics.modules.sys.entity.User;
import com.graphics.modules.sys.service.UserService;

/**
 * 用户工具类
 */
public class UserUtils extends BaseService {
	
	private static UserService userService = SpringContextHolder.getBean(UserService.class);
	
	/**
	 * 登录用户
	 */
	public static User getUser() {
		User user = null;
		Subject subject = SecurityUtils.getSubject();
		String token = (String) subject.getPrincipal();
		if (token != null) {
			user = userService.findUserByToken(token);
		}
		return user;
	}
}
