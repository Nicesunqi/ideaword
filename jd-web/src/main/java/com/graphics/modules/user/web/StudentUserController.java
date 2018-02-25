package com.graphics.modules.user.web;

import com.graphics.common.config.Global;
import com.graphics.common.persistence.Page;
import com.graphics.common.utils.DynamicBean;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.user.entity.StudentUser;
import com.graphics.modules.user.service.StudentUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("${adminPath}/impl/user")
public class StudentUserController extends BaseController{

    @Autowired
    StudentUserService userService;

    @ModelAttribute
    public StudentUser get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return userService.get(id);
        } else {
            return new StudentUser();
        }
    }

    @RequestMapping(value = "/login")
    public ApiData<String>login(String stuNo,String password,HttpServletRequest request,HttpServletResponse response){
        StudentUser user = userService.findUserByStuNo(stuNo,request);
        if(user == null){
            ApiData<String> apiData = apiReturn(0, ApiCode.ACCOUNT_LOGIN_FAILED,null);
            return apiData;
        }
        if(StringUtils.isNotEmpty(password)){
            if(user.getPassword().equals(password)){
                ApiData<String> apiData = apiReturn(1, ApiCode.SUCCESS,user.getToken());
                return apiData;
            }else{
                ApiData<String> apiData = apiReturn(0, ApiCode.ACCOUNT_LOGIN_FAILED,null);
                return apiData;
            }
        }else{
            ApiData<String> apiData = apiReturn(0, ApiCode.ACCOUNT_LOGIN_PASSWORD_REQUIRED,null);
            return apiData;
        }
    }

    @RequestMapping(value = "/list")
    public ApiData<Object>list(String id,String name,String stuNo,
                                    @RequestParam(value = "page", defaultValue = "1") int pageNo,
                                    @RequestParam(value = "limit", defaultValue = "10") int limit,
                                    @RequestParam(value = "order", defaultValue = "timestamp desc") String order){
        Page<StudentUser>page = new Page<StudentUser>(pageNo,limit);
        page.setOrderBy(order);
        StudentUser user = new StudentUser();
        user.setName(name);
        user.setStuNo(stuNo);
        if(StringUtils.isNotEmpty(id)){
            user.setId(id);
        }
        page = userService.findUserList(page,user);

        List<Object>list = new ArrayList<Object>();
        for (int i = 0; i < page.getList().size(); i++) {
           StudentUser stu = page.getList().get(i);
            Object obj = new DynamicBean.Builder()
                    .setPV("id",stu.getId())
                    .setPV("name",stu.getName())
                    .setPV("picture", (StringUtils.isEmpty(id)?Global.getQiniuDomainName():"")+stu.getPicture())
                    .setPV("stuNo",stu.getStuNo())
                    .setPV("birthday",stu.getBirthday())
                    .setPV("address",stu.getAddress())
                    .setPV("email",stu.getEmail())
                    .setPV("mobile",stu.getMobile())
                    .setPV("sex",stu.getSex())
                    .setPV("qq",stu.getQq())
                    .setPV("wechat",stu.getWechat())
                    .setPV("createDate",stu.getCreateDate())
                    .build().getObject();
            list.add(obj);
        }
        Object obj = new DynamicBean.Builder().setPV("total",page.getList().size())
                .setPV("students",list).build().getObject();
        return apiReturn(1, ApiCode.SUCCESS,obj);
    }


    @RequiresPermissions("permission:studentUser:add")
    @RequestMapping(value = "/add")
    public ApiData<Object>add(StudentUser user){
        userService.add(user);
        ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
        return apiData;
    }


    @RequiresPermissions("permission:studentUser:del")
    @RequestMapping(value = "/delete")
    public ApiData<Object>delete(String id){
        userService.deleteDataById(id);
        ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
        return apiData;
    }
}