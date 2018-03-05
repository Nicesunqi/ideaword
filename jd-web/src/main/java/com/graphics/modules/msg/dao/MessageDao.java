package com.graphics.modules.msg.dao;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Page;
import com.graphics.common.utils.StringUtils;
import com.graphics.modules.msg.entity.Message;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository
public class MessageDao extends BaseDao<Message>{


    public Page<Message> findMessageList(Page<Message> page, Message msg) {
        DetachedCriteria dc = createDetachedCriteria();
        if(StringUtils.isNotEmpty(msg.getId())){
            dc.add(Restrictions.eq("id", msg.getId()));
        }
        dc.add(Restrictions.eq(msg.FIELD_DEL_FLAG, msg.DEL_FLAG_NORMAL));
        page = find(page,dc);
        return page;
    }
}
