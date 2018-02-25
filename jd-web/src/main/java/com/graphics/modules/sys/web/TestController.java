package com.graphics.modules.sys.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${adminPath}/test")
public class TestController {
	/**
	 * 此方法执行的时候，会抛出异常： Session creation has been disabled for the current
	 * subject.
	 * @param session
	 * @return
	 */
	@RequestMapping("/session")
	public String session() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		System.out.println(session);
		return "hello,session";
	}
	@RequestMapping("/permiss")
	@RequiresPermissions("test:view")
	public String permiss() {
		return "has permiss";
	}
}
