package com.graphics.modules.upload.web;

import com.graphics.common.config.Global;
import com.graphics.common.utils.DynamicBean;
import com.graphics.common.web.ApiCode;
import com.graphics.common.web.ApiData;
import com.graphics.common.web.BaseController;
import com.graphics.modules.upload.utils.QiniuToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${adminPath}/impl/upload")
public class UploadController extends BaseController{

    @RequestMapping(value = "/token")
    public ApiData<Object> generateToken(){
        String token = QiniuToken.uploadToken();
        Object obj = new DynamicBean.Builder()
                .setPV("token",token)
                .setPV("domain", Global.getQiniuDomainName())
                .build().getObject();
        ApiData<Object> apiData = apiReturn(1, ApiCode.SUCCESS,obj);
        return apiData;
    }

//    public static void main(String[]arg){
//        String token = QiniuToken.uploadToken();
//        System.out.print(token);
//    }
}
