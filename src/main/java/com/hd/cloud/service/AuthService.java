package com.hd.cloud.service;

import java.util.List;

import com.hd.cloud.bo.Auth;
import com.hd.cloud.bo.User;
import com.hd.cloud.bo.UserProfile;
import com.hd.cloud.vo.UserVipVo;
import com.hlb.cloud.bo.BoUtil;

/**
 * 
 * @ClassName: AuthService
 * @Description: 登录 退出服务
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:50:32
 *
 */
public interface AuthService {

	/**
	 * 
	 * @Title: isValidTokenPare
	 * @param:
	 * @Description: 验证UserId与token
	 * @return boolean
	 */
	boolean isValidTokenPare(long userId, String token);

	/**
	 * 
	 * @Title: isValidatePassPare
	 * @param:
	 * @Description: 根据userId、密码、登录APP类型，验证登录
	 * @return Auth
	 */
	Auth isValidatePassPare(long id, String userpass);

	/**
	 * 
	 * @Title: passwordValidation
	 * @param:
	 * @Description: 验证用户id、密码正确性
	 * @return Auth
	 */
	boolean passwordValidation(long id, String userpass);

	/**
	 * 
	 * @Title: getUserIdByAccount
	 * @param:
	 * @Description: 根据手机号码或者邮箱，获取登录用户的UserId
	 * @return long
	 */
	long getUserIdByAccount(String userAccount);

	/**
	 * 
	 * @Title: saveAndGetToken
	 * @param:
	 * @Description: 传入userId生成token
	 * @return Auth
	 */
	Auth saveAndGetToken(long userId);

	/**
	 * 
	 * @Title: detroyAuthRedis
	 * @param:
	 * @Description: 传入userId清除redis中的token，即踢下线
	 * @return void
	 */
	void detroyAuthRedis(long userId);

	/**
	 * 
	 * @Title: getUserProfileByUserId
	 * @param:
	 * @Description: 查询用户详情
	 * @return UserProfile
	 */
	public UserProfile getUserProfileByUserId(long userId);

	/**
	 * 
	 * @Title: update
	 * @param: User
	 *             user
	 * @Description: 更新用户
	 * @return int
	 */
	int updateUser(User user);

	/**
	 * 
	 * @Title: getUserProfileListByUserIds
	 * @param:
	 * @Description: 获取用户信息
	 * @return List<UserProfile>
	 */
	public List<UserProfile> getUserProfileListByUserIds(String userIds, long userId);

	/**
	 * 
	 * @Title: updateUserVip
	 * @param:
	 * @Description: 升级vip
	 * @return BoUtil
	 */
	public BoUtil updateUserVip(UserVipVo payload);
	
	/**
	 * 
	* @Title: searchUser 
	* @param: 
	* @Description: 搜索用户 
	* @return List<FriendBo>
	 */
	List<UserProfile> searchUser(String word);
	
	/**
	 * 
	* @Title: searchUserLists 
	* @param: 
	* @Description: 批量搜索用户
	* @return List<UserProfile>
	 */
	List<UserProfile> searchUserLists(List<String> list);

}