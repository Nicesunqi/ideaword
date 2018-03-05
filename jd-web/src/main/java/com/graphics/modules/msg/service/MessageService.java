package com.graphics.modules.msg.service;

import com.graphics.common.persistence.Page;
import com.graphics.common.service.BaseService;
import com.graphics.common.service.ServiceException;
import com.graphics.common.utils.IdGen;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.modules.msg.dao.MessageDao;
import com.graphics.modules.msg.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Transactional(readOnly = true)
public class MessageService extends BaseService{

    @Autowired
    private MessageDao dao;

    public Message get(String id) {
        return dao.get(id);
    }

    public Page<Message> findMessageList(Page<Message> page, Message msg) {
        return dao.findMessageList(page,msg);
    }

    @Transactional(readOnly = false)
    public void addMessage(Message message) {
        if (StringUtils.isEmpty(message.getContent())) {
            throw new ServiceException(ApiCode.MESSAGE_CONTENT_IS_NOT_NULL);
        }else if(StringUtils.isEmpty(message.getCreateName())) {
            throw new ServiceException(ApiCode.MESSAGE_STUNO_IS_NOT_NULL);
        }

//        Date now = new Date();
//        String id = IdGen.uuid();
//        message.setCreateDate(now);
//        message.setId(id);
        dao.save(message);
    }
}
