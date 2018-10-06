package com.hd.cloud.rest;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.AccountServiceApplication.RSAConfig;
import com.hd.cloud.bo.Auth;
import com.hd.cloud.service.AuthService;
import com.hd.cloud.util.ErrorCode;
import com.hd.cloud.util.IosRSAUtil;
import com.hd.cloud.util.RSAUtil;
import com.hd.cloud.vo.ValidatePassVo;
import com.hd.cloud.vo.ValidateTokenVo;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.util.CommonErrorCode;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: AuthResource
 * @Description: 用户登录
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月21日 上午9:44:46
 *
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/auth")
public class AuthResource {

	@Inject
	private AuthService authService;

	@Autowired
	private RSAConfig rSAConfig;

	/**
	 * 
	 * @Title: validateToken
	 * @param:
	 * @Description: 根据传入的userId与Token，验证token是否有效
	 * @return BoUtil
	 */
	@ResponseBody
	@RequestMapping(value = "/valid", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil validateToken(@RequestBody ValidateTokenVo validateTokenVo) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		long userId = validateTokenVo.getUserId();
		String token = validateTokenVo.getToken();
		if (userId == 0) {
			boUtil.setMsg("userId不能为空");
			boUtil.setCode(ErrorCode.USERID_IS_EMPTY);
			return boUtil;
		}
		if (token.trim().equals("") || token == null) {
			boUtil.setMsg("token不能为空");
			boUtil.setCode(ErrorCode.TOKEN_IS_EMPTY);
			return boUtil;
		}
		// 传入userId与token进行验证
		boolean isValid = authService.isValidTokenPare(userId, token);
		boUtil.setResult(isValid);
		if (isValid) {
			boUtil.setMsg("token验证成功");
			boUtil.setCode(CommonErrorCode.SUCCESS);
			log.info("userId = {} validate token success", userId);
		} else {
			boUtil.setMsg("token验证失败");
			boUtil.setCode(ErrorCode.TOKEN_FAIL);
			log.info("userId = {} validate token failure", userId);
		}
		return boUtil;
	}

	/**
	 * 
	 * @Title: validatePass
	 * @param:
	 * @Description: 根据传入的用户账号与密码，进行登录验证。账号可以使手机号或邮箱
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "登录", notes = "登录")
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil validatePass(@RequestBody ValidatePassVo validatePassVo) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		String userAccount = validatePassVo.getUseraccount();
		String userPass = validatePassVo.getUserpass();
		if (userAccount == null || userPass == null || userAccount.trim() == "" || userPass.trim() == "") {
			boUtil.setMsg("用户名和密码不能为空");
			boUtil.setCode(ErrorCode.USERNAME_PASSWORD_EMPTY);
			return boUtil;
		}
		// 传入登录时填写的手机号或者邮箱，返回用户的userId，最终是依靠userId与密码进行登录验证
		long userId = authService.getUserIdByAccount(userAccount);

		// IOS RSA 特殊处理
		String decryptedStr = "";
		if (validatePassVo.getAppType() == 2) {
			decryptedStr = IosRSAUtil.decryptAndSha256(userPass, rSAConfig.getPrivateKey());
			log.info("###########  IOS decryptedStr = " + decryptedStr);
		} else {
			// 解密之后 md5加密
			decryptedStr = RSAUtil.decryptAndSha256(userPass, rSAConfig.getPrivateKey());
			log.info("###########  Android  decryptedStr = " + decryptedStr);
		}
		Auth auth = authService.isValidatePassPare(userId, decryptedStr);
		log.debug(" decrypt testing decryptedStr = {}", decryptedStr);

		if (auth == null) {
			boUtil.setMsg("用户名或密码错误");
			boUtil.setCode(ErrorCode.USERNAME_OR_PASSWORD_ERROR);
			return boUtil;
		}
		log.info("userId = {} login success! login account ={}", userId, userAccount);
		BoUtil bo = BoUtil.getDefaultTrueBo();
		bo.setMsg("login Success!");
		bo.setCode(CommonErrorCode.SUCCESS);
		bo.setData(auth);
		return bo;
	}

}
