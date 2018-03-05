package com.graphics.modules.notice.web;

import com.graphics.common.persistence.Page;
import com.graphics.common.utils.DynamicBean;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.notice.entity.Notice;
import com.graphics.modules.notice.entity.Notice;
import com.graphics.modules.notice.service.NoticeService;
import com.graphics.modules.notice.service.NoticeTypeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${adminPath}/impl/notice")
public class NoticeController extends BaseController{

    @Autowired
    private NoticeService service;
    @Autowired
    private NoticeTypeService typeService;

    @ModelAttribute
    public Notice get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return service.get(id);
        } else {
            return new Notice();
        }
    }

    @RequestMapping(value = "/list")
    public ApiData<Object> list(String id, String title,
                                @RequestParam(value = "page", defaultValue = "1") int pageNo,
                                @RequestParam(value = "limit", defaultValue = "10") int limit,
                                @RequestParam(value = "order", defaultValue = "timestamp desc") String order){
        Page<Notice> page = new Page<Notice>(pageNo,limit);
        page.setOrderBy(order);
        Notice type = new Notice();
        type.setTitle(title);
        if(StringUtils.isNotEmpty(id)){
            type.setId(id);
        }
        page = service.findNoticeList(page,type);

        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < page.getList().size(); i++) {
            Notice no = page.getList().get(i);
            String noticeType = typeService.get(no.getNoticeType()).getName();
            String noticeId = typeService.get(no.getNoticeType()).getId();
            Object obj = new DynamicBean.Builder()
                    .setPV("id",no.getId())
                    .setPV("title",no.getTitle())
                    .setPV("notype",noticeType)
                    .setPV("noticeId",noticeId)
                    .setPV("content",no.getContent())
                    .setPV("createDate",no.getCreateDate())
                    .build().getObject();
            list.add(obj);
        }
        Object obj = new DynamicBean.Builder().setPV("total",page.getList().size())
                .setPV("notices",list).build().getObject();
        return apiReturn(1, ApiCode.SUCCESS,obj);
    }

    @RequiresPermissions("permission:notice:add")
    @RequestMapping(value = "/add")
    public ApiData<Object>add(Notice notice){
        service.add(notice);
        ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
        return apiData;
    }

    @RequiresPermissions("permission:notice:del")
    @RequestMapping(value = "/delete")
    public ApiData<Object>delete(String id){
        service.deleteDataById(id);
        ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS, null);
        return apiData;
    }
}
