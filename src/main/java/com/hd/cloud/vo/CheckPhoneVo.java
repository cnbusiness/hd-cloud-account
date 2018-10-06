package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: CheckPhoneVo
 * @Description: 发送验证码vo
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月4日 下午4:53:04
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckPhoneVo {

	private String countryCode;

	private String phoneNo;

	private int type;

}
