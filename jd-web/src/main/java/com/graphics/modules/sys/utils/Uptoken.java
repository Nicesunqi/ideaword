package com.graphics.modules.sys.utils;

import com.graphics.common.config.Global;
import com.qiniu.util.Auth;

public class Uptoken {
	/**
	 * 公有空间Token
	 * @return
	 */
	public final static String makeUptoken() {
		String accessKey = Global.getQiniuAccessKey();
		String secretKey = Global.getQiniuSecretKey();
		String bucket = Global.getQiniuBucket();
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		return upToken;
	}
	
	/**
	 * 私有空间Token
	 * @return
	 */
	public final static String makePrivateUptoken() {
		String accessKey = Global.getQiniuAccessKey();
		String secretKey = Global.getQiniuSecretKey();
		String bucket = Global.getQiniuPrivateBucket();
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		return upToken;
	}
	
	/**
	 * 返回加密时效URL
	 * @param url
	 * @return
	 */
	public static String privateDownloadUrl(String url) {
		String accessKey = Global.getQiniuAccessKey();
		String secretKey = Global.getQiniuSecretKey();
		Auth auth = Auth.create(accessKey, secretKey);
		return auth.privateDownloadUrl(url,Global.getAdminAudioUrlTime());
	}
}