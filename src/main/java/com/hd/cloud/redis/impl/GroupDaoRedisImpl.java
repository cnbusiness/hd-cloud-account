package com.hd.cloud.redis.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.DefaultRedisSet;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hd.cloud.bo.FriendGroupBo;
import com.hd.cloud.redis.GroupCache;
import com.hd.cloud.redis.util.RedisKey;
import com.hd.cloud.vo.FriendGroupVo;

/**
 * 
 * @ClassName: GroupDaoRedisImpl
 * @Description: 好友分组redis
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月13日 上午10:49:47
 *
 */
@Repository
public class GroupDaoRedisImpl implements GroupCache {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 
	 * @Title: getGid
	 * @param:
	 * @Description: 计数器自增
	 * @return long
	 */
	public long getGid() {
		Long value = redisTemplate.opsForValue().increment(RedisKey.globalgid(), 1);
		return value.longValue();
	}

	/**
	 * 创建分组
	 */
	@Override
	public FriendGroupBo saveGroup(Long uid, FriendGroupVo friendGroupVo) {
		FriendGroupBo bo = new FriendGroupBo();
		bo.setFriGroupId(getGid());
		bo.setName(friendGroupVo.getGroupName());
		RedisMap redisMap = groupHash(RedisKey.group(uid));
		redisMap.put(bo.getFriGroupId(), bo.getName());
		return bo;
	}

	/**
	 * 分组名是否存在
	 */
	@Override
	public boolean isGroupNameExist(Long uid, String name) {
		Collection<String> names = groupHash(RedisKey.group(uid)).values();
		return names.contains(name);
	}

	/**
	 * 分组是否存在
	 */
	@Override
	public boolean isGroupExist(Long uid, Long gid) {
		return groupHash(RedisKey.group(uid)).containsKey(gid);
	}

	/**
	 * 编辑分组名
	 */
	@Override
	public FriendGroupBo updateGroup(Long uid, FriendGroupVo friendGroupVo) {
		FriendGroupBo bo = new FriendGroupBo();
		bo.setFriGroupId(friendGroupVo.getGroupId());
		bo.setName(friendGroupVo.getGroupName());
		RedisMap redisMap = groupHash(RedisKey.group(uid));
		redisMap.put(bo.getFriGroupId(), bo.getName());
		return bo;
	}

	/**
	 * 获取分组详情
	 */
	@Override
	public FriendGroupBo findOne(Long uid, Long gid) {
		FriendGroupBo bo = new FriendGroupBo();
		String groupName = (String) groupHash(RedisKey.group(uid)).get(gid);
		bo.setFriGroupId(gid);
		bo.setName(groupName);
		return bo;
	}

	/**
	 * 分组列表
	 */
	@Override
	public List<FriendGroupBo> findAll(Long uid) {
		Map<Long, String> redisMap = getRedisTemplate(uid).boundHashOps(RedisKey.group(uid)).entries();
		List<FriendGroupBo> list = Lists.newArrayList();
		for (Map.Entry<Long, String> entry : redisMap.entrySet()) {
			FriendGroupBo bo = new FriendGroupBo();
			bo.setFriGroupId(entry.getKey());
			bo.setName(String.valueOf(entry.getValue()));
			list.add(bo);
		}
		return list;
	}

	/**
	 * 删除好友分组
	 */
	@Override
	public void deleteGroup(Long uid, Long gid) {
		// 删除分组所有好友
		deleteKey(RedisKey.groupFriends(uid, gid));
		// 删除好友人数统计redis
		deleteHashKey(RedisKey.groupFriendsCount(uid), gid);
		// 删除分组
		groupHash(RedisKey.group(uid)).remove(gid);
	}

	/**
	 * 分组添加好友
	 */
	@Override
	public boolean addFriendsToGroup(Long uid, Long gid, Long[] fids) {
		// 分组添加好友
		boolean add = groupFriends(uid, gid).addAll(Arrays.asList(fids));

		BoundHashOperations hashOperations = getRedisTemplate(uid).boundHashOps(RedisKey.groupFriendsCount(uid));
		// 更新分组好友人数
		hashOperations.put(gid, updateGroupFriendsCallback((RedisKey.groupFriends(uid, gid))));
		return add;
	}

