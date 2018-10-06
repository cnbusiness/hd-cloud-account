package com.hd.cloud.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hd.cloud.bo.FriendBo;
import com.hd.cloud.bo.FriendGroupBo;
import com.hd.cloud.dao.FriendDao;
import com.hd.cloud.redis.FansCache;
import com.hd.cloud.redis.GroupCache;
import com.hd.cloud.service.FriendGroupService;
import com.hd.cloud.vo.FriendGroupVo;

/**
 * 
 * @ClassName: GroupServiceImpl
 * @Description: 群组服务类
 * @author luo.xiaofu@moxiangroup.com
 * @Company moxian
 * @date 2015年7月15日 上午11:25:12
 *
 */
@Slf4j
@Service
public class FriendGroupServiceImpl implements FriendGroupService {

	@Autowired
	private GroupCache groupCache;

	@Autowired
	private FansCache fansCache;

	@Autowired
	private FriendDao friendDao;

	/**
	 * 用户添加一个组
	 */
	@Override
	public FriendGroupBo addGroup(Long uid, FriendGroupVo friendGroupVo) {
		log.info("user uid {} add group  , friendGroupVo {} ", uid,
				friendGroupVo);
		// 在redis 添加用户与组的关系
		return groupCache.saveGroup(uid, friendGroupVo);
	}

	/**
	 * 校验分组名是否存在
	 */
	@Override
	public boolean isGroupNameExist(long userId, String groupName) {
		return groupCache.isGroupNameExist(userId, groupName);
	}

	/**
	 * 编辑分组名
	 */
	@Override
	public FriendGroupBo updateGroup(Long uid, FriendGroupVo friendGroupVo) {
		return groupCache.updateGroup(uid, friendGroupVo);
	}

	/**
	 * 分组是否存在
	 */
	@Override
	public boolean isGroupExist(Long uid, Long gid) {
		return groupCache.isGroupExist(uid, gid);
	}

	/**
	 * 分组详情
	 */
	@Override
	public FriendGroupBo findOne(Long uid, Long gid) {
		// 查找
		FriendGroupBo bo = groupCache.findOne(uid, gid);
		// 设置群成员数
		bo.setCount(getOneGroupFriendsCount(uid, gid));
		return bo;
	}

	/**
	 * 分组列表
	 */
	@Override
	public List<FriendGroupBo> findAll(Long uid) {
		// 查找
		List<FriendGroupBo> boList = groupCache.findAll(uid);
		// 分组人数量
		Map<Long, Long> groupFriendsCount = getAllGroupFriendsCount(uid);
		for (FriendGroupBo bo : boList) {
			bo.setCount(groupFriendsCount.get(bo.getFriGroupId()));
		}
		// Collections.sort(boList, (FriendGroupBo a, FriendGroupBo b) ->
		// (b.getFriGroupId().compareTo(a.getFriGroupId())));
		return boList;
	}

	/**
	 * 删除好友分组
	 */
	@Override
	public void deleteGroup(Long uid, Long gid) {
		// 从redis删除这个组
		groupCache.deleteGroup(uid, gid);
	}

	/**
	 * 分组添加好友
	 */
	@Override
	public boolean addFriendsToGroup(Long uid, Long gid, Long[] fids) {
		log.debug("user uid {} add friends {} to  group {} ", uid, fids, gid);
		return groupCache.addFriendsToGroup(uid, gid, fids);
	}

	/**
	 * 分组移除好友
	 */
	@Override
	public boolean removeFriendsFromGroup(Long uid, Long gid, Long[] fids) {
		return groupCache.removeFriendsFromGroup(uid, gid, fids);
	}

