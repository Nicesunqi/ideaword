package com.graphics.common.web;

public enum ApiCode {
	//String password,String newPassword,String verifyNewPassword
	SUCCESS("0001", "成功"), 
	UNKNOWN_ERROR("1000", "系统繁忙，请稍后再试...."),
	UNCAUGHT_EXCEPTION("1002","未捕获异常...."),
	
	ACCOUNT_LOGIN_ACCOUNT_REQUIRED("0101","用户名不能为空！"), 
    ACCOUNT_LOGIN_PASSWORD_REQUIRED("0102","密码不能为空！"),
    ACCOUNT_LOGIN_FAILED("0103","用户名或密码错误，登录失败！"),
    ACCOUNT_EDIT_PASSWORD_REQUIRED("0104","请输入原密码！"), 
    ACCOUNT_EDIT_NEWPASSWORD_REQUIRED("0105","请输入新密码！"), 
    ACCOUNT_EDIT_VERIFYNEWPASSWORD_REQUIRED("0106","请确认密码！"),
    ACCOUNT_EDIT_PASSWORD_LENGTH("0107","密码过长！"),
    ACCOUNT_EDIT_PASSWORD_FORMAT("0108","密码格式不正确！"),
    ACCOUNT_EDIT_PASSWORD_VERIFY("0109","原密码错误!"),
    ACCOUNT_EDIT_PASSWORDS_VERIFY("0110","两次密码输入不相同!"),
    
	PERMISSION_USER_ACCOUNT_EXIST("0202","用户账号已经存在，请更改!"), 
    PERMISSION_USER_NICKNAME_REQUIRED("0203","用户名称不能为空!"),
    PERMISSION_USER_EMAIL_REQUIRED("0204","用户密码不能为空!"),
    PERMISSION_USER_OFFICE_REQUIRED("0205","用户机构不能为空!"),
    PERMISSION_USER_PASSWORD_REQUIRED("0206","用户密码不能为空！"),
    PERMISSION_USER_ACCOUNT_REQUIRED("0207","用户帐号不能为空！"),
	PERMISSION_NOTICE_TYPE_NAME_NULL("0208","公告类型不能为空！"),

	MESSAGE_CONTENT_IS_NOT_NULL("0209","留言内容不能为空"),
	MESSAGE_STUNO_IS_NOT_NULL("0209","学号不能为空"),

	MSG_COMMENT_ID_NOT_NULL("0210","留言ID不能为空"),
	MSG_COMMENT_CONTENT_NOT_NULL("0211","评论内容不能为空"),
	MSG_COMMENT_NAME_NOT_NULL("0212","评论人不能为空"),
	SPOT_COMMENT_NAME_NOT_NULL("0213","点赞人不能为空"),


	PERMISSION_USER_NOTEXIST("0201", "用户不存在"),
	PERMISSION_MENU_ID_REQUIRED("0221","菜单ID不能为空！"),
    PERMISSION_MENU_NAME_REQUIRED("0222","菜单名称不能为空！"),
    PERMISSION_MENU_CODE_REQUIRED("0223","菜单权限编码不能为空！"), 
    PERMISSION_MENU_NOT_EXIST("0224","菜单不存在！"), 
    PERMISSION_ROLE_NOTEXIST("0211","角色不存在！"), 
    PERMISSION_ROLE_NAME_REQUIRED("0212","角色名称不能为空！"),
	PERMISSION_OFFICE_NAME_REQUIRED("0232","部门名称不能为空!"),
	PERMISSION_OFFICE_ID_REQUIRED("0231","部门ID不能为空！"),
	PERMISSION_OFFICE_NOT_EXIST("0234","部门不存在！"),
	PERMISSION_USER_ACCOUNT_REGEX("0238","长度为 6 到 16 个字母或数字!"),
	PERMISSION_USER_PASSWORD_REGEX("0240","长度为 6 到 16 个字母或数字!"),
	PERMISSION_USER_NICKNAME_LENGTH("0239","长度为 20 字符以内!"),
	PERMISSION_USER_EMAIL_FORMAT("0241","请输入正确的邮箱!"),
	PERMISSION_USER_MOBILE_REQUIRED("0242","手机号不能为空!"),
	PERMISSION_USER_MOBILE_FORMAT("0243","手机号格式错误!"),
	PERMISSION_USER_PASSWORD_VERIFY("0247","请确认密码!"),
	PERMISSION_USER_PASSWORD_INCONFORMITY("0248","两次密码输入不一致!"),
	PERMISSION_DICT_LABEL_VERIFY("0249","标签必填且在100字符以内"),
	PERMISSION_DICT_VALUE_VERIFY("0250","键值	必填且在100字符以内"),
	PERMISSION_DICT_TYPE_VERIFY("0251","类型必填且在100字符以内"),

