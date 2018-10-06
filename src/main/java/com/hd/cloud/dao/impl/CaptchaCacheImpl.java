
package com.hd.cloud.dao.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.hd.cloud.dao.CaptchaCache;
import com.hd.cloud.util.RedisKeyUtils;

/**
 * 
 * @ClassName: CaptchaCacheImpl
 * @Description: 验证码
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年12月7日 下午4:44:31
 *
 */
@Repository
public class CaptchaCacheImpl implements CaptchaCache {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Override
	public void recordCaptchaError(String phone, int type) {
		// 查询数量
		String i = (String) redisTemplate.opsForValue().get(RedisKeyUtils.captchaError(phone, type));
		// 每次都加一
		redisTemplate.opsForValue().increment(RedisKeyUtils.captchaError(phone, type), 1);
		// 超过5次就设置失效时间
		if (i != null && Integer.valueOf(i) == 5 - 1) {
			redisTemplate.expire(RedisKeyUtils.captchaError(phone, type), 30, TimeUnit.MINUTES);
		}
	}

	@Override
	public void delCaptchaError(String phone, int type) {
		redisTemplate.delete(RedisKeyUtils.captchaError(phone, type));
	}

	@Override
	public int getCaptchaErrCount(String phone, int type) {
		String i = (String) redisTemplate.opsForValue().get(RedisKeyUtils.captchaError(phone, type));
		return i == null ? 0 : Integer.valueOf(i);
	}

}
