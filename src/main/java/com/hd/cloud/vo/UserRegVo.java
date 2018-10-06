package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: UserRegVo
 * @Description: 注册
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月4日 下午3:21:14
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegVo {

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 验证码
	 */
	private String captcha;

	/**
	 * 手机号
	 */
	private String phoneNo;

	/**
	 * 国家码
	 */
	private String countryCode;

	/**
	 * 应用类型： 1=安卓，2=IOS
	 */
	private int appType;
}
