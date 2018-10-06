package com.hd.cloud.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hd.cloud.bo.FriendBo;
import com.hd.cloud.bo.User;
import com.hd.cloud.bo.UserProfile;

public interface AuthDao {

	/**
	 * 
	 * @Title: login
	 * @param:
	 * @Description: 登录验证，返回userId
	 * @return Long
	 */
	Long login(long userId, String userPassword);

	/**
	 * 
	 * @Title: getIdByPhoneNumber
	 * @param:
	 * @Description: 根据用户手机号码返回userId
	 * @return User
	 */
	User getIdByPhoneNumber(String phoneNumber);

	/**
	 * 
	 * @Title: getIdByEmail
	 * @param:
	 * @Description: 根据用户邮箱返回userId
	 * @return long
	 */
	long getIdByEmail(String useraccount);

	/**
	 * 
	 * @Title: getPhoneByUserId
	 * @param:
	 * @Description: 通过用户id查询手机号
	 * @return String
	 */
	String getPhoneByUserId(long userId);

	/**
	 * 
	 * @Title: save
	 * @param:
	 * @Description: 保存
	 * @return int
	 */
	public int save(User user);

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
	 * @Title: getUserProfileByUserId
	 * @param:
	 * @Description: 查询用户详情
	 * @return UserProfile
	 */
	public UserProfile getUserProfileByUserId(long userId);

	/**
	 * 
	 * @Title: getUserProfileListByUserIds
	 * @param:
	 * @Description: 批量查询用户资料
	 * @return List<UserProfile>
	 */
	public List<UserProfile> getUserProfileListByUserIds(String userIds);
	
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
