package com.graphics.modules.msg.dao;

import com.graphics.common.persistence.BaseDao;
import com.graphics.common.persistence.Page;
import com.graphics.common.utils.StringUtils;
import com.graphics.modules.msg.entity.MsgComment;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class MsgCommentDao extends BaseDao<MsgComment>{


    public Page<MsgComment> getCommentById(Page<MsgComment> page, String id) {
        DetachedCriteria dc = createDetachedCriteria();
        if(StringUtils.isNotEmpty(id)){
            dc.add(Restrictions.eq("msgId", id));
        }
        dc.add(Restrictions.eq(MsgComment.FIELD_DEL_FLAG, MsgComment.DEL_FLAG_NORMAL));
        page = find(page,dc);
        return page;
    }
}
