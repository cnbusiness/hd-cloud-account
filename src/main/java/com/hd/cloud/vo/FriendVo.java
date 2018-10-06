package com.hd.cloud.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: FriendVo
 * @Description:
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月11日 上午10:25:42
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendVo {

	// 用户Id
	private long userId;
	// 备注
	private String remark;
	// 手机号
	private List<String> phones;
}