	PERMISSION_USER_STU_NO_REQUIRED("01001","用户学号不能为空!"),
	PERMISSION_USER_ADDRESS_REQUIRED("01002","用户家庭住址不能为空!"),
	PERMISSION_USER_EMIAL_REQUIRED("01003","用户邮箱不能为空!"),
	PERMISSION_USER_EMIAL_PATTERN_REQUIRED("01004","用户邮箱格式不能为空!"),
	PERMISSION_USER_WECHAT_REQUIRED("01005","用户微信不能为空!"),
	PERMISSION_USER_WECHAT_PATTERN_REQUIRED("01006","用户微信格式不能为空!"),
	PERMISSION_USER_QQ_REQUIRED("01007","用户微信不能为空!"),
	PERMISSION_USER_QQ_PATTERN_REQUIRED("01008","用户微信格式不能为空!"),
	PERMISSION_USER_SEX_REQUIRED("01009","性别不能为空!"),
	
	CAROUSEL_ID_REQUIRED("0301","轮播图ID不能为空"),
	CAROUSEL_URL_REQUIRED("0302","轮播图地址不能为空"),
	CAROUSEL_TITLE_REQUIRED("0303","轮播图标题长度在 1 到 50 个字符"),
	
	INFO_CONFIG_NAME_VERIFY("0401","参数名必填且在50字符以内"),
	INFO_CONFIG_TYPE_VERIFY("0402","参数类型不存在或为空"),
	INFO_CONFIG_KEY_VERIFY("0403","参数Key必填且在50字符以内"),
	INFO_CONFIG_KEY_EXIST("0410","参数Key已存在"),
	INFO_CONFIG_VALUE_VERIFY("0404","参数值必填且在100字符以内"),
	INFO_CONFIG_DEF_VERIFY("0405","默认值长度在100字符以内"),
	INFO_CONFIG_REMARK_VERIFY("0406","描述长度在255字符以内"),
	INFO_AGREEMENT_TYPE_VERIFY("0407","类型不存在或者为空"),
	INFO_CATEGORY_NAME_VERIFY("0408","专业分类名称必填且在50字符以内"),
	INFO_SPECIALTY_CATEGORY_NONENTITY("0410","专业分类不存在"),
	INFO_SPECIALTY_NAME_VERIFY("0409","专业名称必填且在50字符以内"),
	INFO_SPECIALTY_REQUIRED("0410","请选择专业"),
	
	INFO_TRACK_ANME_VERIFY("0501","伴奏曲谱名称必填且在50字符以内"),
	INFO_TRACK_ALEPH_VERIFY("0502","伴奏曲谱名称第一个字母必填且为1字符"),
	INFO_TRACK_PY_VERIFY("0503","伴奏名称拼音首字母必填且在50字符以内"),
	INFO_TRACK_PINYIN_VERIFY("0504","伴奏名称全拼必填且在100字符以内"),
	INFO_TRACK_ID_REQUIRED("0505","ID为空"),
	INFO_TRACKPICTURE_IDS_REQUIRED("0506","无图片信息"),
	
