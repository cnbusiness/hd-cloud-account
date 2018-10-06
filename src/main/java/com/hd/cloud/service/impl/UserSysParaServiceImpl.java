package com.hd.cloud.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.hd.cloud.bo.UserSysPara;
import com.hd.cloud.dao.UserSysParaDao;
import com.hd.cloud.service.UserSysParaService;

@Service
public class UserSysParaServiceImpl implements UserSysParaService{

	@Inject
	private UserSysParaDao  userSysParaDao;
	
	@Override
	public UserSysPara getUserSysParaByInternalCode(String internalCode) {
		return userSysParaDao.getUserSysParaByInternalCode(internalCode);
	}

}
