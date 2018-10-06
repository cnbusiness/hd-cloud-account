package com.hd.cloud.dao.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.hd.cloud.bo.Auth;
import com.hd.cloud.dao.TokenRedisDao;
import com.hd.cloud.util.ConstantUtil;

/**
 * 
 * @ClassName: TokenRedisDaoImpl
 * @Description: 用户token 存储以及销毁
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:46:57
 *
 */
@Repository
public class TokenRedisDaoImpl implements TokenRedisDao {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	/**
	 * auth对象存入缓存
	 */
	public void save(final Auth auth) {
		String redisKey = ConstantUtil.TOKEN_REDIS + auth.getUserId();
		// auth转换json再存入redis
		redisTemplate.opsForValue().set(redisKey, auth);
		// 过期时间设置
		redisTemplate.expire(redisKey, 24, TimeUnit.DAYS);
	}

	/**
	 * 根据userId读取redis中的auth对象
	 */
	public Auth read(final long userId) {
		String redisKey = ConstantUtil.TOKEN_REDIS + userId;
		Auth auth = (Auth) redisTemplate.opsForValue().get(redisKey);
		return auth;
	}

	/**
	 * 
	 * @Title: destroyAuth
	 * @param:
	 * @Description: 传入userId销毁redis缓存中对应的auth对象
	 */
	public void destroyAuth(final long userId) {
		String redisKey = ConstantUtil.TOKEN_REDIS + userId;
		redisTemplate.delete(redisKey);
	}

	@Override
	public void refreshAuthExpire(long userId) {
		String redisKey = ConstantUtil.TOKEN_REDIS + userId;
		redisTemplate.expire(redisKey, 24, TimeUnit.DAYS);
	}

}
