package com.graphics.modules.sys.web;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.common.utils.DynamicBean;
import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.sys.entity.Menu;
import com.graphics.modules.sys.entity.User;
import com.graphics.modules.sys.service.AccountService;
import com.graphics.modules.sys.service.MenuService;
import com.graphics.modules.sys.service.UserService;

/**
 * 登录Controller
 */
@RestController
@RequestMapping("${adminPath}/system/account")
public class AccountController extends BaseController {
	@Autowired
	private AccountService accountService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private UserService userService;

	/**
	 * 登录
	 * @throws Exception 
	 */
	@RequestMapping(value = "/login")
	public ApiData<Object> login(String account, String password,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 登录处理
		User user = null;
		//菜单权限
		List<Menu> menuList = null;
		try{
			user = accountService.login(account,password,request, response);
			if(user.isAdmin()){
				menuList = menuService.findAll();
			}else{
				menuList = userService.findMenusByUserId(user.getId());
			}
		}catch(ServiceException ex){
			ApiData<Object> apiData = apiReturn(0,ex.getApiCode(), null);
			return apiData;
		}
		List<Object> permissions = new DynamicBean.Builder()
										.setPV("permission",null)
										.build()
										.copyList(menuList);
		// 构造响应数据
		Object data = new DynamicBean.Builder()
				.setPV("token", user.getToken())
				.setPV("id", user.getId())
				.setPV("nickName", user.getNickname())
				.setPV("access", permissions)
				.setPV("account", user.getAccount()).build().getObject();
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, data);
		
		return apiData;
	}

	/**
	 * 退出
	 */
	@RequestMapping(value = "/logout")
	public ApiData<Object> logout(HttpServletRequest request,
			HttpServletResponse response) {
		// 退出逻辑处理
		accountService.logout(request, response);

		// 构造响应数据
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);

		return apiData;
	}

	@RequestMapping(value = "/edit")
	public ApiData<Object> edit(HttpServletRequest request,
			HttpServletResponse response,String password,String newPassword,String verifyNewPassword) {
		try {
			accountService.edit(password,newPassword, verifyNewPassword);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		// 构造响应数据
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);

		return apiData;
	}
	
	@RequestMapping(value = "/editInfo")
	public ApiData<Object> editInfo(
			String nickname,
			String email,
			String mobile) {
		try {
			accountService.editInfo(nickname, email, mobile);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
}
