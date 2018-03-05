package com.graphics.modules.msg.web;

import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.msg.entity.Message;
import com.graphics.modules.msg.entity.MsgComment;
import com.graphics.modules.msg.service.MsgCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "${adminPath}/impl/msgComment")
public class MsgCommentController extends BaseController{

    @Autowired
    private MsgCommentService commentService;


    /**
     * 对留言对进评价
     * @param msgId
     * @param content
     * @param commentName
     * @return
     */
    @RequestMapping(value = "/commentMsg")
    public ApiData<Object>commentMsg(String msgId,String content,String commentName){
        MsgComment comment = new MsgComment();
        comment.setMsgId(msgId);
        comment.setContent(content);
        comment.setCommentName(commentName);

        commentService.commentMsg(comment);
        return apiReturn(1, ApiCode.SUCCESS,null);
    }

}
