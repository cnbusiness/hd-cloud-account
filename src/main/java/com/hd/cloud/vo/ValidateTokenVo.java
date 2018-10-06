package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: ValidateTokenVo
 * @Description: 验证token
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:24:16
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTokenVo {

	/**
	 * 用户userId
	 */
	private long userId;

	/**
	 * 登录token
	 */
	private String token;

}
