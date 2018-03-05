package com.graphics.modules.msg.entity;

import com.graphics.common.persistence.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "class_comment")
public class MsgComment extends IdEntity<MsgComment>{

    private String msgId;
    private String content;
    private String commentName;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }
}
