package com.hd.cloud.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: City
 * @Description: 城市
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:44:47
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {

	private long cityId;

	// 国家码
	private String countryCode;

	// 名称
	private String cityName;
}
