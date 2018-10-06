package com.hd.cloud.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.hd.cloud.bo.FriendBo;
import com.hd.cloud.dao.FriendDao;
import com.hd.cloud.redis.FansCache;
import com.hd.cloud.redis.GroupCache;
import com.hd.cloud.redis.bo.FansCount;
import com.hd.cloud.service.FriendService;
import com.hd.cloud.util.ConstantUtil;
import com.hd.cloud.vo.FriendVo;

/**
 * 
 * @ClassName: FriendServiceImpl
 * @Description: 好友
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月12日 上午11:55:34
 *
 */
@Slf4j
@Service
public class FriendServiceImpl implements FriendService {

	@Autowired
	private FansCache fansCache;

	@Autowired
	private FriendDao friendDao;

	@Autowired
	private GroupCache groupCache;

	/**
	 * 是否关注
	 */
	@Override
	public boolean isFollowing(long userId, long targetId) {
		return fansCache.isFollowing(userId, targetId);
	}

	/**
	 * 关注
	 */
	@Override
	public void attention(long userId, long targetId) {
		log.info("exec saveFans userid@ {},targetId@{}", userId, targetId);
		// 保存到redis
		fansCache.saveFans(userId, targetId);
	}

	/**
	 * 取消关注
	 */
	@Override
	public void unAttention(Long uid, Long targetUid) {
		log.info("exec deleteFans userid@{},followingId@{}", uid, targetUid);
		// remover following
		this.fansCache.removeFans(uid, targetUid);
		// remover remark
		this.fansCache.removeRemark(uid, targetUid);
		// remover group移除好友分组
		groupCache.deleteFriendsFromAllGroup(uid, targetUid);
	}

	/**
	 * 获取两者的关系
	 */
	@Override
	public int getRelationship(long userId, long targetId) {
		int relationship = ConstantUtil.FollowState.NONE;
		if (fansCache.isFollowers(userId, targetId)) {
			relationship = ConstantUtil.FollowState.FOLLOWER;
		}
		if (fansCache.isFollowing(userId, targetId)) {
			relationship = ConstantUtil.FollowState.FOLLOWING;
		}
		if (fansCache.isFriends(userId, targetId)) {
			relationship = ConstantUtil.FollowState.FRIEND;
		}
		// if (fansCache.isBlacklist(userId, targetId)) {
		// relationship = "blacklist";
		// }
		return relationship;
	}

	/**
	 * 我的好友列表
	 */
	@Override
	public List<FriendBo> findAllFriends(Long userId, Integer page, Integer size) {
		List<FriendBo> fansList = Lists.newArrayList();
		// 从redis获取好友
		List<Long> fansIds = fansCache.findAllFriends(userId, page, size);
		if (fansIds == null) {
			fansIds = Lists.newArrayList();
		}
		log.info(" 好友列表 : {} ",fansIds);
		if (fansIds.size() > 0) {
			for (Long uId : fansIds) {
				FriendBo friendBo = friendDao.getUserById(uId);
				if (friendBo != null) {
					friendBo.setFollowState(ConstantUtil.FollowState.FRIEND);
					fansList.add(friendBo);
				}
			}
		}
		// 添加用户备注信息
		setRemarks(userId, fansList);
		return fansList;
	}

	/**
	 * 获取关注的人列表
	 */
	@Override
	public List<FriendBo> findAllFollowing(Long userId, Integer page,
			Integer size) {
		List<Long> fansIds = fansCache.findAllFollowing(userId, page, size);
		List<FriendBo> list = null;
		// 查询好友
		List<Long> friendIds = fansCache.findAllFriends(userId,
				ConstantUtil.PageAll.PAGE_ALL, ConstantUtil.PageAll.SIZ_ALL);
		if (fansIds.size() > 0) {
			list = friendDao.findAllFriends(fansIds);
			for (FriendBo friendBo : list) {
				// 判断关系
				if (friendIds.contains(friendBo.getUserId())) {// 是好友
					friendBo.setFollowState(ConstantUtil.FollowState.FRIEND);
				} else {// 不是好友
					friendBo.setFollowState(ConstantUtil.FollowState.FOLLOWING);
				}
			}
		}
		setRemarks(userId, list);
		return list;
	}

	/**
	 * 获取所有粉丝
	 */
	@Override
	public List<FriendBo> findAllFollower(Long userId, Integer page,
			Integer size) {
		List<FriendBo> list = null;
		// 查询粉丝
		List<Long> fansIds = fansCache.findAllFollower(userId, page, size);
		// 查询好友
		List<Long> friendIds = fansCache.findAllFriends(userId,
				ConstantUtil.PageAll.PAGE_ALL, ConstantUtil.PageAll.SIZ_ALL);
		if (fansIds.size() > 0) {
			list = friendDao.findAllFriends(fansIds);
			for (FriendBo friendBo : list) {
				// 判断关系
				if (friendIds.contains(friendBo.getUserId())) {// 是好友
					friendBo.setFollowState(ConstantUtil.FollowState.FRIEND);
				} else {// 不是好友
					friendBo.setFollowState(ConstantUtil.FollowState.FOLLOWER);
				}
			}
		}
		setRemarks(userId, list);
		return list;
	}

	/**
	 * 添加/修改备注
	 */
	@Override
	public void saveOrUpdateRemark(long userId, FriendVo vo) {
		boolean isExist = fansCache.hasRemark(userId, vo.getUserId());
		if (!isExist) {
			fansCache.addRemark(userId, vo.getUserId(), vo.getRemark());
		} else {
			fansCache.updateRemark(userId, vo.getUserId(), vo.getRemark());
		}
	}

	/**
	 * 粉丝数统计
	 */
	@Override
	public FansCount getFansCount(Long uid) {
		return fansCache.getFansCount(uid);
	}

	/**
	 * 通讯录搜索
	 */
	@Override
	public List<FriendBo> findUserByPhoneBook(List<String> phone, Long userId) {
		List<FriendBo> list = friendDao.findUsersByPhoneBook(phone);
		// 查询所有关注的
		List<Long> following = fansCache.findAllFollowing(userId,
				ConstantUtil.PageAll.PAGE_ALL, ConstantUtil.PageAll.SIZ_ALL);

		for (FriendBo u : list) {
			if (following.contains(u.getUserId())) {
				u.setFollowState(ConstantUtil.FollowState.FOLLOWING);
			}
		}
		return list;
	}

	/**
	 * 
	 * @Title: setRemarks
	 * @param:
	 * @Description: 获取好友备注
	 * @return void
	 */
	private void setRemarks(Long userId, List<FriendBo> fansList) {
		if (null != fansList && !fansList.isEmpty()) {
			HashMap<Long, String> remarks = fansCache.getAllRemark(userId);
			for (FriendBo bo : fansList) {
				String remark = remarks.get(bo.getUserId());
				bo.setRemark(remark);
			}
		}
	}

	/**
	 * 搜索用户 昵称/id/手机号
	 */
	@Override
	public List<FriendBo> findAllConditions(String countryCode, String condition) {
		return friendDao.findAllConditions(countryCode, condition);
	}

	/**
	 * 添加好友
	 */
	@Override
	public void addFriend(long userId, long targetId) {
		fansCache.addFriend(userId, targetId);
	}

	/**
	 * 删除好友
	 */
	@Override
	public void deleteFriend(long userId, long targetId) {
		fansCache.deleteFriend(userId, targetId);
	}

}
