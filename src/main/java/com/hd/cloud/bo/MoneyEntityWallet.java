package com.hd.cloud.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: MoneyEntityWallet
 * @Description: 实体钱包
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月17日 下午4:52:32
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyEntityWallet {

	// 用户钱包账户ID
	private String account;

	// 用户id/店铺id
	private long walletId;

	// 钱包类型： 1用户 2商家
	private int type;

	// 支付密码加密
	private String payPassword;

	// 密保问题加密
	private String question;

	// 密保答案加密
	private String answer;

	// 有效阿萨石数量
	private double validCoinCnt;

	// 冻结阿萨石数量
	private double freezeCoinCnt;

	// 游戏点数量
	private double gameCnt;

	// 1正常2冻结
	private int status;

	private long createBy;

	private long updateBy;

	private String activeFlag;

}
