package com.hd.cloud.redis;

import java.util.HashMap;
import java.util.List;

import com.hd.cloud.redis.bo.FansCount;

public interface FansCache {

	/**
	 * 
	 * @Title: isFollowing
	 * @param:
	 * @Description: 是否关注
	 * @return boolean
	 */
	boolean isFollowing(Long uid, Long targetUid);

	/**
	 * 
	 * @Title: saveFans
	 * @param:
	 * @Description: 关注
	 * @return void
	 */
	void saveFans(Long uid, Long targetUid);

	/**
	 * 
	 * @Title: isFollowers
	 * @param:
	 * @Description: 是否被关注
	 * @return boolean
	 */
	boolean isFollowers(Long uid, Long targetUid);

	/**
	 * 
	 * @Title: isFriends
	 * @param:
	 * @Description: 是否是好友
	 * @return boolean
	 */
	boolean isFriends(Long uid, Long targetUid);

	/**
	 * 
	 * @Title: removeFans
	 * @param:
	 * @Description: 取消关注
	 * @return void
	 */
	void removeFans(Long uid, Long targetUid);

	/**
	 * 
	 * @Title: addRemark
	 * @param:
	 * @Description: 添加备注
	 * @return void
	 */
	void addRemark(Long uid, Long targetUid, String remarkStr);

	/**
	 * 
	 * @Title: hasRemark
	 * @param:
	 * @Description: 是否有备注
	 * @return boolean
	 */
	boolean hasRemark(Long uid, Long fid);

	/**
	 * 
	 * @Title: updateRemark
	 * @param:
	 * @Description: 修改备注
	 * @return void
	 */
	void updateRemark(Long uid, Long fid, String remarkStr);

	/**
	 * 
	 * @Title: removeRemark
	 * @param:
	 * @Description: 移除备注
	 * @return void
	 */
	void removeRemark(Long uid, Long targetUid);

	/**
	 * 
	 * @Title: getAllRemark
	 * @param:
	 * @Description: 获取好友备注
	 * @return HashMap<Long,String>
	 */
	HashMap<Long, String> getAllRemark(Long uid);

	/**
	 * 
	 * @Title: findAllFriends
	 * @param:
	 * @Description: 好友列表
	 * @return List<Long>
	 */
	List<Long> findAllFriends(Long uid, Integer page, Integer size);

	/**
	 * 
	 * @Title: findAllFollowing
	 * @param:
	 * @Description: 关注人列表
	 * @return List<Long>
	 */
	List<Long> findAllFollowing(Long userId, Integer page, Integer size);

	/**
	 * 
	 * @Title: findAllFollower
	 * @param:
	 * @Description: 粉丝列表
	 * @return List<Long>
	 */
	List<Long> findAllFollower(Long userId, Integer page, Integer size);

	/**
	 * 
	 * @Title: getFansCount
	 * @param:
	 * @Description: 获取好友/粉丝/关注统计数
	 * @return FansCount
	 */
	FansCount getFansCount(Long uid);

	/**
	 * 
	* @Title: addFriend 
	* @param: 
	* @Description: 添加好友
	* @return void
	 */
	void addFriend(Long uid, Long targetUid);
	
	/**
	 * 
	* @Title: deleteFriend 
	* @param: 
	* @Description: 删除好友
	* @return void
	 */
	void deleteFriend(Long uid, Long targetUid);
}