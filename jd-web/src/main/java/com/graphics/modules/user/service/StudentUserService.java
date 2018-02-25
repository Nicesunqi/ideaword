package com.graphics.modules.user.service;

import com.graphics.common.persistence.Page;
import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.utils.IdGen;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.user.entity.StudentUser;
import com.graphics.modules.user.dao.StudentUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class StudentUserService extends BaseService{


    @Autowired
    StudentUserDao userDao;

    public StudentUser get(String id) {
        return userDao.get(id);
    }

    public StudentUser findUserByStuNo(String stuNo, HttpServletRequest request) {
        StudentUser user =  userDao.findUserByStuNo(stuNo);
        if(user == null){
            return null;
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
        userDao.save(user);
        return user;
    }

    public Page<StudentUser>findUserList(Page<StudentUser>page,StudentUser user) {
        page = userDao.findUserList(page,user);
        return page;
    }

    @Transactional(readOnly = false)
    public void deleteDataById(String id) {
        userDao.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void add(StudentUser user) {
        if (StringUtils.isEmpty(user.getName())) {
            throw new ServiceException(ApiCode.PERMISSION_USER_NICKNAME_REQUIRED);
        } else if (StringUtils.isEmpty(user.getStuNo())) {
            throw new ServiceException(ApiCode.PERMISSION_USER_STU_NO_REQUIRED);
        } else if (StringUtils.isEmpty(user.getAddress())) {
            throw new ServiceException(ApiCode.PERMISSION_USER_ADDRESS_REQUIRED);
        } else if (StringUtils.isEmpty(user.getSex())) {
            throw new ServiceException(ApiCode.PERMISSION_USER_SEX_REQUIRED);
        }

        if (StringUtils.isEmpty(user.getEmail())) {
            throw new ServiceException(ApiCode.PERMISSION_USER_EMIAL_REQUIRED);
        } else {
            String emailreg = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w{2,3}){1,3})$";
            Pattern pattern = Pattern.compile(emailreg);
            Matcher matcher = pattern.matcher(user.getEmail());
            if (!matcher.matches()) {
                throw new ServiceException(ApiCode.PERMISSION_USER_EMIAL_PATTERN_REQUIRED);    //长度为 6 到 16 个字母或数字
            }
        }

        if (StringUtils.isEmpty(user.getWechat())) {
            throw new ServiceException(ApiCode.PERMISSION_USER_WECHAT_REQUIRED);
        } else {
            String wxreg = "^[a-z_\\d]+$";
            Pattern pattern = Pattern.compile(wxreg);
            Matcher matcher = pattern.matcher(user.getWechat());
            if (!matcher.matches()) {
                throw new ServiceException(ApiCode.PERMISSION_USER_WECHAT_PATTERN_REQUIRED);    //长度为 6 到 16 个字母或数字
            }
        }

        if (StringUtils.isEmpty(user.getQq())) {
            throw new ServiceException(ApiCode.PERMISSION_USER_QQ_REQUIRED);
        } else {
            String wxreg = "^[a-z_\\d]+$";
            Pattern pattern = Pattern.compile(wxreg);
            Matcher matcher = pattern.matcher(user.getWechat());
            if (!matcher.matches()) {
                throw new ServiceException(ApiCode.PERMISSION_USER_QQ_PATTERN_REQUIRED);    //长度为 6 到 16 个字母或数字
            }
        }

        userDao.save(user);
    }


}
