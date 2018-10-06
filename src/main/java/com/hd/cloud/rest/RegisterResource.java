package com.hd.cloud.rest;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.AccountServiceApplication.RSAConfig;
import com.hd.cloud.bo.Auth;
import com.hd.cloud.bo.Sms;
import com.hd.cloud.feign.SmsClient;
import com.hd.cloud.service.UserService;
import com.hd.cloud.util.ConstantUtil;
import com.hd.cloud.util.ErrorCode;
import com.hd.cloud.util.IosRSAUtil;
import com.hd.cloud.util.RSAUtil;
import com.hd.cloud.vo.GetSmsVo;
import com.hd.cloud.vo.UserRegVo;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.util.CommonErrorCode;
import com.hlb.cloud.util.ValidateUtil;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: RegisterResource
 * @Description: 注册
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月4日 下午4:12:04
 *
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("sso")
public class RegisterResource {

	@Autowired
	private RSAConfig rSAConfig;

	@Autowired
	private UserService userService;

	@Autowired
	private SmsClient smsClient;

	/**
	 * 
	 * @Title: registered
	 * @param:
	 * @Description: 注册
	 * @return BoUtil
	 * @throws Exception
	 */
	@ApiOperation(httpMethod = "POST", value = "reg", notes = "reg")
	@ResponseBody
	@RequestMapping(value = "register", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil registered(final @RequestBody UserRegVo payload) throws Exception {
		// set the default return object
		BoUtil bo = BoUtil.getDefaultTrueBo();

		// phone number format
		if (StringUtils.isBlank(payload.getPhoneNo())
				|| ValidateUtil.validatePhone(payload.getCountryCode(), payload.getPhoneNo()) == false) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("the phone number format is incorrect!");
			bo.setCode(ErrorCode.PHONE_FORMAT_ERROR);
			return bo;
		}
		if (StringUtils.isBlank(payload.getCaptcha())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("the captcha is empty!");
			bo.setCode(ErrorCode.CAPTCHA_EMPTY_ERROR);
			return bo;
		}
		if (StringUtils.isBlank(payload.getPassword())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("The phone number or password can`t be blank.");
			bo.setCode(ErrorCode.ACCOUNT_PWD_EMPTY_ERROR);
			return bo;
		}
		if (Pattern.compile("[\u0391-\uFFE5]+", Pattern.CASE_INSENSITIVE).matcher(payload.getPassword()).find()) { // 判断密码是否有中文和中文符号
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("Password is chinese error");
			bo.setCode(ErrorCode.PASSWORD_IS_CHINESE_ERROR);
			return bo;
		}

		// 判断是否已经注册
		if (userService.hasRegisterByPhoneNo(payload.getPhoneNo()) != null) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.REGISTER_ERROR);
			bo.setMsg("account had been registed.");
			return bo;
		}

		// 检测验证码
		GetSmsVo getSmsVo = GetSmsVo.builder().countryCode(payload.getCountryCode())
				.mobile(payload.getPhoneNo().replaceFirst(payload.getCountryCode(), ""))
				.captchaType(ConstantUtil.REG_BY_PHONE).build();
		log.info("###getSmsVo:{}", getSmsVo);
		Sms sms = smsClient.checkCaptchaByPhoneNo(getSmsVo);
		if (sms == null) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("the captcha is not match!");
			bo.setCode(ErrorCode.CAPTCHA_NOT_MATCH_ERROR);
			return bo;
		} else {
			if (!sms.getCode().equals(payload.getCaptcha())) {
				bo = BoUtil.getDefaultFalseBo();
				bo.setMsg("the captcha is not match!");
				bo.setCode(ErrorCode.CAPTCHA_NOT_MATCH_ERROR);
				return bo;
			}
		} 
		String md5Pwd = "";
		if (payload.getAppType() == 1) {
			md5Pwd = RSAUtil.decryptAndSha256(payload.getPassword(), rSAConfig.getPrivateKey());
			log.info("###################payload:{},password:{}", payload,md5Pwd);
		} else {
			md5Pwd = IosRSAUtil.decryptAndSha256(payload.getPassword(), rSAConfig.getPrivateKey());
			log.info("###########  IOS md5Pwd ={} ", md5Pwd);
		}
		log.info("######## decrypt testing decryptedStr = {}", md5Pwd);
		payload.setPassword(md5Pwd);
		Auth authBo;
		try {
			// 注册
			authBo = userService.createUser(payload);
		} catch (Exception e) {
			e.printStackTrace();
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.ACCOUN_REGISTED_ERROR);
			bo.setMsg("register failed.");
			return bo;
		}
		if (authBo.getUserId() <= 0) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.ACCOUN_REGISTED_ERROR);
			bo.setMsg("register failed.");
			return bo;
		} else {
			bo.setResult(true);
			bo.setCode(CommonErrorCode.SUCCESS);
			bo.setMsg("Success.");
			// 改成直接返回authBo
			bo.setData(authBo);
		}

		if (sms != null) {
			// 所有执行成功后-- 修改验证状态
			sms.setStatus(1);
			smsClient.updateSms(sms);
			bo.setResult(true);
		}
		return bo;
	}

}
