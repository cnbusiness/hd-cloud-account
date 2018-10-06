package com.hd.cloud.redis.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

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
import com.hd.cloud.redis.FansCache;
import com.hd.cloud.redis.bo.FansCount;
import com.hd.cloud.redis.util.RedisKey;
import com.hd.cloud.util.ConstantUtil;

/**
 * Created by Kui.Yang on 2015/4/21.
 */

@Repository
public class FansDaoRedisImpl implements FansCache {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	// 关注数量
	private final String FOLLOWINGCNT = "followingCnt";
	// 被关注数量
	private final String FOLLOWERSCNT = "followersCnt";
	// 好友数
	private final String FRIENDSCNT = "friendsCnt";

	/**
	 * 是否关注
	 */
	@Override
	public boolean isFollowing(Long uid, Long targetUid) {
		Set<Long> followings = getLimitRedisSet(RedisKey.following(uid));
		return followings.contains(targetUid);
	}

	/**
	 * 关注好友
	 */
	@Override
	public void saveFans(Long uid, Long targetUid) {
		limitRedisSet(RedisKey.following(uid), 1).add(targetUid);
		limitRedisSet(RedisKey.followers(targetUid), 1).add(uid);

		updateCounterByHashKey(RedisKey.fansCount(uid), FOLLOWINGCNT, redisSetCallback(RedisKey.following(uid)));

		updateCounterByHashKey(RedisKey.fansCount(targetUid), FOLLOWERSCNT,
				redisSetCallback(RedisKey.followers(targetUid)));

		if (isFollowing(targetUid, uid)) {

			limitRedisSet(RedisKey.friends(uid), 1).add(targetUid);
			limitRedisSet(RedisKey.friends(targetUid), 1).add(uid);

			updateCounterByHashKey(RedisKey.fansCount(uid), FRIENDSCNT, redisSetCallback(RedisKey.friends(uid)));

			updateCounterByHashKey(RedisKey.fansCount(targetUid), FRIENDSCNT,
					redisSetCallback(RedisKey.following(targetUid)));
		}
	}

	/**
	 * 是否被关注
	 */
	@Override
	public boolean isFollowers(Long uid, Long targetUid) {
		Set<Long> followers = getLimitRedisSet(RedisKey.followers(uid));
		return followers.contains(targetUid);
	}

	/**
	 * 是否好友
	 */
	@Override
	public boolean isFriends(Long uid, Long targetUid) {
		boolean flag = false;

		Set<Long> friendIds = getLimitRedisSet(RedisKey.friends(uid));

		flag = friendIds.containsAll(Arrays.asList(targetUid));

		return flag;
	}

	/**
	 * 取消关注
	 */
	@Override
	public void removeFans(Long uid, Long targetUid) {
		// 删除他的关注对象
		deleteLimitRedisSet(RedisKey.following(uid), targetUid);
		// 删除目标对象的粉丝关系
		deleteLimitRedisSet(RedisKey.followers(targetUid), uid);

		updateCounterByHashKey(RedisKey.fansCount(uid), FOLLOWINGCNT, redisSetCallback(RedisKey.following(uid)));

		updateCounterByHashKey(RedisKey.fansCount(targetUid), FOLLOWERSCNT,
				redisSetCallback(RedisKey.followers(targetUid)));

		if (isFriends(uid, targetUid)) {

			deleteLimitRedisSet(RedisKey.friends(uid), targetUid);
			deleteLimitRedisSet(RedisKey.friends(targetUid), uid);

			updateCounterByHashKey(RedisKey.fansCount(uid), FRIENDSCNT, redisSetCallback(RedisKey.friends(uid)));

			updateCounterByHashKey(RedisKey.fansCount(targetUid), FRIENDSCNT,
					redisSetCallback(RedisKey.following(targetUid)));
		}
	}

	/**
	 * 添加备注
	 */
	@Override
	public void addRemark(Long uid, Long fid, String remarkStr) {
		limitRedisMap(RedisKey.remark(uid), 1).put(fid, remarkStr);
	}

