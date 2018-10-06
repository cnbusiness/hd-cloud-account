package com.hd.cloud.vo;

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
public class FriendGroupVo {

	// 分组Id
	private long groupId;
	// 分组名称
	private String groupName;
	// 好友id数组
	private Long[] friendsId;
	// 用户Id
	private Long friendId;
	// 组id数组
	private Long[] groupsId;
}
