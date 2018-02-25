package com.graphics.modules.sys.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.common.utils.DynamicBean;
import com.graphics.common.persistence.Page;
import com.graphics.common.service.ServiceException;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.sys.entity.Menu;
import com.graphics.modules.sys.entity.Role;
import com.graphics.modules.sys.entity.RoleMenu;
import com.graphics.modules.sys.service.MenuService;
import com.graphics.modules.sys.service.RoleService;

/**
 * 角色Controller
 */
@RestController
@RequestMapping("${adminPath}/system/role")
public class RoleController extends BaseController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private MenuService menuService;
	
	
	/**
	 * 获取所有角色
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/all")
	public ApiData<List<Object>> all(HttpServletRequest request,
			HttpServletResponse response) {
		List<Role> roleList = roleService.findAll();
		
		List<Object> dataList = new DynamicBean.Builder()
			.setPV("id", null)
			.setPV("name", null)
			.build()
			.copyList(roleList);
		
		ApiData<List<Object>> apiData = apiReturn(1, ApiCode.SUCCESS,
				dataList);
		return apiData;
	}
	
	/**
	 * 取特定角色，同时会返回所有菜单及角色菜单
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequiresPermissions("permission:role:view")
	@RequestMapping(value = "/get")
	public ApiData<Object> get(String id, HttpServletRequest request,
			HttpServletResponse response) {
		Role role = roleService.get(id);
		List<Menu> menus = menuService.allListTree();
		List<RoleMenu> roleMenus = roleService.findRoleMenuByRoleId(id);
		List<Object> roleMenuList = new DynamicBean.Builder()
		.setPV("menuId", null)
		.build()
		.copyList(roleMenus);
		//构造响应数据
		Object object = new DynamicBean.Builder()
			.setPV("role", role,Role.class)
			.setPV("permissions", menus,List.class)
			.setPV("permissionsSelected",roleMenuList, List.class)
			.build().getObject();
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, object);
		return apiData;
	}
	
	@RequiresPermissions("permission:role:view")
	@RequestMapping(value = "/search")
	public ApiData<Object> search(String name,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "order", defaultValue = "timestamp desc") String order) {

		// 设置查询条件
		Page<Role> page = new Page<Role>(pageNo,limit);
		page.setOrderBy(order);
		Role role = new Role();
		role.setName(name);

		// 查询
		page = roleService.find(page, role);

		// 构造返回数据
		List<Object> roleList = new DynamicBean.Builder()
			.setPV("id", null)
			.setPV("name", null)
			.setPV("createDate", null,Date.class)
			.setPV("updateDate", null,Date.class)
			.build()
			.copyList(page.getList());
		Object data = new DynamicBean.Builder()
			.setPV("total", page.getCount())
			.setPV("count", page.getCount())
			.setPV("roles",roleList, List.class)
			.build().getObject();
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS,data);

		return apiData;
	}
	
	/**
     * 获取所有菜单
     */
	@RequiresPermissions("permission:role:add")
	@RequestMapping(value = "/menus")
    public ApiData<Object>  menus()
    {
		List<Menu> menus = menuService.allListTree();
		//构造响应数据
		Object object = new DynamicBean.Builder()
			.setPV("permissions", menus,List.class)
			.build().getObject();
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS,object);
		return apiData;
    }
	
	/**
	 * 创建角色
	 * @param name
	 * @param permissions
	 * @return
	 */
	@RequiresPermissions("permission:role:add")
	@RequestMapping(value = "/create")
	public ApiData<Object> create(String name,
			@RequestParam(value = "permissions[]", required = false) List<String> permissions) {
		try {
			roleService.create(name, permissions);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
	
	/**
	 * 编辑角色
	 * @param id
	 * @param name
	 * @param permissions
	 * @return
	 */
	@RequiresPermissions("permission:role:edit")
	@RequestMapping(value = "/update")
	public ApiData<Object> update(String id,String name,
			@RequestParam(value = "permissions[]", required = false) List<String> permissions) {
		try {
			roleService.update(id,name, permissions);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
	
	/**
	 * 删除角色 同时删除角色菜单
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("permission:role:del")
	@RequestMapping(value = "/delete")
	public ApiData<Object> delete(String id) {
		roleService.delete(id);
		// 构造响应数据
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}
	
}
