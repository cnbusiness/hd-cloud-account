package com.hd.cloud.bo;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: MoneyEntityTrans
 * @Description: 交易记录
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年5月2日 上午10:56:45
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyEntityTrans {

	// id
	private int id;

	// 交易类型，1扣款，2充值
	private int type;

	// 内码
	private String transInternalCode;

	// 交易form方
	private long transFromId;

	// 交易to方
	private long transToId;

	// 交易金额
	private double amt;

	// 状态,1成功，2失败
	private int status;

	// 描述
	private String desc;

	private long createBy;

	private long updateBy;

	private String activeFlag;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	private Timestamp createTime;// 开始时间
}