	/**
	 * 查询没有添加到此分组的好友
	 */
	@Override
	public List<FriendBo> findNotInCurrentGroupFriends(Long uid, Long gid) {
		// 获取没有在这个群的好友ID
		List<Long> ids = groupCache.findNotInCurrentGroupFriends(uid, gid);
		// 通过好友ID获取好友信息
		List<FriendBo> fansList = null;
		if (ids != null && ids.size() > 0) {
			fansList = friendDao.findAllFriends(ids);
			// 查询备注
			setRemarks(uid, fansList);
		}
		return fansList;
	}

	/**
	 * 
	 * @Title: getGroupFriends
	 * @param:
	 * @Description: 获取分组内好友
	 * @return List<FriendBo>
	 */
	@Override
	public List<FriendBo> getGroupFriends(Long uid, Long gid) {
		List<FriendBo> fansList = null;
		// 从cache里获取用户ID
		List<Long> fids = groupCache.findGroupFriends(uid, gid);

		// 通过cache里获取的用户ID到数据查询用户信息
		if (fids != null && fids.size() > 0) {
			fansList = friendDao.findAllFriends(fids);
			setRemarks(uid, fansList);
		}
		return fansList;

	}

	/**
	 * 添加好友到多个分组
	 */
	@Override
	public void addFriendsToMultipleGroup(Long uid, Long[] gids, Long fid) {
		log.debug("uid {} , gids  {} , fid {}", uid, gids, fid);
		// 获取所有群组
		List<FriendGroupBo> boList = groupCache.findAll(uid);
		// 循环将用户加入到这些群
		for (FriendGroupBo groupBo : boList) {
			boolean flag = false;
			for (Long gid : gids) {
				if (groupBo.getFriGroupId() == gid.longValue()) {
					flag = true;
				}
			}
			if (flag) {
				// 添加好友到分组
				groupCache.addFriendsToGroup(uid, groupBo.getFriGroupId(),
						new Long[] { fid });
			} else {
				// 判断是否加入分组
				boolean isGroupFriends = groupCache.isGroupFriends(uid,
						groupBo.getFriGroupId(), new Long[] { fid });
				if (isGroupFriends) {
					// 移除分组
					groupCache.removeFriendsFromGroup(uid,
							groupBo.getFriGroupId(), new Long[] { fid });
				}
			}
		}

	}

	/**
	 * 查询用户已添加的组
	 */
	public List<FriendGroupBo> findFriendsInGroups(Long uid, Long fid) {
		log.debug(" uid {},fid {}", uid, fid);
		// 查询登录用户所有的组
		List<FriendGroupBo> boList = groupCache.findAll(uid);
		// 根据好友添加的
		for (FriendGroupBo groupBo : boList) {
			boolean isGroupFriends = groupCache.isGroupFriends(uid,
					groupBo.getFriGroupId(), new Long[] { fid });
			if (isGroupFriends) {
				groupBo.setExist(true);
			} else {
				groupBo.setExist(false);
			}
		}
		return boList;
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
	 * 查询分组好友
	 */
	@Override
	public List<Long> findGroupFriends(Long uid, Long gid) {
		return groupCache.findGroupFriends(uid, gid);
	}

	/**
	 * 查询不在该分组的好友
	 */
	@Override
	public List<Long> findNotInCurrentGroupFriendIds(Long uid, Long gid) {
		return groupCache.findNotInCurrentGroupFriends(uid, gid);
	}

	/**
	 * 查询所有的分组好友数量
	 */
	@Override
	public Map<Long, Long> getAllGroupFriendsCount(Long uid) {
		Map<Long, Long> counts = groupCache.getAllGroupFriendsCount(uid);
		return counts;
	}

	/**
	 * 查询单个分组的好友数量
	 */
	@Override
	public long getOneGroupFriendsCount(Long uid, Long gid) {
		long count = groupCache.getOneGroupFriendsCount(uid, gid);
		return count;
	}

	/**
	 * 是否好友
	 */
	@Override
	public boolean isFriends(Long userId, Long friendIds) {
		return groupCache.isFriends(userId, new Long[] { friendIds });
	}
}
