package com.graphics.common.config;
import org.springframework.core.env.Environment;
import com.graphics.common.utils.SpringContextHolder;

/**
 * 全局配置类
 */
public class Global {

    private static Environment env = SpringContextHolder.getBean("environment");

	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		String value = env.getProperty(key);
		return value;
	}
	
	/**
	 * 获取七牛域名
	 * @return
	 */
	public static String getQiniuDomainName() {
		return getConfig("qiniuDomainName");
	}
	
	/**
	 * 获取七牛私有空间域名
	 * @return
	 */
	public static String getQiniuPrivateDomainName() {
		return getConfig("qiniuPrivateDomainName");
	}
	
	/**
	 * 获取七牛accessKey
	 * @return
	 */
	public static String getQiniuAccessKey() {
		return getConfig("qiniuAccessKey");
	}
	
	/**
	 * 获取七牛secretKey
	 * @return
	 */
	public static String getQiniuSecretKey() {
		return getConfig("qiniuSecretKey");
	}
	
	/**
	 * 七牛公有空间名
	 * @return
	 */
	public static String getQiniuBucket() {
		return getConfig("qiniuBucket");
	}
	
	/**
	 * 七牛私有有空名
	 * @return
	 */
	public static String getQiniuPrivateBucket() {
		return getConfig("qiniuPrivateBucket");
	}
	
	/**
	 * 获取后台音频Url有效时间 秒
	 * @return
	 */
	public static long getAdminAudioUrlTime() {
		return Long.parseLong(getConfig("adminAudioUrlTime"));
	}
	
	/**
	 * 获得数据库类型，小写
	 * @return
	 */
	public static String getJdbcType(){
		String type = getConfig("jdbc.type");
		type = type.toLowerCase();
		return type;
	}
	
	public static boolean isDebug(){
		String isDebug = getConfig("isDebug");
		if(isDebug!=null&&isDebug.equals("true")){
			return true;
		}
		return false;
	}
	/**
	 * 是否允许跨越访问
	 */
	public static boolean isDifferentDomain(){
		String differentDomain = getConfig("differentDomain");
		if(differentDomain!=null&&differentDomain.equals("true")){
			return true;
		}
		return false;
	}
}
