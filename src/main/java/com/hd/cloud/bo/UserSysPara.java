package com.hd.cloud.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: UserSysPara
 * @Description: 用户参数
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年5月2日 上午9:50:01
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSysPara {

	private int id;

	// 内码
	private String internalCode;

	// 参数名称
	private String paraName;

	// 描述
	private String paraDesc;

	// 备注
	private String remark;

	// 值
	private String value;

}