	/**
	 * 移除备注
	 */
	@Override
	public void removeRemark(Long uid, Long fid) {
		deleteLimitRedisMap(RedisKey.remark(uid), fid);
	}

	/**
	 * 好友备注
	 */
	@Override
	public HashMap<Long, String> getAllRemark(Long uid) {
		return getLimitRedisMap(RedisKey.remark(uid));
	}

	/**
	 * 是否有备注
	 */
	@Override
	public boolean hasRemark(Long uid, Long fid) {
		return limitRedisMapHasKey(RedisKey.remark(uid), fid);
	}

	/**
	 * 修改备注
	 */
	@Override
	public void updateRemark(Long uid, Long fid, String remarkStr) {
		String baseKey = RedisKey.remark(uid);
		updateLimitRedisMap(baseKey, fid, remarkStr);

	}

	/**
	 * 好友列表
	 */
	@Override
	public List<Long> findAllFriends(Long uid, Integer page, Integer size) {
		
		Set<String> friendStr = redisTemplate.opsForSet().members(RedisKey.friends(uid));
		Set<Long> friendIds = new HashSet<Long>();
		for (String str : friendStr) {
			friendIds.add(Long.parseLong(str));
		}
		List<Long> ids = Lists.newArrayList();
		ids.addAll(friendIds);
		// 采用默认排序(暂时注释)
		Collections.sort(ids);

		if (page == ConstantUtil.PageAll.PAGE_ALL && size == ConstantUtil.PageAll.SIZ_ALL) {
			return ids;
		}
		int start = page * size < ids.size() ? page * size : ids.size();
		int end = (start + size) < ids.size() ? (start + size) : ids.size();
		if (!ids.isEmpty()) {
			end = ids.size() < end ? ids.size() : end;
			System.out.println(start + " - " + end);
			ids = ids.subList(start, end);
		}
		return ids;
	}

	/**
	 * 关注列表
	 */
	@Override
	public List<Long> findAllFollowing(Long uid, Integer page, Integer size) {
		Set<Long> friendIds = getLimitRedisSet(RedisKey.following(uid));
		List<Long> ids = Lists.newArrayList();
		ids.addAll(friendIds);
		// 采用默认排序(暂时注释)
		Collections.sort(ids);

		if (page == ConstantUtil.PageAll.PAGE_ALL && size == ConstantUtil.PageAll.SIZ_ALL) {
			return ids;
		}
		int start = page * size < ids.size() ? page * size : ids.size();
		int end = (start + size) < ids.size() ? (start + size) : ids.size();
		if (!ids.isEmpty()) {
			end = ids.size() < end ? ids.size() : end;
			System.out.println(start + " - " + end);
			ids = ids.subList(start, end);
		}
		return ids;
	}

	/**
	 * 粉丝列表
	 */
	@Override
	public List<Long> findAllFollower(Long userId, Integer page, Integer size) {
		Set<Long> friendIds = getLimitRedisSet(RedisKey.following(userId));
		List<Long> ids = Lists.newArrayList();
		ids.addAll(friendIds);
		// 采用默认排序(暂时注释)
		Collections.sort(ids);
		if (page == ConstantUtil.PageAll.PAGE_ALL && size == ConstantUtil.PageAll.SIZ_ALL) {
			return ids;
		}
		int start = page * size < ids.size() ? page * size : ids.size();
		int end = (start + size) < ids.size() ? (start + size) : ids.size();
		if (!ids.isEmpty()) {
			end = ids.size() < end ? ids.size() : end;
			System.out.println(start + " - " + end);
			ids = ids.subList(start, end);
		}
		return ids;
	}

	/**
	 * 粉丝统计
	 */
	@Override
	public FansCount getFansCount(Long uid) {
		RedisMap<String, AtomicLong> map = new DefaultRedisMap<String, AtomicLong>(RedisKey.fansCount(uid),
				getRedisTemplate(uid));
		FansCount fansCount = new FansCount();
		fansCount.setFollowingCnt(map.get(FOLLOWINGCNT) == null ? new AtomicLong(0) : map.get(FOLLOWINGCNT));
		fansCount.setFollowersCnt(map.get(FOLLOWERSCNT) == null ? new AtomicLong(0) : map.get(FOLLOWERSCNT));
		fansCount.setFriendsCnt(map.get(FRIENDSCNT) == null ? new AtomicLong(0) : map.get(FRIENDSCNT));
		return fansCount;
	}

