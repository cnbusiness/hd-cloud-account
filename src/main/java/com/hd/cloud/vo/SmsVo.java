package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: SmsVo
 * @Description: 业务短信
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午2:59:43
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsVo {

	// 电话
	private String mobile;

	// 短信内容
	private String message;
}
