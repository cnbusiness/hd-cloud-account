package com.hd.cloud.service;

import java.util.List;
import java.util.Map;

import com.hd.cloud.bo.FriendBo;
import com.hd.cloud.bo.FriendGroupBo;
import com.hd.cloud.vo.FriendGroupVo;

/**
 * 
 * @ClassName: FriendGroupService
 * @Description: 好友分组
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月13日 上午9:51:45
 *
 */
public interface FriendGroupService {

	/**
	 * 
	 * @Title: addGroup
	 * @param:
	 * @Description: 创建好友分组
	 * @return FriendGroupBo
	 */
	FriendGroupBo addGroup(Long uid, FriendGroupVo friendGroupVo);

	/**
	 * 
	 * @Title: isGroupNameExist
	 * @param:
	 * @Description: 判断分组名是否存在
	 * @return boolean
	 */
	boolean isGroupNameExist(long userId, String groupName);

	/**
	 * 
	 * @Title: updateGroup
	 * @param:
	 * @Description: 编辑分组名称
	 * @return FriendGroupBo
	 */
	FriendGroupBo updateGroup(Long uid, FriendGroupVo friendGroupVo);

	/***
	 * 
	 * @Title: isGroupExist
	 * @param:
	 * @Description: 判断分组是否存在
	 * @return boolean
	 */
	boolean isGroupExist(Long uid, Long gid);

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
	 * @Description: 删除分组
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
	 * @Title: findNotInCurrentGroupFriends
	 * @param:
	 * @Description: 查询没有添加到此分组的好友
	 * @return List<FriendBo>
	 */
	List<FriendBo> findNotInCurrentGroupFriends(Long uid, Long gid);

	/**
	 * 
	 * @Title: getGroupFriends
	 * @param:
	 * @Description: 获取分组内好友
	 * @return List<FriendBo>
	 */
	List<FriendBo> getGroupFriends(Long uid, Long gid);

	/**
	 * 
	 * @Title: addFriendsToMultipleGroup
	 * @param:
	 * @Description: 添加好友到多个分组
	 * @return void
	 */
	void addFriendsToMultipleGroup(Long uid, Long[] gids, Long fid);

	/**
	 * 
	 * @Title: findFriendsInGroups
	 * @param:
	 * @Description: 查询好友已添加的组
	 * @return List<FriendGroupBo>
	 */
	List<FriendGroupBo> findFriendsInGroups(Long uid, Long fid);

	/**
	 * 
	 * @Title:
	 * @param:
	 * @Description: 查询该分组的好友
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
	List<Long> findNotInCurrentGroupFriendIds(Long uid, Long gid);

	/**
	 * 
	 * @Title: getAllGroupFriendsCount
	 * @param:
	 * @Description: 所有分组人数
	 * @return Map<Long,Long>
	 */
	Map<Long, Long> getAllGroupFriendsCount(Long uid);

	/**
	 * 
	 * @Title: getOneGroupFriendsCount
	 * @param:
	 * @Description: 单个分组人数
	 * @return Map<Long,Long>
	 */
	long getOneGroupFriendsCount(Long uid, Long gid);

	/**
	 * 
	 * @Title: isFriends
	 * @param:
	 * @Description: 是否好友
	 * @return boolean
	 */
	boolean isFriends(Long userId, Long friendIds);
}
