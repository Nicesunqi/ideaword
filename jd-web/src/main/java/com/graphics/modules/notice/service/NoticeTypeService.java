package com.graphics.modules.notice.service;

import com.graphics.common.persistence.Page;
import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.notice.dao.NoticeTypeDao;
import com.graphics.modules.notice.entity.NoticeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class NoticeTypeService extends BaseService{

    @Autowired
    private NoticeTypeDao dao;


    public NoticeType get(String id) {
       return dao.get(id);
    }

    public Page<NoticeType> findNoticeTypeList(Page<NoticeType> page, NoticeType type) {
        page = dao.findNoticeTypeList(page,type);
        return page;
    }

    @Transactional(readOnly = false)
    public void add(NoticeType type) {
        if (StringUtils.isEmpty(type.getName())) {
            throw new ServiceException(ApiCode.PERMISSION_NOTICE_TYPE_NAME_NULL);
        }
        dao.save(type);
    }

    @Transactional(readOnly = false)
    public void delete(NoticeType type) {
        dao.deleteById(type.getId());
    }
}
