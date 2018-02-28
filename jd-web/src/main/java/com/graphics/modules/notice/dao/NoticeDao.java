package com.graphics.modules.notice.dao;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Page;
import com.graphics.common.utils.StringUtils;
import com.graphics.modules.notice.entity.Notice;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeDao extends BaseDao<Notice>{


    public Page<Notice> findNoticeList(Page<Notice> page, Notice notice) {
        DetachedCriteria dc = createDetachedCriteria();
        if (StringUtils.isNotEmpty(notice.getTitle())) {
            dc.add(Restrictions.like("title", "%" + notice.getTitle() + "%"));
        }
        if(StringUtils.isNotEmpty(notice.getId())){
            dc.add(Restrictions.eq("id", notice.getId()));
        }
        dc.add(Restrictions.eq(notice.FIELD_DEL_FLAG, notice.DEL_FLAG_NORMAL));
        page = find(page,dc);
        return page;
    }
}
