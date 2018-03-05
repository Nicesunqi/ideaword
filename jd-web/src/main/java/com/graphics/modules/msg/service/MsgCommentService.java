package com.graphics.modules.msg.service;

import com.graphics.common.persistence.Page;
import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.msg.dao.MsgCommentDao;
import com.graphics.modules.msg.entity.MsgComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class MsgCommentService extends BaseService{

    @Autowired
    private MsgCommentDao dao;

    @Transactional(readOnly = false)
    public void commentMsg(MsgComment comment) {
        if (StringUtils.isEmpty(comment.getContent())) {
            throw new ServiceException(ApiCode.MSG_COMMENT_CONTENT_NOT_NULL);
        }else if (StringUtils.isEmpty(comment.getMsgId())) {
            throw new ServiceException(ApiCode.MSG_COMMENT_ID_NOT_NULL);
        }else if (StringUtils.isEmpty(comment.getMsgId())) {
            throw new ServiceException(ApiCode.MSG_COMMENT_NAME_NOT_NULL);
        }
        dao.save(comment);
    }

    /**
     * 留言ID
     * @param id
     * @return
     */
    public Page<MsgComment>getCommentById(String id){
       return dao.getCommentById(new Page<MsgComment>(),id);
    }
}
