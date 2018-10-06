package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: MoneyEntityTransVo
 * @Description: 交易记录vo
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年5月2日 上午11:01:20
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyEntityTransVo {

	private long userId;

	private int offset;

	private int pageSize;
}
