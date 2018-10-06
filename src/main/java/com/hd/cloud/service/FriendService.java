package com.hd.cloud.service;

import java.util.List;

import com.hd.cloud.bo.FriendBo;
import com.hd.cloud.redis.bo.FansCount;
import com.hd.cloud.vo.FriendVo;

/**
 * 
 * @ClassName: FriendService
 * @Description: 好友
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月16日 下午4:06:58
 *
 */
public interface FriendService {

	/**
	 * 
	 * @Title: isFollowing
	 * @param:
	 * @Description: 判断是否关注
	 * @return boolean
	 */
	boolean isFollowing(long userId, long targetId);

	/**
	 * 
	 * @Title: attention
	 * @param:
	 * @Description: 关注好友
	 * @return void
	 */
	void attention(long userId, long targetId);

	/**
	 * 
	 * @Title: getRelationship
	 * @param:
	 * @Description: 获取用户的关系
	 * @return String
	 */
	int getRelationship(long userId, long targetId);

	/**
	 * 取消关注
	 *
	 * @param uid
	 *            the uid
	 * @param targetUid
	 *            the target uid
	 */
	void unAttention(Long uid, Long targetUid);

	/**
	 * 
	 * @Title: findAllFriends
	 * @param:
	 * @Description: 获取好友列表
	 * @return List<FansBo>
	 */
	List<FriendBo> findAllFriends(Long uid, Integer page, Integer size);

	/**
	 * 
	 * @Title: findAllFollowing
	 * @param:
	 * @Description: 获取关注的人
	 * @return List<FansBo>
	 */
	List<FriendBo> findAllFollowing(Long userId, Integer page, Integer size);

	/**
	 * 
	 * @Title: findAllFollower
	 * @param:
	 * @Description: 获取粉丝列表
	 * @return List<FansBo>
	 */
	List<FriendBo> findAllFollower(Long userId, Integer page, Integer size);

	/***
	 * 
	 * @Title: saveOrUpdateRemark
	 * @param:
	 * @Description: 添加备注
	 * @return void
	 */
	void saveOrUpdateRemark(long userId, FriendVo vo);

	/**
	 * 
	 * @Title: getFansCount
	 * @param:
	 * @Description: 统计 好友/粉丝/关注 数量
	 * @return FansCount
	 */
	FansCount getFansCount(Long uid);

	/**
	 * 
	 * @Title: findUserByPhone
	 * @param:
	 * @Description: 根据电话查询用户
	 * @return List<UserBo>
	 */
	List<FriendBo> findUserByPhoneBook(List<String> phone, Long userId);

	/**
	 * 
	 * @Title: findAllConditions
	 * @param:
	 * @Description: 搜索用户 昵称/id/手机号
	 * @return List<FriendBo>
	 */
	List<FriendBo> findAllConditions(String countryCode, String condition);
	
	/**
	 * 
	* @Title: addFriend 
	* @param: 
	* @Description: 添加好友
	* @return void
	 */
	void addFriend(long userId, long targetId);

	/**
	 * 
	* @Title: deleteFriend 
	* @param: 
	* @Description: 删除好友
	* @return void
	 */
	void deleteFriend(long userId, long targetId);
}
