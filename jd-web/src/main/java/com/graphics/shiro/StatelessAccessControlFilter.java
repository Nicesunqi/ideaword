package com.graphics.shiro;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graphics.common.config.Global;

/**
 * 访问控制过滤器
 */
public class StatelessAccessControlFilter extends AccessControlFilter {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 先执行：isAccessAllowed 再执行onAccessDenied
	 * 
	 * isAccessAllowed：表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分，
	 * 如果允许访问返回true，否则false；
	 * 
	 * 如果返回true的话，就直接返回交给下一个filter进行处理。 如果返回false的话，回往下执行onAccessDenied
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		if (Global.isDebug()) {
			logger.info("进入isAccessAllowed");
		}
		return false;
	}

	/**
	 * onAccessDenied：表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；
	 * 如果返回false表示该拦截器实例已经处理了，将直接返回即可。
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		if (Global.isDebug()) {
			HttpServletRequest req = (HttpServletRequest) request;
			String url = req.getRequestURI();
			logger.info("进入onAccessDenied,url={}",url);
		}
		HttpServletRequest req = (HttpServletRequest) request;
		
		// 1、获取token
		String tokenKey = req.getHeader("token");
		
		// 3、客户端请求的参数列表
		Map<String, String[]> params = new HashMap<String, String[]>(
				request.getParameterMap());
		params.remove("digest");// 为什么要移除呢？签名或者消息摘要算法的时候不能包含digest.

		// 4、生成无状态Token
		StatelessAuthenticationToken token = new StatelessAuthenticationToken(tokenKey, params);
		try {
			// 5、委托给Realm进行登录
			getSubject(request, response).login(token);
		} catch (Exception e) {
			// 6、登录失败
			onLoginFail(response);
			return false;// 就直接返回给请求者.
		}
		return true;
	}

	// 登录失败时默认返回401 状态码
	private void onLoginFail(ServletResponse response) throws IOException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpResponse.getWriter().write("login error");
	}

}
