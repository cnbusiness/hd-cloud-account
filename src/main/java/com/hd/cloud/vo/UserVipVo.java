package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: UserVipVo
 * @Description: 用户升级vip
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年5月2日 上午10:09:20
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVipVo {

	// 用户id
	private long userId;

	// 用户支付密码
	private String password;

	// app 类型 1 安卓 2 ios
	private int appType;
}
