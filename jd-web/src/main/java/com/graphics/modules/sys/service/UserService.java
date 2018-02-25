package com.graphics.modules.sys.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graphics.common.utils.StringUtils;
import com.google.common.collect.Lists;
import com.graphics.common.persistence.Page;
import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.utils.Encodes;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.sys.dao.MenuDao;
import com.graphics.modules.sys.dao.OfficeDao;
import com.graphics.modules.sys.dao.RoleDao;
import com.graphics.modules.sys.dao.RoleMenuDao;
import com.graphics.modules.sys.dao.UserDao;
import com.graphics.modules.sys.dao.UserRoleDao;
import com.graphics.modules.sys.entity.Menu;
import com.graphics.modules.sys.entity.Office;
import com.graphics.modules.sys.entity.Role;
import com.graphics.modules.sys.entity.User;
import com.graphics.modules.sys.entity.UserRole;

/**
 * 用户Service
 */
@Service
@Transactional(readOnly = true)
public class UserService extends BaseService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private RoleMenuDao roleMenuDao;
	@Autowired
	private MenuDao menuDao; 
	@Autowired
	private RoleDao roleDao;
	public User get(String id) {
		return userDao.get(id);
	}

	public List<User> findAllList() {
		return userDao.findAllList();
	}
	
	public List<UserRole> findUserRoleByUserId(String userId){
		return userRoleDao.findAllByUserId(userId);
	}
	/**
	 * 查找用户菜单
	 * @param userId
	 * @return
	 */
	public List<Menu> findMenusByUserId(String userId){
		List<Menu> menuList = Lists.newArrayList();
		List<UserRole> userRoleList = userRoleDao.findAllByUserId(userId);
		User user = userDao.get(userId);
		if(user!=null&&user.isAdmin()){
			menuList = menuDao.findAll();
		}else{
			for(UserRole userRole:userRoleList){
				String roleId = userRole.getRoleId();
				List<Menu> menus = roleMenuDao.findMenusByRoleId(roleId);
				for(Menu m:menus){
					boolean has = false;
					for(Menu menu:menuList){
//						if("68291fe0d5744156a544cc8aed3b344a".equals(menu.getId())){
//							System.err.println();
//						}
						if(m.getId().equals(menu.getId())){
							has = true;
							break;
						}
					}
					if(!has){
						menuList.add(m);
					}
				}
			}
		}
		
		return menuList;
	}

	@Transactional(readOnly = false)
	public void save(User user) {
		userDao.save(user);
	}

	public User findUserByAccount(String account) {
		return userDao.findByAccount(account);
	}

	public User findUserByToken(String token) {
		return userDao.findByToken(token);
	}

	public Page<User> find(Page<User> page, User user) {
		DetachedCriteria dc = userDao.createDetachedCriteria();
		if (StringUtils.isNotEmpty(user.getAccount())) {
			dc.add(Restrictions.like("account", "%" + user.getAccount() + "%"));
		}
		if (StringUtils.isNotEmpty(user.getNickname())) {
			dc.add(Restrictions.like("nickname", "%" + user.getNickname() + "%"));
		}
		if (StringUtils.isNotEmpty(user.getEmail())) {
			dc.add(Restrictions.like("email", "%" + user.getEmail() + "%"));
		}
		if (user.getOffice() != null
				&& StringUtils.isNotBlank(user.getOffice().getCode())) {
			DetachedCriteria dcwei = dc.createAlias("office", "o");
            dcwei.add(Restrictions.like("o.code", user.getOffice().getCode()
					+ "%"));
		}

		dc.add(Restrictions.eq(User.FIELD_DEL_FLAG, User.DEL_FLAG_NORMAL));
		page = userDao.find(page, dc);
		//解除延迟加载office
		for(User u:page.getList()){
			Hibernate.initialize(u.getOffice());
		}
		return page;
	}

	@Transactional(readOnly = false)
	public void delete(String userId) {
		userDao.deleteById(userId);
		userRoleDao.deleteByUserId(userId);
	}

	/**
	 * 新增用户
	 * 
	 * @param account
	 *            帐号
	 * @param nickname
	 *            昵称
	 * @param email
	 *            邮箱
	 * @param officeId
	 *            部门id
	 * @param password
	 *            密码
	 * @param mobile
	 *            手机号码
	 * @param roles
	 *            角色集合
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void create(String account, String nickname, String email,
			String officeId, String password, String mobile,String verifyPassword, List<String> roles)
			throws ServiceException {

		// 数据验证
		if (StringUtils.isBlank(account)) {
			throw new ServiceException(ApiCode.PERMISSION_USER_ACCOUNT_REQUIRED);
		}else{
			String reg="^[A-Za-z0-9]+$";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(account);
			if(account.length() < 6 || account.length() > 16 || !matcher.matches()){
				throw new ServiceException(ApiCode.PERMISSION_USER_ACCOUNT_REGEX);	//长度为 6 到 16 个字母或数字
			}
		}
		User user = userDao.findByAccount(account);
		if (user != null) {
			throw new ServiceException(ApiCode.PERMISSION_USER_ACCOUNT_EXIST);
		}
		if (StringUtils.isBlank(nickname)) {
			throw new ServiceException(
					ApiCode.PERMISSION_USER_NICKNAME_REQUIRED);
		}else if(StringEscapeUtils.unescapeHtml4(nickname).length() > 20){
			throw new ServiceException(ApiCode.PERMISSION_USER_NICKNAME_LENGTH);		//长度为为 20 字符以内
		}
		if (StringUtils.isNotBlank(email)) {
			String reg="^([a-zA-Z0-9_\\-\\.])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+$";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(email);
			if(StringEscapeUtils.unescapeHtml4(email).length() > 100 || !matcher.matches()){
				throw new ServiceException(ApiCode.PERMISSION_USER_EMAIL_FORMAT);	//请输入正确的邮箱
			}
		}
		
		if(StringUtils.isBlank(mobile)){
			throw new ServiceException(ApiCode.PERMISSION_USER_MOBILE_REQUIRED);
		}else{
			String reg="^1[34578]\\d{9}$";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(mobile);
			if(!matcher.matches()){
				throw new ServiceException(ApiCode.PERMISSION_USER_MOBILE_FORMAT);
			}
		}

		if (StringUtils.isBlank(officeId)) {
			throw new ServiceException(ApiCode.PERMISSION_OFFICE_NAME_REQUIRED);
		}
		Office office = officeDao.get(officeId);
		if (office == null || "1".equals(office.getDelFlag())) {
			throw new ServiceException(ApiCode.PERMISSION_OFFICE_NOT_EXIST);
		}

		if (StringUtils.isBlank(password)) {
			throw new ServiceException(
					ApiCode.PERMISSION_USER_PASSWORD_REQUIRED);
		}else{
			String reg="^[A-Za-z0-9]+$";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(password);
			if(password.length() < 6 || password.length() > 16 || !matcher.matches()){
				throw new ServiceException(ApiCode.PERMISSION_USER_PASSWORD_REGEX);	//长度为 6 到 16 个字母或数字
			}else{
				if(StringUtils.isBlank(verifyPassword)){
					throw new ServiceException(ApiCode.PERMISSION_USER_PASSWORD_VERIFY);	
				}else if(!password.equals(verifyPassword)){
					throw new ServiceException(ApiCode.PERMISSION_USER_PASSWORD_INCONFORMITY);
				}
			}
		}
		
		if(roles == null || roles.size() == 0){
			throw new ServiceException(ApiCode.PERMISSION_ROLE_NAME_REQUIRED);
		}
		
		for(String role : roles){
			if(StringUtils.isBlank(role)){
				throw new ServiceException(ApiCode.PERMISSION_ROLE_NOTEXIST);
			}else{
				Role r = roleDao.get(role);
				if(r == null || "1".equals(r.getDelFlag())){
					throw new ServiceException(ApiCode.PERMISSION_ROLE_NOTEXIST);
				}
			}
		}
		
		// 创建用户
		user = new User();
		user.setAccount(account);
		user.setNickname(StringEscapeUtils.unescapeHtml4(nickname));
		user.setPassword(Encodes.md5(password, null));
		user.setLoginCount(0);
		user.setEmail(StringEscapeUtils.unescapeHtml4(email));
		user.setMobile(mobile);
		user.setOffice(office);
		userDao.save(user);

		// 创建用户角色
//		if (roles != null) {
			List<UserRole> userRoleList = Lists.newArrayList();
			for (String role : roles) {
				UserRole userRole = new UserRole();
				userRole.setUserId(user.getId());
				userRole.setRoleId(role);
				userRoleList.add(userRole);
			}
			userRoleDao.save(userRoleList);
//		}
	}

	/**
	 * 编辑用户 同时修改用户角色信息.如果password为空，表示不修改密码
	 * 
	 * @param id
	 * @param nickname
	 * @param email
	 * @param officeId
	 * @param password	
	 * @param mobile
	 * @param roles
	 * @throws ServiceException
	 */
	@Transactional(readOnly = false)
	public void update(String id, String nickname, String email,
			String officeId, String password, String mobile,String verifyPassword,List<String> roles)
			throws ServiceException {

		// 数据验证
		User user = userDao.get(id);
		if (user == null) {
			throw new ServiceException(ApiCode.PERMISSION_USER_NOTEXIST);
		}
		if (StringUtils.isBlank(nickname)) {
			throw new ServiceException(
					ApiCode.PERMISSION_USER_NICKNAME_REQUIRED);
		}else if(StringEscapeUtils.unescapeHtml4(nickname).length() > 20){
			throw new ServiceException(ApiCode.PERMISSION_USER_NICKNAME_LENGTH);		//长度为为 20 字符以内
		}
		
		if (StringUtils.isNotBlank(email)) {
			String reg="^([a-zA-Z0-9_\\-\\.])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+$";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(email);
			if(StringEscapeUtils.unescapeHtml4(email).length() > 100 || !matcher.matches()){
				throw new ServiceException(ApiCode.PERMISSION_USER_EMAIL_FORMAT);	//请输入正确的邮箱
			}
		}
		
		if(StringUtils.isBlank(mobile)){
			throw new ServiceException(ApiCode.PERMISSION_USER_MOBILE_REQUIRED);
		}else{
			String reg="^1[34578]\\d{9}$";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(mobile);
			if(!matcher.matches()){
				throw new ServiceException(ApiCode.PERMISSION_USER_MOBILE_FORMAT);
			}
		}
		
		if (StringUtils.isBlank(officeId)) {
			throw new ServiceException(ApiCode.PERMISSION_USER_OFFICE_REQUIRED);
		}
		Office office = officeDao.get(officeId);
		if (office == null || "1".equals(office.getDelFlag())) {
			throw new ServiceException(ApiCode.PERMISSION_OFFICE_NOT_EXIST);
		}
		
		if(roles == null || roles.size() == 0){
			throw new ServiceException(ApiCode.PERMISSION_ROLE_NAME_REQUIRED);
		}
		
		for(String role : roles){
			if(StringUtils.isBlank(role)){
				throw new ServiceException(ApiCode.PERMISSION_ROLE_NOTEXIST);
			}else{
				Role r = roleDao.get(role);
				if(r == null || "1".equals(r.getDelFlag())){
					throw new ServiceException(ApiCode.PERMISSION_ROLE_NOTEXIST);
				}
			}
		}
		
		if(StringUtils.isNotBlank(password)){
			String reg="^[A-Za-z0-9]+$";
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(password);
			if(password.length() < 6 || password.length() > 16 || !matcher.matches()){
				throw new ServiceException(ApiCode.PERMISSION_USER_PASSWORD_REGEX);	//长度为 6 到 16 个字母或数字
			}else{
				if(StringUtils.isBlank(verifyPassword)){
					throw new ServiceException(ApiCode.PERMISSION_USER_PASSWORD_VERIFY);	
				}else if(!password.equals(verifyPassword)){
					throw new ServiceException(ApiCode.PERMISSION_USER_PASSWORD_INCONFORMITY);
				}
			}
		}
//		user.setAccount(account);
		user.setNickname(StringEscapeUtils.unescapeHtml4(nickname));
		user.setMobile(mobile);
		user.setEmail(StringEscapeUtils.unescapeHtml4(email));
		user.setOffice(office);
		if(StringUtils.isNotBlank(password)){
			user.setPassword(Encodes.md5(password, null));
		}
		userDao.save(user);

		// 更新用户角色
		List<UserRole> userRoleList = userRoleDao.findAllByUserId(id);
		List<UserRole> deleteList = Lists.newArrayList();// 需要删除的角色菜单
		List<UserRole> addList = Lists.newArrayList();// 需要新增的角色菜单
//		if (roles == null) {
//			deleteList.addAll(userRoleList);
//		} else {
			// 确定需要新增的用户角色
			for (String role : roles) {
				boolean has = false;
				for (UserRole ur : userRoleList) {
					if (ur.getRoleId().equals(role)) {
						has = true;
						break;
					}
				}
				if (!has) {// 需要新增
					UserRole ur = new UserRole();
					ur.setRoleId(role);
					ur.setUserId(id);
					addList.add(ur);
				}
			}
			// 确定需要删除的用户角色
			for (UserRole  ur: userRoleList) {
				boolean has = false;
				for (String role : roles) {
					if (ur.getRoleId().equals(role)) {
						has = true;
						break;
					}
				}
				if (!has) {
					ur.setDelFlag(UserRole.DEL_FLAG_DELETE);// 删除
					deleteList.add(ur);
				}
			}
//		}
		userRoleDao.save(addList);
		userRoleDao.save(deleteList);
	}
	
	/**
	 * 获取部门id层级数组
	 * @param officeId
	 * @return
	 */
	public List<String> findOneOffice(String officeId){
		Office office = officeDao.get(officeId);
		List<String> officeIdList = new ArrayList<String>();
		while(true){
			officeIdList.add(office.getId());
			if(!"0".toString().equals(office.getPid())){
				office=officeDao.get(office.getPid());
			}else{
				break;
			}
		}
		
		//反转数组元素
		Collections.reverse(officeIdList);
		return officeIdList;
	}
	
}
