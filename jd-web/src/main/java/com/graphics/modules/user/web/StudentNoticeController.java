package com.graphics.modules.user.web;

import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.BaseController;
import com.graphics.modules.user.entity.StudentNotice;
import com.graphics.modules.user.entity.StudentUser;
import com.graphics.modules.user.service.StudentNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${adminPath}/impl/notice")
public class StudentNoticeController extends BaseController{

    @Autowired
    private StudentNoticeService noticeService;

    @ModelAttribute
    public StudentNotice get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return noticeService.get(id);
        } else {
            return new StudentNotice();
        }
    }

}
