package com.hd.cloud.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: FriendBo
 * @Description: 好友Bo
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月11日 下午2:45:08
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendBo {

	// 用户ID
	private long userId;

	// 昵称
	private String name;

	// 头像
	private String photoUrl;

	// 性别
	private int sex;

	// 备注
	private String remark;

	// 是否互为好友状态
	private int followState;

}
