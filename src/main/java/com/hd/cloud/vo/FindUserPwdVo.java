package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindUserPwdVo {

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 确认密码
	 */
	private String rePassword;

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

	private int appType;

}
