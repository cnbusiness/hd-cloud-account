package com.hd.cloud.util;

/**
 * 
 * @ClassName: ConstantUtil
 * @Description: 常量字典
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:24:57
 *
 */
public class ConstantUtil {

	// 成功内码
	public final static String SUCCESS = "hlb0000000";

	// 标识 y
	public static final String ACTIVE_FLAG_Y = "y";

	// 标识 d
	public static final String ACTIVE_FLAG_D = "d";

	// 用户登录token redis key
	public static final String TOKEN_REDIS = "token:userid:";

	public static final String LOGIN_USERID = "userId";

	public static final String LOGIN_TOKEN = "token";

	// 验证码常量
	public static final String PASSWORD_KEY = "HLB";

	// 手机注册
	public final static int REG_BY_PHONE = 1;

	// 手机找回密码
	public final static int FORGET_PASSWORD_BY_PHONE = 2;

	// 邮箱找回密码
	public final static int FORGET_PASSWORD_BY_EMAIL = 3;

	// 邮箱绑定账号
	public final static int EMAIL_BINDING = 4;
	
	//手机找回密码
	public final static int FORGET_PAYMENT_PASSWORD_BY_PHONE = 6;

	public static class VerifyCodeStatus {
		// 验证码有效
		public final static int VALID = 0;
		// 验证码无效
		public final static int INVALID = 1;
	}

	public static class PageAll{
		//查询全部，不分页
		public static final int	PAGE_ALL = -1;
		//查询全部，不分页
		public static final int SIZ_ALL = -1;
	}
	
	/**
	 * 好友状态
	 */
	public static class FollowState{
		//没有关系
		public static final int	NONE = 0;  
		//好友状态
		public static final int	FRIEND = 1;
		//粉丝状态
		public static final int	FOLLOWER = 2;
		//关注状态
		public static final int	FOLLOWING = 3;
	}
	
	//升级vip信息
	public final static String SYS_VIP="sys001";
	
}