	INFO_NEWS_TITLE_VERIFY("0601","资讯标题必填且在255字符以内"),
	INFO_NEWS_CATEGORY_VERIFY("0602","资讯类型必选"),
	INFO_NEWS_CATEGORY_EXIST("0603","资讯类型不存在"),
	INFO_NEWS_COVERURL_VERIFY("0604","封面图片必传"),
	INFO_NEWS_COVERURL_LENGTH("0605","封面图片URL过长"),
	INFO_NEWS_CONTENT_REQUIRED("0606","内容不能为空"),
	INFO_NEWS_ISTOP_REQUIRED("0607","是否置顶必选"),
	INFO_NEWS_ISTOP_LENGTH("0608","是否置顶字段超长"),
	
	INFO_VERSION_NO_LENGTH("0701","版本号必填且在10字符以内"),
	INFO_VERSION_CLIENT_VERIFY("0702","请选择客户端类型"),
	INFO_VERSION_OSTYPE_VERIFY("0703","请选择操作系统类型"),
	
	INFO_APPUSER_NAME_REQUIRED("0801","用户名必填且小于40字符"),
	INFO_APPUSER_MOBILE_REQUIRED("0802","手机号码必填"),
	INFO_APPUSER_MOBILE_FORMAT("0803","手机号码格式错误"),
	INFO_APPUSER_MOBILE_EXIST("0804","手机号码已存在"),
	INFO_APPUSER_PASSWORD_REQUIRED("0805","密码必填且小于20字符"),
	INFO_APPUSER_GENDER_REQUIRED("0806","性别必选"),
	INFO_APPUSER_CLIENT_TYPE("0807","客户端类型必选"),
	INFO_APPUSER_TYPE_REQUIRED("0807","用户类型必选"),
	INFO_APPUSER_DEVELOPER_REQUIRED("0807","是否为开发者必选"),
	
	FINANCE_ORDER_EXPORT_ERROR("0906","导出失败"),
	
	INFO_VIDEO_TITLE_REQUIRED("1011","标题必填且小于100字符"),
	INFO_VIDEO_COLUMN_REQUIRED("1012","请选择栏目类型"),
	INFO_VIDEO_COLUMN_NOT_EXIST("1013","无此栏目类型"),
	INFO_VIDEO_VIDEO_URL_REQUIRED("1014","请上传视频"),
	INFO_COLUMN_NAME_REQUIRED("1015","栏目名称必填且在50字符以内"),
	
	INFO_TEACHER_NOT_EXIST("1016","教师信息不存在!"),
	
	ORDER_IS_NOT_EXIST("1100","订单不存在"),
	ORDER_COURSE_PRICE_IS_NULL("1101","课程价格为空"),
	WX_REFUND_XML_GENERATE_ERROR("1200","拼接微信退款XML异常"),
	WX_REFUND_REQUEST_EXCEPTION("1201","微信退款请求异常"),
	WX_REFUND_XML_PARSE_EXCEPTION("1202","解析退款产生的XML字符串异常"),
	WX_REFUND_FAILURE("1203","退款申请失败"),
	WX_REFUND_IS_SUCCEED("1203","该笔款项已全额退款,请勿继续申请!"),
	WX_REFUND_IS_UNDERWAY("1203","退款正在处理中,请勿重复申请!"),
	WX_REFUND_CALLBACK_EXCEPTION("1204","微信退款回调异常"),
	WX_REFUND_YET_CALLBACK("1205","微信退款回调已处理"),
	WX_REFUND_CALLBACK_DATA_EXCEPTION("1206","微信退款回调数据验证不通过"),
	
	COURSE_IS_NOT_EXIST("1300","课程不存在"),
	COURSE_IS_NOT_TIMEOUT("1301","课程未超时,不能退款"),
	
	PLEASE_INPUT_SORT_FIELD("1401","请输入排序字段");
	
	
	private String code;
	private String msg;

	private ApiCode(String code, String msg) {
		this.setCode(code);
		this.setMsg(msg);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "[" + this.code + "]" + this.msg;
	}
}
