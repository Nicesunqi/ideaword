package com.graphics.modules.msg.web;

import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.msg.entity.SpotFabulous;
import com.graphics.modules.msg.service.SpotFabulouService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "${adminPath}/impl/fabulous")
public class SpotFabulousController extends BaseController{

    @Autowired
    private SpotFabulouService fabulouService;


    @RequestMapping(value = "/spotFabulous")
    public ApiData<Object> spotFabulous(String msgId, String spotName){
        SpotFabulous spotFabulous = new SpotFabulous();
        spotFabulous.setMsgId(msgId);
        spotFabulous.setSpotName(spotName);

        fabulouService.save(spotFabulous);
        return apiReturn(1, ApiCode.SUCCESS,null);
    }

}