	/**
	 * 分组移除好友
	 */
	@Override
	public boolean removeFriendsFromGroup(Long uid, Long gid, Long[] fids) {
		// 分组移除好友
		boolean flag = groupFriends(uid, gid).removeAll(Arrays.asList(fids));

		BoundHashOperations hashOperations = getRedisTemplate(uid).boundHashOps(RedisKey.groupFriendsCount(uid));
		// 更新分组数量
		hashOperations.put(gid, updateGroupFriendsCallback((RedisKey.groupFriends(uid, gid))));
		return flag;
	}

	/**
	 * 是否好友
	 */
	@Override
	public boolean isFriends(Long uid, Long[] fids) {
		boolean flag = false;
		Set<Long> friendIds = getLimitRedisSet(RedisKey.friends(uid));
		flag = friendIds.contains(fids);
		return flag;
	}

	/**
	 * 好友是否在此分组
	 */
	@Override
	public boolean isGroupFriends(Long uid, Long gid, Long[] fids) {
		boolean flag = false;
		Set<Long> groupFriendIds = groupFriends(uid, gid);
		flag = groupFriendIds.containsAll(Arrays.asList(fids));
		return flag;
	}

	/**
	 * 查询分组好友
	 */
	@Override
	public List<Long> findGroupFriends(Long uid, Long gid) {
		RedisSet<Long> redisSet = groupFriends(uid, gid);
		List<Long> fids = Lists.newArrayList(redisSet);
		return fids;
	}

	/**
	 * 获取不在该分组的好友
	 */
	@Override
	public List<Long> findNotInCurrentGroupFriends(Long uid, Long gid) {
		// 获取好友
		Set<Long> friendIds = getLimitRedisSet(RedisKey.friends(uid));
		// 获取已经加入群组的好友
		RedisSet<Long> groupFriends = groupFriends(uid, gid);
		// 移除
		friendIds.removeAll(groupFriends);
		// 返回
		List<Long> ids = Lists.newArrayList(friendIds);
		return ids;
	}

	// 移除好友所有分组
	@Override
	public void deleteFriendsFromAllGroup(Long uid, Long fid) {
		List<FriendGroupBo> groupBos = findAll(uid);
		for (FriendGroupBo groupBo : groupBos) {
			removeFriendsFromGroup(uid, groupBo.getFriGroupId(), new Long[] { fid });
		}
	}

	private Set<Long> getLimitRedisSet(String baseKey) {
		boolean isExist = getRedisTemplate(baseKey).hasKey(baseKey);
		if (isExist) {
			Set<Long> ids = getRedisTemplate(baseKey).boundSetOps(baseKey).members();
			return ids;
		} else {
			return Sets.newHashSet();
		}
	}

	private Long updateGroupFriendsCallback(String key) {
		BoundSetOperations setOperations = getRedisTemplate(key).boundSetOps(key);
		return setOperations.size();
	}

	private RedisSet<Long> groupFriends(Long uid, Long gid) {
		return new DefaultRedisSet<Long>(RedisKey.groupFriends(uid, gid), getRedisTemplate(uid));
	}

	private void deleteHashKey(String key, Object hashKey) {
		BoundHashOperations hashOperations = getRedisTemplate(key).boundHashOps(key);
		if (hashOperations.hasKey(hashKey)) {
			hashOperations.delete(hashKey);
		}
	}

	private void deleteKey(String key) {
		getRedisTemplate(key).delete(key);
	}

	private RedisMap<Long, String> groupHash(String key) {
		return new DefaultRedisMap<Long, String>(key, getRedisTemplate(key));
	}

	private RedisTemplate getRedisTemplate(String key) {
		return redisTemplate;
	}

	private RedisTemplate getRedisTemplate(Long busId) {
		return redisTemplate;
	}

	@Override
	public Map<Long, Long> getAllGroupFriendsCount(Long uid) {
		HashMap<Long, Long> map = Maps.newHashMap();
		BoundHashOperations hashOperations = getRedisTemplate(uid).boundHashOps(RedisKey.groupFriendsCount(uid));
		map.putAll(hashOperations.entries());
		return map;
	}

	@Override
	public long getOneGroupFriendsCount(Long uid, Long gid) {
		HashMap<Long, Long> map = Maps.newHashMap();
		BoundHashOperations hashOperations = getRedisTemplate(uid).boundHashOps(RedisKey.groupFriendsCount(uid));
		map.putAll(hashOperations.entries());
		if (hashOperations.hasKey(gid)) {
			if (hashOperations.get(gid) != null) {
				return Long.parseLong(hashOperations.get(gid).toString());
			}
		}
		return 0;
	}
}
