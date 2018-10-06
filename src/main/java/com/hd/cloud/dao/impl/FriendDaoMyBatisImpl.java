package com.hd.cloud.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hd.cloud.bo.FriendBo;
import com.hd.cloud.dao.FriendDao;
import com.hd.cloud.dao.mapper.FriendMapper;

/**
 * 
  * @ClassName: FriendDaoMyBatisImpl
  * @Description: 好友
  * @author yaojie yao.jie@hadoop-tech.com
  * @Company hadoop-tech
  * @date 2018年4月12日 下午2:21:01
  *
 */
@Repository
public class FriendDaoMyBatisImpl implements FriendDao {

	@Autowired
	private FriendMapper fansMapper;

	@Override
	public List<FriendBo> findAllFriends(List<Long> ids) {
		return fansMapper.findAllFriends(ids);
	}

	@Override
	public List<FriendBo> findAllConditions(String countryCode, String condition) {
		return fansMapper.findAllConditions(countryCode, condition);
	}

	@Override
	public FriendBo getUserById(long userId) {
		return fansMapper.getUserById(userId);
	}

	@Override
	public List<FriendBo> findUsersByPhoneBook(List<String> phones) {
		return fansMapper.findUsersByPhoneBook(phones);
	}

}

