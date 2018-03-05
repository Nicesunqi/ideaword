package com.graphics.modules.notice.service;

import com.graphics.common.persistence.Page;
import com.graphics.common.service.BaseService;
import com.graphics.modules.notice.dao.NoticeDao;
import com.graphics.modules.notice.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NoticeService extends BaseService {

    @Autowired
    public NoticeDao noticeDao;

    public Notice get(String id) {
        return noticeDao.get(id);
    }

    public Page<Notice> findNoticeList(Page<Notice> page, Notice notice) {
        return noticeDao.findNoticeList(page, notice);
    }

    @Transactional(readOnly = false)
    public void add(Notice notice) {
        noticeDao.save(notice);
    }

    @Transactional(readOnly = false)
    public void deleteDataById(String id) {
        noticeDao.deleteById(id);
    }
}
