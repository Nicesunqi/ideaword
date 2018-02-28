package com.graphics.modules.notice.dao;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Page;
import com.graphics.common.utils.StringUtils;
import com.graphics.modules.notice.entity.NoticeType;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeTypeDao extends BaseDao<NoticeType>{


    public Page<NoticeType> findNoticeTypeList(Page<NoticeType> page, NoticeType type) {
        DetachedCriteria dc = createDetachedCriteria();
        if (StringUtils.isNotEmpty(type.getName())) {
            dc.add(Restrictions.like("name", "%" + type.getName() + "%"));
        }
        if(StringUtils.isNotEmpty(type.getId())){
            dc.add(Restrictions.eq("id", type.getId()));
        }
        dc.add(Restrictions.eq(type.FIELD_DEL_FLAG, type.DEL_FLAG_NORMAL));
        page = find(page,dc);
        return page;
    }
}
