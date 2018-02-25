package com.graphics.modules.sys.service;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.graphics.common.service.ServiceException;
import com.graphics.common.utils.Encodes;
import com.graphics.common.utils.IdGen;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.sys.entity.User;
import com.graphics.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class AccountService {
	@Autowired
	private UserService userService;
	
	@Transactional(readOnly=false)
	public User login(String account, String password,HttpServletRequest request, HttpServletResponse response) throws ServiceException{
		
		if(StringUtils.isBlank(account)){
			throw new ServiceException(ApiCode.ACCOUNT_LOGIN_ACCOUNT_REQUIRED);
		}
		if(StringUtils.isBlank(password)){
			throw new ServiceException(ApiCode.ACCOUNT_LOGIN_PASSWORD_REQUIRED);
		}
		
		User user = userService.findUserByAccount(account);
		if(user==null){
			throw new ServiceException(ApiCode.ACCOUNT_LOGIN_FAILED);
		}
		
		if(!Encodes.md5(password,null).equals(user.getPassword())){
			throw new ServiceException(ApiCode.ACCOUNT_LOGIN_FAILED);
		}
		
		// 设置登录信息
		Date now = new Date();
		String token = IdGen.uuid();
		user.setToken(token);
		user.setLastLoginTime(now);
		user.setLastLoginIp(StringUtils.getIpAddress(request));
		Integer loginCount = user.getLoginCount();
		if (loginCount == null) {
			loginCount = 0;
		}
		user.setLoginCount(loginCount + 1);
		user.setLastVisitTime(now);
		userService.save(user);
		return user;
	}
	
	@Transactional(readOnly=false)
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		
		User user = UserUtils.getUser();
		if(user==null){
			return;
		}
		user.setToken(null);
		userService.save(user);
		return;
	}
	
//	ACCOUNT_EDIT_PASSWORD_REQUIRED("0104","请输入原密码！"), 
//  ACCOUNT_EDIT_NEWPASSWORD_REQUIRED("0105","请输入新密码！"), 
//  ACCOUNT_EDIT_VERIFYNEWPASSWORD_REQUIRED("0106","请确认密码！"),
//  ACCOUNT_EDIT_PASSWORD_LENGTH("0107","密码过长！"),
//  ACCOUNT_EDIT_PASSWORD_FORMAT("0108","密码格式不正确！"),
	/**
	 * 修改密码
	 * @param password
	 * @param newPassword
	 * @param verifyNewPassword
	 */
	@Transactional(readOnly=false)
	public void edit(String password,String newPassword,String verifyNewPassword) {
		
		if(StringUtils.isBlank(newPassword)){
			throw new ServiceException(ApiCode.ACCOUNT_EDIT_NEWPASSWORD_REQUIRED);
		}
		if(StringUtils.isBlank(verifyNewPassword)){
			throw new ServiceException(ApiCode.ACCOUNT_EDIT_VERIFYNEWPASSWORD_REQUIRED);
		}
		if(!newPassword.equals(verifyNewPassword)){
			throw new ServiceException(ApiCode.ACCOUNT_EDIT_PASSWORDS_VERIFY);
		}
		User user = UserUtils.getUser();
		if(user==null){
			return;
		}
		//StudentUser user = userService.get(id);
		if(StringUtils.isBlank(password)){
			throw new ServiceException(ApiCode.ACCOUNT_EDIT_PASSWORD_REQUIRED);
		}else if(password.length() > 16){
			throw new ServiceException(ApiCode.ACCOUNT_EDIT_PASSWORD_LENGTH);
		}else if(!(user.getPassword()).equals(Encodes.md5(password,null))){
			throw new ServiceException(ApiCode.ACCOUNT_EDIT_PASSWORD_VERIFY);
		}
		user.setPassword(Encodes.md5(newPassword,null));
		userService.save(user);
	}
	
	@Transactional(readOnly = false)
	public void editInfo(String nickname, String email,String mobile)throws ServiceException {

		User user = UserUtils.getUser();
		if(user==null){
			return;
		}
		/*if (user == null) {
			throw new ServiceException(ApiCode.PERMISSION_USER_NOTEXIST);
		}*/
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
		
		user.setNickname(StringEscapeUtils.unescapeHtml4(nickname));
		user.setMobile(mobile);
		user.setEmail(StringEscapeUtils.unescapeHtml4(email));
		
		userService.save(user);
	}
}
