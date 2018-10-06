package com.hd.cloud.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
/**
 * 
  * @ClassName: UpdatePaymentPasswordVo
  * @Description: 修改支付密码
  * @author ShengHao shenghaohao@hadoop-tech.com
  * @Company hadoop-tech 
  * @date 2018年3月16日 上午9:52:48
  *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePaymentPasswordVo {

	// 原密码
	private String oldPassword;

	// 新密码
	private String newPassWord;

	// 确认密码
	private String confirmPassword;
	
	//1 用户钱包 2 商家钱包
	private int type;
	
	private int appType;
}
