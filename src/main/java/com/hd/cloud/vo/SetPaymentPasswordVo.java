package com.hd.cloud.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: PaymentVo
 * @Description: 设置支付密码Vo
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年3月16日 上午10:16:32
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SetPaymentPasswordVo {

	private String password;

	private String confirmPassword;

	// 1 用户钱包 2 商家钱包
	private int type;

	// 1 安卓 2 ios
	private int appType;
}
