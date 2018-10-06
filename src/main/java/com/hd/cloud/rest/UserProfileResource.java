package com.hd.cloud.rest;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.hd.cloud.AccountServiceApplication.RSAConfig;
import com.hd.cloud.bo.User;
import com.hd.cloud.bo.UserProfile;
import com.hd.cloud.bo.UserSysPara;
import com.hd.cloud.service.AuthService;
import com.hd.cloud.service.UserSysParaService;
import com.hd.cloud.util.ErrorCode;
import com.hd.cloud.util.IosRSAUtil;
import com.hd.cloud.util.RSAUtil;
import com.hd.cloud.vo.UserProfileVo;
import com.hd.cloud.vo.UserVipVo;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.controller.RestBase;
import com.hlb.cloud.util.StringUtil;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: UserProfileResource
 * @Description: 用户管理
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月8日 上午11:23:27
 *
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("userprofile")
public class UserProfileResource extends RestBase {

	@Inject
	private AuthService authService;

	@Inject
	private UserSysParaService userSysParaService;

	@Autowired
	private RSAConfig rSAConfig;

	/**
	 * 
	 * @Title: getUserProfile
	 * @param:
	 * @Description: 登录用户详情
	 * @return BoUtil
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public BoUtil getUserProfile() {
		BoUtil bo = BoUtil.getDefaultTrueBo();
		UserProfile userProfile = authService.getUserProfileByUserId(getLoginUserID());
		log.info("##############userProfile:{}", userProfile);
		bo.setData(userProfile);
		return bo;
	}

	/**
	 * 
	 * @Title: updateUser
	 * @param:
	 * @Description: 编辑用户信息
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "updateUser", notes = "updateUser")
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public BoUtil updateUser(final @RequestBody User user) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		if (StringUtils.isBlank(user.getNickName())) {
			boUtil.setCode(ErrorCode.NICKNAME_EMPTY_ERROR);
			boUtil.setMsg("NickName is empty");
			return boUtil;
		}
		if (user.getNickName().length() > 40) {
			boUtil.setCode(ErrorCode.NICKNAME_LENGTH_ERROR);
			boUtil.setMsg("Nickname within the 40-character length limit");
			return boUtil;
		}
		user.setUserId(getLoginUserID());
		user.setUpdateBy(getLoginUserID());
		int result = authService.updateUser(user);
		if (result > 0) {
			boUtil = BoUtil.getDefaultTrueBo();
		}
		return boUtil;
	}

	/**
	 * 
	 * @Title: updateUserVip
	 * @param:
	 * @Description: 用户vip升级
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "updateUser", notes = "updateUser")
	@ResponseBody
	@RequestMapping(value = "/vip", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public BoUtil updateUserVip(final @RequestBody UserVipVo payload) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		if (StringUtils.isBlank(payload.getPassword())) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setMsg("Please enter your payment password!");
			boUtil.setCode(ErrorCode.PAYMENT_PASSWORD_EMPTY);
			return boUtil;
		}
		// 解密 RSA 加密
		String oldPwd = "";
		if (payload.getAppType() == 2) {
			oldPwd = IosRSAUtil.decryptAndSha256(payload.getPassword(), rSAConfig.getPrivateKey());
			log.info("########### userId:{}, IOS md5Pwd ={} ", getLoginUserID(), oldPwd);
		} else {
			oldPwd = RSAUtil.decryptAndSha256(payload.getPassword(), rSAConfig.getPrivateKey());
		}
		payload.setPassword(oldPwd);
		payload.setUserId(getLoginUserID());
		boUtil = authService.updateUserVip(payload);
		return boUtil;
	}

	/**
	 * 
	 * @Title: getSysPara
	 * @param:
	 * @Description: 获取系统配置 升级vip会员信息
	 * @return BoUtil
	 */
	@ResponseBody
	@RequestMapping(value = "/syspara/{internalCode}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public BoUtil getSysPara(@PathVariable("internalCode") String internalCode) {
		BoUtil bo = BoUtil.getDefaultTrueBo();
		UserSysPara userSysPara = userSysParaService.getUserSysParaByInternalCode(internalCode);
		log.info("##############userSysPara:{}", userSysPara);
		bo.setData(userSysPara);
		return bo;
	}

	/**
	 * 
	 * @Title: getUserProfileByUserId
	 * @param:
	 * @Description: userId获取用户详情
	 * @return BoUtil
	 */
	@ResponseBody
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public UserProfile getUserProfileByUserId(@PathVariable("userId") long userId) {
		UserProfile userProfile = authService.getUserProfileByUserId(userId);
		log.info("##############userProfile:{}", userProfile);
		return userProfile;
	}

	/**
	 * 
	 * @Title: getUserProfileList
	 * @param:
	 * @Description: 批量获取用户信息
	 * @return UserProfile
	 */
	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<UserProfile> getUserProfileList(final @RequestBody UserProfileVo userProfileVo) {
		List<UserProfile> list = authService.getUserProfileListByUserIds(userProfileVo.getUserIds(),
				userProfileVo.getUserId());
		log.info("##############list:{}", list);
		return list;
	}
	
	@ResponseBody
	@RequestMapping(value = "/search", method = RequestMethod.GET,produces = "application/json", consumes = "application/*")
	public BoUtil searchUser(final @QueryParam("word") String word,@QueryParam("type") Integer type) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		type = type == null ? 0 : type;
		List<UserProfile> list = null;
		if(type == 1){//搜索用户
			list = authService.searchUser(word);
		}else{//好友列表
			List<String> strList = Lists.newArrayList();
			if(!StringUtil.isBlank(word)){
				String[] arr = word.split(",");
				for (int i = 0; i < arr.length; i++) {
					strList.add(arr[i]);
				}
				list = authService.searchUserLists(strList);
			}
		}
		boUtil.setData(list);
		return boUtil;
	}
	
}
