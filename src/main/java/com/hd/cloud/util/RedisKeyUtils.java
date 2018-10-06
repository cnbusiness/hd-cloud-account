package com.hd.cloud.util;

/**
 * 
 * @ClassName: RedisKeyUtils
 * @Description: rediskey的常量
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年12月7日 下午4:45:12
 *
 */
public class RedisKeyUtils {

	/**
	 * The domain
	 */
	static final String USER = "USER:";

	static final String CAPTCHA = "CAPTCHA:";

	static final String USERPROFILE = "USERPROFILE";

	public static String captchaError(String phone, int type) {
		return CAPTCHA + phone + ":" + type + ":error";
	}

	public static String domainId() {
		return USER + "domain:did";
	}

	/**
	 * 
	 * @Title: domain
	 * @param:
	 * @Description: 域名key
	 * @return String
	 */
	public static String domain(long uid) {
		return USER + uid + ":domain";
	}

	public static String userProfile(long userId) {
		return USERPROFILE + userId + ":info";
	}

}
