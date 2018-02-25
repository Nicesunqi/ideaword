package com.graphics.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.graphics.common.config.Global;

/**
 * 跨域访问过滤器
 */
public class DifferentDomainFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String method = req.getMethod();
		boolean differentDomain = Global.isDifferentDomain();
		if(differentDomain){
			 res.setHeader("Access-Control-Allow-Origin","*");
			 res.setHeader("Access-Control-Allow-Methods","*");
		}
		res.setHeader("Access-Control-Allow-Headers","x-requested-with,content-type,token");
		if ("OPTIONS".equals(method)) {
			if (differentDomain) {//允许访问
				return;
			} else {
				res.sendError(400, "禁止跨越访问");
			}
		}else{
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
