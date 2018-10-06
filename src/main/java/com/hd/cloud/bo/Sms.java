package com.hd.cloud.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: Sms
 * @Description: sms_verify_record_br 短信
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:15:01
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sms {

	private long id;

	// 国家区号 86
	private String countryCode;

	// 手机号码、邮件
	private String recordAccount;

	// 验证码
	private String code;

	// 1 手机号码注册 2 手机找回密码 3 邮箱找回密码
	private int codeType;

	// 1 已验证 0 未验证
	private int status;

	private String createTime;

	// ynd
	private String activeFlag;

	// checkType 为查询的时间区间类型 1为查询一分钟内 2为查询一天内
	private int checkType;
}
