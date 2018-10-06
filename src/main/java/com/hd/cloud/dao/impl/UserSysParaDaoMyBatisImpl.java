package com.hd.cloud.dao.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.hd.cloud.bo.UserSysPara;
import com.hd.cloud.dao.UserSysParaDao;
import com.hd.cloud.dao.mapper.UserSysParaMapper;

@Repository
public class UserSysParaDaoMyBatisImpl implements UserSysParaDao{

	@Inject
	private UserSysParaMapper userSysParaMapper;
	
	@Override
	public UserSysPara getUserSysParaByInternalCode(String internalCode) {
		return userSysParaMapper.getUserSysParaByInternalCode(internalCode);
	}

}
