package com.graphics.modules.user.service;

import com.graphics.common.service.BaseService;
import com.graphics.modules.user.dao.StudentNoticeDao;
import com.graphics.modules.user.entity.StudentNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StudentNoticeService extends BaseService{

    @Autowired
    public StudentNoticeDao noticeDao;

    public StudentNotice get(String id) {
        return noticeDao.get(id);
    }
}
