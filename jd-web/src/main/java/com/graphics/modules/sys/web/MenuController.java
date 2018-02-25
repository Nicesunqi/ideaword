package com.graphics.modules.sys.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.sys.entity.Menu;
import com.graphics.modules.sys.service.MenuService;

/**
 * 菜单Controller
 */
@RestController
@RequestMapping("${adminPath}/system/menu")
public class MenuController extends BaseController {
	@Autowired
	private MenuService menuService;

	@RequiresPermissions("permission:menu:view")
	@RequestMapping(value = "/get")
	public ApiData<Menu> get(String id, HttpServletRequest request,
			HttpServletResponse response) {
		Menu menu = menuService.get(id);
		ApiData<Menu> apiData = apiReturn(1, ApiCode.SUCCESS, menu);
		return apiData;
	}

	/**
	 * 获取所有菜单
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("permission:menu:view")
	@RequestMapping(value = "/all")
	public ApiData<List<Menu>> all(HttpServletRequest request,
			HttpServletResponse response) {
		List<Menu> menuList = menuService.allListTree();
		ApiData<List<Menu>> apiData = apiReturn(1, ApiCode.SUCCESS,
				menuList);
		return apiData;
	}

	/**
	 * 删除菜单 同时删除所有子菜单
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("permission:menu:edit")
	@RequestMapping(value = "/delete")
	public ApiData<Object> delete(String id) {
		menuService.delete(id);
		// 构造响应数据
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}

	/**
	 * 新建菜单
	 * @param name
	 * @param pid
	 * @param sort
	 * @param permission
	 * @param isShow
	 * @return
	 */
	@RequiresPermissions("permission:menu:edit")
	@RequestMapping(value = "/create")
	public ApiData<Object> create(String name,
			@RequestParam(value = "pid", defaultValue = "0") String pid,
			@RequestParam(value = "sort", defaultValue = "999") int sort,
			String permission,
			@RequestParam(value = "isShow", defaultValue = "1")int isShow) {
		try {
			menuService.create(pid, sort, name, permission, isShow);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
	
	/**
	 * 修改菜单
	 * @param id
	 * @param name
	 * @param sort
	 * @param permission
	 * @param isShow
	 * @return
	 */
	@RequiresPermissions("permission:menu:edit")
	@RequestMapping(value = "/update")
	public ApiData<Object> update(String id,String name,
			@RequestParam(value = "sort", defaultValue = "999") int sort,
			String permission,
			@RequestParam(value = "isShow", defaultValue = "1")int isShow){
		try {
			menuService.update(id, name, sort, permission, isShow);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}

}
