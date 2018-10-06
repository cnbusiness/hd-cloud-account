package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: GetSmsVo
 * @Description: 获取sms vo
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年12月7日 下午2:57:53
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetSmsVo {

	private String countryCode;

	private String mobile;

	private int captchaType;
}
