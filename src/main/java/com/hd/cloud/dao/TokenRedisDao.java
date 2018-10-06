package com.hd.cloud.dao;

import com.hd.cloud.bo.Auth;

/**
 * 
 * @ClassName: TokenRedisDao
 * @Description: token redis
 * @author Sheng sheng.haohao@autoflysoft.com
 * @Company dctp
 * @date 2017年8月7日 下午9:12:19
 *
 */
public interface TokenRedisDao {

	/**
	 * 
	 * @Title: save
	 * @param:
	 * @Description: 传入auth对象，存入redis缓存
	 * @return void
	 */
	public void save(final Auth auth);

	/**
	 * 
	 * @Title: read
	 * @param:
	 * @Description: 传入userId，查询redis缓存的auth对象
	 * @return Auth
	 */
	public Auth read(final long userId);

	/**
	 * 
	 * @Title: destroyAuth
	 * @param:
	 * @Description: 传入userId，销毁redis缓存中对应的auth对象
	 * @return void
	 */
	public void destroyAuth(final long userId);

	/**
	 * 
	 * @Title: refreshAuthExpire
	 * @param:
	 * @Description: 刷新auth过期时间
	 * @return void
	 */
	public void refreshAuthExpire(final long userId);

}