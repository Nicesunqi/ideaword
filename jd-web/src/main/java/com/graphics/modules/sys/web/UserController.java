package com.graphics.modules.sys.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.graphics.common.persistence.Page;
import com.graphics.common.service.ServiceException;
import com.graphics.common.utils.DynamicBean;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.sys.entity.Office;
import com.graphics.modules.sys.entity.User;
import com.graphics.modules.sys.entity.UserRole;
import com.graphics.modules.sys.service.OfficeService;
import com.graphics.modules.sys.service.UserService;

/**
 * 后台用户控制器
 */
@RestController("StudentUserController")
@RequestMapping("${adminPath}/system/user")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private OfficeService officeService;

	@RequiresPermissions("permission:user:view")
	@RequestMapping(value = "/search")
	public ApiData<Object> search(
			@RequestParam(required = false) String account,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) String email,
			@RequestParam(value = "office_id",required = false) String officeId,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value = "limit", defaultValue = "10") int limit,
			@RequestParam(value = "order", defaultValue = "timestamp desc") String order) {
		//order = NamingStrategyUtils.camelName(order);
		// 设置查询条件
		Page<User> page = new Page<User>(pageNo, limit);
		page.setOrderBy(order);
		User user = new User();
		user.setNickname(StringEscapeUtils.unescapeHtml4(name));
		user.setAccount(account);
		user.setEmail(StringEscapeUtils.unescapeHtml4(email));
		if (StringUtils.isNotBlank(officeId)) {
			user.setOffice(officeService.get(officeId));
		}

		// 查询
		page = userService.find(page, user);

		// 构造返回数据

		List<Object> userList = new ArrayList<Object>();
		for (User u : page.getList()) {
			Office office = u.getOffice();
			String oId = null;
			String oName = null;
			if (office != null) {
				oId = office.getId();
				oName = office.getName();
			}
			Object userBean = new DynamicBean.Builder().setPV("id", u.getId())
					.setPV("account", u.getAccount())
					.setPV("nickname", u.getNickname())
					.setPV("email", u.getEmail()).setPV("officeId", oId)
					.setPV("officeName", oName)
					.setPV("lastLoginIp", u.getLastLoginIp())
					.setPV("loginCount", u.getLoginCount())
					.setPV("lastLoginTime", u.getLastLoginTime())
					.setPV("createDate", u.getCreateDate()).build().getObject();

			userList.add(userBean);
		}

		Object data = new DynamicBean.Builder()
				.setPV("total", page.getCount())
				.setPV("users", userList, List.class)
				.build().getObject();
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, data);

		return apiData;
	}
	
	@RequestMapping(value = "/get")
	public ApiData<Object> get(String id, HttpServletRequest request,
			HttpServletResponse response) {
		
		User user = userService.get(id);
		// 获取用户所有角色数据
		List<UserRole> userRoles = userService.findUserRoleByUserId(id);
		// 获取所属机构
		Office office = user.getOffice();
		String officeId = null;
		if (office != null) {
			String oId = office.getId();
			//office = officeService.get(oId);
			officeId = oId;
		}
		
		//一个部门层级id集合
		List<String> officeIdList = new ArrayList<String>();
		if(StringUtils.isNotBlank(officeId)){
			officeIdList = userService.findOneOffice(officeId);
		}
		
		// 构造响应数据
		List<Object> roles = new ArrayList<Object>();
		for(UserRole userRole:userRoles){
			Object roleBean = new DynamicBean.Builder().setPV("id", userRole.getRoleId()).build().getObject();
			roles.add(roleBean);
		}
		Object object = new DynamicBean.Builder().setPV("id", user.getId())
				.setPV("account", user.getAccount())
				.setPV("nickname", user.getNickname())
				.setPV("email", user.getEmail())
				.setPV("mobile", user.getMobile()).setPV("officeId", officeId)
				.setPV("roles", roles, List.class)
				.setPV("officeIdList", officeIdList, List.class)
				.setPV("office", office, Office.class).build().getObject();

		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, object);
		return apiData;
	}

	/**
	 * 删除用户 同时删除用户角色
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("permission:user:del")
	@RequestMapping(value = "/delete")
	public ApiData<Object> delete(String id) {
		userService.delete(id);
		// 构造响应数据
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}

	/**
	 * 创建用户
	 * 
	 * @param account
	 * @param nickname
	 * @param email
	 * @param officeId
	 * @param password
	 * @param mobile
	 * @param roles
	 * @param verifyPassword 确认密码
	 * @return
	 */
	@RequiresPermissions("permission:user:add")
	@RequestMapping(value = "/create")
	public ApiData<Object> create(
			String account,
			String nickname,
			String email,
			@RequestParam(value = "office_id")String officeId,
			String password,
			String mobile,
			String verifyPassword,
			@RequestParam(value = "roles[]") List<String> roles) {
		try {
			userService.create(account, nickname, email, officeId, password,
					mobile,verifyPassword, roles);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}

	/**
	 * 编辑用户 同时修改用户角色信息.如果password为空，表示不修改密码
	 * 
	 * @param id
	 * @param account
	 * @param nickname
	 * @param email
	 * @param officeId
	 * @param password
	 * @param mobile
	 * @param roles
	 * @param verifyPassword 确认密码
	 * @return
	 */
	@RequiresPermissions("permission:user:edit")
	@RequestMapping(value = "/update")
	public ApiData<Object> update(
			String id,
			String account,
			String nickname,
			String email,
			String verifyPassword,
			@RequestParam(value = "office_id")String officeId,
			@RequestParam(required = false) String password,
			String mobile,
			@RequestParam(value = "roles[]", required = false) List<String> roles) {
		try {
			userService.update(id, nickname, email, officeId, password, mobile,verifyPassword,
					roles);
		} catch (ServiceException ex) {
			ApiData<Object> apiData = apiReturn(0, ex.getApiCode(), null);
			return apiData;
		}
		ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
		return apiData;
	}

}
