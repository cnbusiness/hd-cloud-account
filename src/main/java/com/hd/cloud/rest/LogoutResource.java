package com.hd.cloud.rest;

import javax.inject.Inject;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.service.AuthService;
import com.hd.cloud.util.ErrorCode;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.controller.RestBase;

/**
 * 
 * @ClassName: LogoutResource
 * @Description: 用户 退出
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月21日 上午9:45:06
 *
 */
@RefreshScope
@RestController
@RequestMapping("/logout")
public class LogoutResource extends RestBase {

	@Inject
	private AuthService authService;

	/**
	 * 
	 * @Title: userLogout
	 * @param:
	 * @Description: 退出
	 * @return BoUtil
	 */
	@ResponseBody
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public BoUtil userLogout() {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		long loginUserId = super.getLoginUserID();
		if (loginUserId == 0) {
			boUtil.setMsg("userId不能为空");
			boUtil.setCode(ErrorCode.USERID_IS_EMPTY);
			return boUtil;
		}
		// 从redis缓存中销毁用户的token
		authService.detroyAuthRedis(loginUserId);
		BoUtil bo = BoUtil.getDefaultTrueBo();
		bo.setMsg("user logout Success!");
		return bo;
	}
}
