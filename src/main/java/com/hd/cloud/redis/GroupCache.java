package com.hd.cloud.redis;

import java.util.List;
import java.util.Map;

import com.hd.cloud.bo.FriendGroupBo;
import com.hd.cloud.vo.FriendGroupVo;

/**
 * 
 * @ClassName: GroupCache
 * @Description: 好友分组
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月13日 下午2:33:08
 *
 */
public interface GroupCache {

	/**
	 * 
	 * @Title: saveGroup
	 * @param:
	 * @Description: 创建好友分组
	 * @return FriendGroupBo
	 */
	FriendGroupBo saveGroup(Long uid, FriendGroupVo friendGroupVo);

	/**
	 * 
	 * @Title: isGroupNameExist
	 * @param:
	 * @Description: 判断分组名是否存在
	 * @return boolean
	 */
	boolean isGroupNameExist(Long uid, String name);

	/**
	 * 
	 * @Title: isGroupExist
	 * @param:
	 * @Description: 判断分组是否存在
	 * @return boolean
	 */
	boolean isGroupExist(Long uid, Long gid);

	/**
	 * 
	 * @Title: updateGroup
	 * @param:
	 * @Description: 编辑分组名
	 * @return FriendGroupBo
	 */
	FriendGroupBo updateGroup(Long uid, FriendGroupVo friendGroupVo);

	/**
	 * 
	 * @Title: findOne
	 * @param:
	 * @Description: 获取分组详情
	 * @return FriendGroupBo
	 */
	FriendGroupBo findOne(Long uid, Long gid);

	/**
	 * 
	 * @Title: findAll
	 * @param:
	 * @Description: 分组列表
	 * @return List<FriendGroupBo>
	 */
	List<FriendGroupBo> findAll(Long uid);

	/**
	 * 
	 * @Title: deleteGroup
	 * @param:
	 * @Description: 删除好友分组
	 * @return void
	 */
	void deleteGroup(Long uid, Long gid);

	/**
	 * 
	 * @Title: addFriendsToGroup
	 * @param:
	 * @Description: 分组添加好友
	 * @return boolean
	 */
	boolean addFriendsToGroup(Long uid, Long gid, Long[] fids);

	/**
	 * 
	 * @Title: removeFriendsFromGroup
	 * @param:
	 * @Description: 分组移除好友
	 * @return boolean
	 */
	boolean removeFriendsFromGroup(Long uid, Long gid, Long[] fids);

	/**
	 * 
	 * @Title: isFriends
	 * @param:
	 * @Description: 是否好友
	 * @return boolean
	 */
	boolean isFriends(Long uid, Long[] fids);

	/**
	 * 
	 * @Title: isGroupFriends
	 * @param:
	 * @Description: 查询是否在此分组
	 * @return boolean
	 */
	boolean isGroupFriends(Long uid, Long gid, Long[] fids);

	/**
	 * 
	 * @Title: findGroupFriends
	 * @param:
	 * @Description:查询分组好友
	 * @return List<Long>
	 */
	List<Long> findGroupFriends(Long uid, Long gid);

	/**
	 * 
	 * @Title: findNotInCurrentGroupFriends
	 * @param:
	 * @Description: 查询不在该分组的好友
	 * @return List<Long>
	 */
	List<Long> findNotInCurrentGroupFriends(Long uid, Long gid);

	/**
	 * 
	 * @Title: deleteFriendsFromAllGroup
	 * @param:
	 * @Description: 移除好友所有分组
	 * @return void
	 */
	void deleteFriendsFromAllGroup(Long uid, Long fid);

	/**
	 * 
	 * @Title: getAllGroupFriendsCount
	 * @param:
	 * @Description: 获取所有分组人数
	 * @return Map<Long,Long>
	 */
	Map<Long, Long> getAllGroupFriendsCount(Long uid);

	/**
	 * 
	 * @Title: getOneGroupFriendsCount
	 * @param:
	 * @Description: 获取单个分组人数
	 * @return Map<Long,Long>
	 */
	long getOneGroupFriendsCount(Long uid, Long gid);

}
