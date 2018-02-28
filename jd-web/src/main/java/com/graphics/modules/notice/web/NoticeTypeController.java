package com.graphics.modules.notice.web;

import com.graphics.common.config.Global;
import com.graphics.common.persistence.Page;
import com.graphics.common.utils.DynamicBean;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.notice.entity.NoticeType;
import com.graphics.modules.notice.service.NoticeTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("${adminPath}/impl/notype")
public class NoticeTypeController extends BaseController{

    @Autowired
    private NoticeTypeService service;


    @ModelAttribute
    public NoticeType get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return service.get(id);
        } else {
            return new NoticeType();
        }
    }

    @RequestMapping(value = "/list")
    public ApiData<Object> list(String id, String name,
                                @RequestParam(value = "page", defaultValue = "1") int pageNo,
                                @RequestParam(value = "limit", defaultValue = "10") int limit,
                                @RequestParam(value = "order", defaultValue = "timestamp desc") String order){
        Page<NoticeType> page = new Page<NoticeType>(pageNo,limit);
        page.setOrderBy(order);
        NoticeType type = new NoticeType();
        type.setName(name);
        if(StringUtils.isNotEmpty(id)){
            type.setId(id);
        }
        page = service.findNoticeTypeList(page,type);

        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < page.getList().size(); i++) {
            NoticeType no = page.getList().get(i);
            Object obj = new DynamicBean.Builder()
                    .setPV("id",no.getId())
                    .setPV("name",no.getName())
                    .setPV("createDate",no.getCreateDate())
                    .build().getObject();
            list.add(obj);
        }
        Object obj = new DynamicBean.Builder().setPV("total",page.getList().size())
                .setPV("noticeType",list).build().getObject();
        return apiReturn(1, ApiCode.SUCCESS,obj);
    }

    @RequiresPermissions("permission:noticeType:add")
    @RequestMapping(value = "/add")
    public ApiData<Object>add(NoticeType type){
        service.add(type);
        ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
        return apiData;
    }

    @RequiresPermissions("permission:noticeType:delete")
    @RequestMapping(value = "/delete")
    public ApiData<Object>delete(NoticeType type){
        service.delete(type);
        ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
        return apiData;
    }
}
