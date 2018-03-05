package com.graphics.modules.msg.web;

import com.graphics.common.persistence.Page;
import com.graphics.common.utils.DynamicBean;
import com.graphics.common.utils.StringUtils;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.msg.entity.Message;
import com.graphics.modules.msg.entity.MsgComment;
import com.graphics.modules.msg.entity.SpotFabulous;
import com.graphics.modules.msg.service.MessageService;
import com.graphics.modules.msg.service.MsgCommentService;
import com.graphics.modules.msg.service.SpotFabulouService;
import com.graphics.modules.notice.entity.Notice;
import com.graphics.modules.sys.entity.User;
import com.graphics.modules.sys.service.UserService;
import com.graphics.modules.user.entity.StudentUser;
import com.graphics.modules.user.service.StudentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("${adminPath}/impl/message")
public class MessageController extends BaseController{

    @Autowired
    private MessageService service;
    @Autowired
    private StudentUserService userService;
    @Autowired
    private MsgCommentService commentService;


    @ModelAttribute
    public Message  get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return service.get(id);
        } else {
            return new Message();
        }
    }

    @RequestMapping(value = "/list")
    public ApiData<Object> list(String id,
                                @RequestParam(value = "page", defaultValue = "1") int pageNo,
                                @RequestParam(value = "limit", defaultValue = "10") int limit,
                                @RequestParam(value = "order", defaultValue = "timestamp desc") String order){
        Page<Message> page = new Page<Message>(pageNo,limit);
        page.setOrderBy(order);
        Message msg = new Message();
        if(StringUtils.isNotEmpty(id)){
            msg.setId(id);
        }
        page = service.findMessageList(page,msg);

        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < page.getList().size(); i++) {
            Message no = page.getList().get(i);
           StudentUser user = userService.getUserByStuNo(no.getCreateName());
           int commentSize = commentService.getCommentById(no.getId()).getList().size();
            Object obj = new DynamicBean.Builder()
                    .setPV("id",no.getId())
                    .setPV("content",no.getContent())
                    .setPV("createDate",no.getCreateDate())
                    .setPV("commentSize",commentSize)
                    .setPV("name",user == null?"":user.getName())
                    .build().getObject();
            list.add(obj);
        }
        Object obj = new DynamicBean.Builder().setPV("total",page.getList().size())
                .setPV("messages",list).build().getObject();
        return apiReturn(1, ApiCode.SUCCESS,obj);
    }



    /**
     * 新增留言
     * @param content
     * @param StuNo
     * @return
     */
    @RequestMapping(value = "/addMessage")
    public ApiData<Object>add( @RequestParam(value = "content", defaultValue = "") String content,
                               @RequestParam(value = "stuNo", defaultValue = "") String StuNo){
        Message msg = new Message();
        msg.setContent(content);
        msg.setCreateName(StuNo);
        service.addMessage(msg);
        return apiReturn(1, ApiCode.SUCCESS,null);
    }



}
