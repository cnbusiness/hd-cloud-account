package com.hd.cloud.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	// 用户id
	private long userId;

	// 昵称
	private String nickName;

	// 性别，1男2女
	private int sex;

	// 用户的状态，1正常 2待激活3冻结
	private int status;

	// 国家区号 86
	private String countryCode;

	// 国际区号+手机号
	private String phone;

	// 密码
	private String password;

	// 生日
	private String birthday;

	// 头像
	private String avatar;

	// 是否会员，1是2否
	private int vipType;

	// ynd
	private String activeFlag;

	private long createBy;

	private long updateBy;
}
