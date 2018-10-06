package com.hd.cloud.service.impl;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esotericsoftware.minlog.Log;
import com.hd.cloud.bo.Auth;
import com.hd.cloud.bo.MoneyEntityWallet;
import com.hd.cloud.bo.User;
import com.hd.cloud.bo.UserProfile;
import com.hd.cloud.dao.AuthDao;
import com.hd.cloud.dao.MoneyEntityWalletDao;
import com.hd.cloud.dao.UserProfileCache;
import com.hd.cloud.rest.RegisterResource;
import com.hd.cloud.service.AuthService;
import com.hd.cloud.service.UserService;
import com.hd.cloud.util.SequenceUtil;
import com.hd.cloud.vo.UserRegVo;
import com.hlb.cloud.util.CommonConstantUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: UserServiceImpl
 * @Description: 用户管理
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月4日 下午4:47:51
 *
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Inject
	private AuthDao authDao;

	@Inject
	private AuthService authService;

	@Inject
	private UserProfileCache userProfileCache;

	@Inject
	private MoneyEntityWalletDao moneyEntityWalletDao;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Auth createUser(UserRegVo userRegVo) throws UnsupportedEncodingException {
		User user = User.builder().status(1).countryCode(userRegVo.getCountryCode()).phone(userRegVo.getPhoneNo())
				.nickName(userRegVo.getPhoneNo().substring(userRegVo.getPhoneNo().length() - 4,
						userRegVo.getPhoneNo().length()))
				.password(userRegVo.getPassword()).activeFlag(CommonConstantUtil.ACTIVE_FLAG_Y).createBy(10000).build();
		authDao.save(user);
		// 保存钱包
		MoneyEntityWallet moneyEntityWallet = MoneyEntityWallet.builder()
				.account(SequenceUtil.getInstance().generateWalletId(2)).walletId(user.getUserId()).type(1)
				.activeFlag(CommonConstantUtil.ACTIVE_FLAG_Y).build();
		moneyEntityWalletDao.save(moneyEntityWallet);

		// 生成token
		Auth auth = authService.saveAndGetToken(user.getUserId());

		// 更新到redis
		UserProfile userProfile = UserProfile.builder().userId(user.getUserId()).nickName(user.getNickName()).status(1)
				.countryCode(userRegVo.getCountryCode()).phone(userRegVo.getPhoneNo()).build();
		userProfileCache.update(userProfile);
		log.info("################userProfile:{}",userProfile);
		return auth;
	}

	@Override
	public User hasRegisterByPhoneNo(String phoneNo) {
		return authDao.getIdByPhoneNumber(phoneNo);
	}

	@Override
	public int update(User user) {
		return authDao.updateUser(user);
	}

}
