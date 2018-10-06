package com.hd.cloud.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.hd.cloud.bo.UserProfile;
import com.hd.cloud.dao.UserProfileCache;
import com.hd.cloud.util.RedisKeyUtils;

@Repository
public class UserProfileCacheImpl implements UserProfileCache {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Override
	public UserProfile getUserProfileByUserId(long userId) {
		String key = RedisKeyUtils.userProfile(userId);
		UserProfile result = redisTemplate.execute(new RedisCallback<UserProfile>() {
			public UserProfile doInRedis(RedisConnection connection) throws DataAccessException {
				ValueOperations<Object, Object> operations = redisTemplate.opsForValue();
				UserProfile user = (UserProfile) operations.get(key);
				return user;
			}
		});
		return result;
	}

	@Override
	public void update(UserProfile userProfile) {
		String key = RedisKeyUtils.userProfile(userProfile.getUserId());
		redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				ValueOperations<Object, Object> valueOper = redisTemplate.opsForValue();
				valueOper.set(key, userProfile);
				return true;
			}
		});

	}

}
