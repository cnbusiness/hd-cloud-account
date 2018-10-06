package com.hd.cloud.dao;

import com.hd.cloud.bo.UserProfile;

/**
 * 
  * @ClassName: UserProfileCache
  * @Description: 用户基础信息缓存
  * @author ShengHao shenghaohao@hadoop-tech.com
  * @Company hadoop-tech 
  * @date 2018年4月13日 下午3:04:15
  *
 */
public interface UserProfileCache {
	
	/**
	 * 
	* @Title: getUserProfileByUserId 
	* @param: 
	* @Description: 获取用户基础信息
	* @return UserProfile
	 */
	public UserProfile getUserProfileByUserId(long userId);
	
	
	/**
	 * 
	* @Title: update 
	* @param: 
	* @Description: 更新用户基础信息到redis 
	* @return void
	 */
	public void update(UserProfile userProfile);
}
