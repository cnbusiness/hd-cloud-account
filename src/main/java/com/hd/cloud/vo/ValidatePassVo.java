package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: ValidatePassVo
 * @Description: 验证账号密码vo
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:24:44
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidatePassVo {

	/**
	 * 用户账号（手机号或邮箱）
	 */
	private String useraccount;

	/**
	 * 用户密码
	 */
	private String userpass;

	/**
	 * 1 安卓 2 IOS
	 */
	private int appType;

	/**
	 * 国家码
	 */
	private String countryCode;
}
