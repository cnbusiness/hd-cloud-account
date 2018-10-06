package com.hd.cloud.dao.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.hd.cloud.bo.FriendBo;
import com.hd.cloud.bo.User;
import com.hd.cloud.bo.UserProfile;
import com.hd.cloud.dao.AuthDao;
import com.hd.cloud.dao.mapper.AuthMapper;

/**
 * 
 * @ClassName: AuthDaoMyBatisImpl
 * @Description: 用户
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:49:07
 *
 */
@Repository
public class AuthDaoMyBatisImpl implements AuthDao {

	@Inject
	private AuthMapper authMapper;

	/**
	 * 
	 * @Title: login
	 * @param:
	 * @Description: 登录验证，返回userId
	 */
	@Override
	public Long login(long userId, String userPassword) {
		Long userIdInDb = authMapper.login(userId, userPassword);
		return userIdInDb;
	}

	/**
	 * 
	 * @Title: getIdByPhoneNumber
	 * @param:
	 * @Description: 根据手机号返回userId
	 */
	@Override
	public User getIdByPhoneNumber(String phoneNumber) {
		User user = authMapper.getIdByPhoneNumber(phoneNumber);
		return user;
	}

	/**
	 * 根据邮箱获取userId
	 */
	@Override
	public long getIdByEmail(String email) {
		User user = authMapper.getIdByEmail(email);
		if (user == null) {
			return 0;
		} else {
			return user.getUserId();
		}
	}

	@Override
	public String getPhoneByUserId(long userId) {
		return authMapper.getPhoneByUserId(userId);
	}

	@Override
	public int save(User user) {
		return authMapper.save(user);
	}

	@Override
	public int updateUser(User user) {
		return authMapper.updateUser(user);
	}

	@Override
	public UserProfile getUserProfileByUserId(long userId) {
		return authMapper.getUserProfileByUserId(userId);
	}

	@Override
	public List<UserProfile> getUserProfileListByUserIds(String userIds) {
		return authMapper.getUserProfileListByUserIds(userIds);
	}

	@Override
	public List<UserProfile> searchUser(String word) {
		return authMapper.searchUser(word);
	}

	@Override
	public List<UserProfile> searchUserLists(List<String> list) {
		return authMapper.searchUserLists(list);
	}
}
