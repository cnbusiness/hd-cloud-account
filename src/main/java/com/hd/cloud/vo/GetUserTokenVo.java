package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: GetUserTokenVo
 * @Description: 获取用户token
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年12月7日 下午3:22:32
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserTokenVo {

	private long userId;
}