	private RedisTemplate getRedisTemplate(Long busId) {
		return redisTemplate;
	}

	private void updateLimitRedisMap(String baseKey, Long id, Object value) {
		boolean isExist = getRedisTemplate(baseKey).hasKey(baseKey);
		if (isExist) {
			BoundHashOperations hashOperations = getRedisTemplate(baseKey).boundHashOps(baseKey);
			if (hashOperations.hasKey(id)) {
				hashOperations.put(id, value);
			}
		}
	}

	private boolean limitRedisMapHasKey(String baseKey, Object id) {
		boolean flag = false;
		boolean isExist = getRedisTemplate(baseKey).hasKey(baseKey);
		if (isExist) {
			BoundHashOperations hashOperations = getRedisTemplate(baseKey).boundHashOps(baseKey);
			if (hashOperations.hasKey(id)) {
				flag = true;
			}
		}
		return flag;
	}

	private HashMap<Long, String> getLimitRedisMap(String baseKey) {
		HashMap<Long, String> map = Maps.newHashMap();
		boolean isExist = getRedisTemplate(baseKey).hasKey(baseKey);
		if (isExist) {
			BoundHashOperations hashOperations = getRedisTemplate(baseKey).boundHashOps(baseKey);
			if (hashOperations.size() > 0) {
				map.putAll(hashOperations.entries());
			}
		}
		return map;
	}

	private void deleteLimitRedisMap(String baseKey, Long id) {
		boolean isExist = getRedisTemplate(baseKey).hasKey(baseKey);
		if (isExist) {
			BoundHashOperations hashOperations = getRedisTemplate(baseKey).boundHashOps(baseKey);
			if (hashOperations.hasKey(id)) {
				hashOperations.delete(id);
			}
		}
	}

	private RedisMap<Long, String> limitRedisMap(String baseKey, int dataSize) {
		return new DefaultRedisMap<Long, String>(baseKey, getRedisTemplate(baseKey));
	}

	private void deleteLimitRedisSet(String baseKey, Long id) {
		boolean isExist = getRedisTemplate(baseKey).hasKey(baseKey);
		if (isExist) {
			BoundSetOperations setOperations = getRedisTemplate(baseKey).boundSetOps(baseKey);
			if (setOperations.isMember(id)) {
				setOperations.remove(id);
			}
		}
	}

	private void updateCounterByHashKey(String key, Object hashKey, Long count) {
		BoundHashOperations hashOperations = getRedisTemplate(key).boundHashOps(key);
		hashOperations.put(hashKey, new AtomicLong(count));
	}

	private Long redisSetCallback(String baseKey) {
		Long size = 0L;
		boolean isExist = getRedisTemplate(baseKey).hasKey(baseKey);
		if (isExist) {
			BoundSetOperations setOperations = getRedisTemplate(baseKey).boundSetOps(baseKey);
			size += setOperations.size();
		}
		return size;
	}

	private RedisSet<Long> limitRedisSet(String baseKey, int dataSize) {
		return new DefaultRedisSet<Long>(baseKey, getRedisTemplate(baseKey));
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

	private RedisTemplate getRedisTemplate(String key) {
		return redisTemplate;

	}

	@Override
	public void addFriend(Long uid, Long targetUid) {
		redisTemplate.opsForSet().add(RedisKey.friends(uid), String.valueOf(targetUid));
		redisTemplate.opsForSet().add(RedisKey.friends(targetUid), String.valueOf(uid));
	}

	@Override
	public void deleteFriend(Long uid, Long targetUid) {
		redisTemplate.opsForSet().remove(RedisKey.friends(uid), String.valueOf(targetUid));
		redisTemplate.opsForSet().remove(RedisKey.friends(targetUid), String.valueOf(uid));
	}
}
