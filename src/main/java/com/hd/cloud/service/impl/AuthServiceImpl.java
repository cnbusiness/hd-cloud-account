package com.hd.cloud.service.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.hd.cloud.bo.Auth;
import com.hd.cloud.bo.MoneyEntityTrans;
import com.hd.cloud.bo.User;
import com.hd.cloud.bo.UserProfile;
import com.hd.cloud.bo.UserSysPara;
import com.hd.cloud.dao.AuthDao;
import com.hd.cloud.dao.MoneyEntityTransDao;
import com.hd.cloud.dao.TokenRedisDao;
import com.hd.cloud.dao.UserProfileCache;
import com.hd.cloud.dao.UserSysParaDao;
import com.hd.cloud.service.AuthService;
import com.hd.cloud.service.FriendService;
import com.hd.cloud.util.ConstantUtil;
import com.hd.cloud.util.ErrorCode;
import com.hd.cloud.util.HashGenerator;
import com.hd.cloud.util.SequenceUtil;
import com.hd.cloud.vo.UserVipVo;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.util.CommonConstantUtil;
import com.hlb.cloud.util.Type;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: AuthServiceImpl
 * @Description: 登录 退出
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:50:54
 *
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

	@Inject
	private AuthDao authDao;

	@Inject
	private TokenRedisDao tokenDao;

	@Inject
	private UserProfileCache userProfileCache;

	@Inject
	private FriendService friendService;

	@Inject
	private UserSysParaDao userSysParaDao;

	@Inject
	private MoneyEntityTransDao moneyEntityTransDao;

	/**
	 * 
	 * @Title: isValidTokenPare
	 * @param:
	 * @Description: 传入userId与token，进行token有效性验证
	 */
	@Override
	public boolean isValidTokenPare(long userId, String token) {
		final Auth auth = tokenDao.read(userId);
		log.debug("auth :{}", auth);
		boolean isValid = false;
		if (auth == null || token == null || !token.equals(auth.getToken())) {
			log.info("UserId = {} token validation failure!", userId);
			return isValid;
		}
		// 当token验证成功是，auth对象的过期时间刷新
		tokenDao.refreshAuthExpire(userId);
		log.info("UserId = {} token validation success!", userId);
		return true;
	}

	/**
	 * 
	 * @Title: isValidatePassPare
	 * @param:
	 * @Description: 根据用户userId与密码，进行登录验证
	 */
	@Override
	public Auth isValidatePassPare(long userId, String userpass) {
		Long userIdInDb = authDao.login(userId, userpass);
		Auth auth = null;
		if (userIdInDb != null) {
			String token = this.createToken(userIdInDb);
			auth = Auth.builder().userId(userId).token(token).id(userId).authTime(new Date()).build();
			tokenDao.save(auth);
		}
		return auth;
	}

	/**
	 * 
	 * @Title: getUserIdByAccount
	 * @param:
	 * @Description: 根据传入的手机号或者邮箱，查询userId
	 */
	@Override
	public long getUserIdByAccount(String userAccount) {
		long userId;
		if (Type.isEmail(userAccount)) {
			// 根据邮箱获取userId
			userId = authDao.getIdByEmail(userAccount);
		} else {
			User user = authDao.getIdByPhoneNumber(userAccount);
			// 根据手机号获取userId
			if (user == null) {
				userId = 0;
			} else {
				userId = user.getUserId();
			}

		}
		return userId;
	}

	/**
	 * 
	 * @Title: saveAndGetToken
	 * @param:
	 * @Description: 根据用户userId生成token,并且把auth对象存入redis
	 */
	@Override
	public Auth saveAndGetToken(long userId) {
		Auth auth;
		String token;
		token = this.createToken(userId);
		auth = Auth.builder().id(userId).userId(userId).token(token).authTime(new Date()).build();
		// 把生成的auth对象存入redis
		tokenDao.save(auth);
		return auth;
	}

	/**
	 * 
	 * @Title: detroyAuthRedis
	 * @param:
	 * @Description: 根据userId销毁redis缓存中的auth对象
	 */
	@Override
	public void detroyAuthRedis(long userId) {
		tokenDao.destroyAuth(userId);

	}

	/**
	 * 
	 * @Title: createToken
	 * @param:
	 * @Description: 根据用户userId生成一个token
	 * @return String
	 */
	public String createToken(long userId) {
		return HashGenerator.generateToken(String.valueOf(userId));
	}

	/**
	 * @Title: passwordValidation
	 * @param:
	 * @Description: 通过userId和密码验证登录
	 */
	@Override
	public boolean passwordValidation(long userId, String userpass) {
		Long userIdInDb = authDao.login(userId, userpass);
		if (userIdInDb != null) {
			return true;
		}
		return false;
	}

	@Override
	public UserProfile getUserProfileByUserId(long userId) {
		UserProfile userProfile = userProfileCache.getUserProfileByUserId(userId);
		if (userProfile == null) {
			userProfile = authDao.getUserProfileByUserId(userId);
		}
		if(userProfile!=null) {
			if (StringUtils.isNotBlank(userProfile.getPhone())) {
				userProfile.setPhone(userProfile.getPhone().replaceFirst(userProfile.getCountryCode(), "")
						.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
			}
		}
		return userProfile;
	}

	@Override
	public int updateUser(User user) {
		int result = authDao.updateUser(user);
		if (result > 0) {
			// 更新到redis
			UserProfile userProfile = authDao.getUserProfileByUserId(user.getUserId());
			userProfileCache.update(userProfile);
		}
		return result;
	}

	@Override
	public List<UserProfile> getUserProfileListByUserIds(String userIds, long userId) {
		List<UserProfile> list = Lists.newArrayList();
		// 获取用户基本信息
		String[] userIdArr = userIds.split(",");
		for (String id : userIdArr) {
			UserProfile userProfile = getUserProfileByUserId(Long.valueOf(id));
			list.add(userProfile);
		}
		if (userId > 0) {
			for (UserProfile userProfile : list) {
				int relationship = friendService.getRelationship(userId, userProfile.getUserId());
				userProfile.setRelationship(relationship);
			}
		}
		return list;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public BoUtil updateUserVip(UserVipVo payload) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		// 判断用户余额是否充足
		UserSysPara userSysPara = userSysParaDao.getUserSysParaByInternalCode(ConstantUtil.SYS_VIP);
		if (userSysPara == null) {
			boUtil.setCode(ErrorCode.VIP_SYSTEM_ERROR);
			boUtil.setMsg("vip system error");
			return boUtil;
		}
		double amt = Double.valueOf(userSysPara.getValue());
		// 获取用户余额 扣除余额 TODO

		// 记录订单记录
		MoneyEntityTrans moneyEntityTrans = MoneyEntityTrans.builder().type(1)
				.transInternalCode(SequenceUtil.getInstance().generateOrderId("1")).transFromId(payload.getUserId())
				.amt(amt).status(1).desc(userSysPara.getParaDesc()).createBy(payload.getUserId())
				.activeFlag(CommonConstantUtil.ACTIVE_FLAG_Y).build();
		moneyEntityTransDao.save(moneyEntityTrans);
		User user = User.builder().userId(payload.getUserId()).updateBy(payload.getUserId()).vipType(1).build();
		int result = authDao.updateUser(user);
		if (result > 0) {
			boUtil = BoUtil.getDefaultTrueBo();
		}
		return boUtil;
	}

	@Override
	public List<UserProfile> searchUser(String word) {
		return authDao.searchUser(word);
	}

	@Override
	public List<UserProfile> searchUserLists(List<String> userIdArr) {
		List<UserProfile> list = Lists.newArrayList();
		for (String id : userIdArr) {
			UserProfile userProfile = getUserProfileByUserId(Long.valueOf(id));
			list.add(userProfile);
		}
		return list;
	}

}