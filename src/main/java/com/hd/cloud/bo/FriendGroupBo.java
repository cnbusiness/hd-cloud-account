package com.hd.cloud.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: GroupBo
 * @Description: 好友分组bo
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月11日 上午10:51:58
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendGroupBo {

	// 分组ID
	private long friGroupId;

	// 分组名
	private String name;

	// 分组成员数
	private long count;

	// 是否已经加入 这个分组
	private boolean exist;

}
