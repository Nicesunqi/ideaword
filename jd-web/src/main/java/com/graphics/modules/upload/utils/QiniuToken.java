package com.graphics.modules.upload.utils;

import com.graphics.common.config.Global;
import com.qiniu.util.Auth;

public class QiniuToken {

    public static String uploadToken(){
        Auth auth = Auth.create(Global.getQiniuAccessKey(), Global.getQiniuSecretKey());
        return auth.uploadToken(Global.getQiniuBucket());
    }

}
