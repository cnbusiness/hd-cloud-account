package com.hd.cloud.rest;

import java.util.regex.Pattern;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.AccountServiceApplication.RSAConfig;
import com.hd.cloud.bo.Sms;
import com.hd.cloud.bo.User;
import com.hd.cloud.feign.SmsClient;
import com.hd.cloud.service.UserService;
import com.hd.cloud.util.ConstantUtil;
import com.hd.cloud.util.ErrorCode;
import com.hd.cloud.util.IosRSAUtil;
import com.hd.cloud.util.RSAUtil;
import com.hd.cloud.vo.CheckPhoneVo;
import com.hd.cloud.vo.FindUserPwdVo;
import com.hd.cloud.vo.GetSmsVo;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.util.CommonConstantUtil;
import com.hlb.cloud.util.CommonErrorCode;
import com.hlb.cloud.util.ValidateUtil;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: ForgetResource
 * @Description: 忘记密码
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年3月15日 下午4:10:26
 *
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("forget")
public class ForgetResource {

	@Autowired
	private RSAConfig rSAConfig;

	@Autowired
	private SmsClient smsClient;

	@Inject
	private UserService userService;

	/**
	 * 
	 * @Title: findpwd
	 * @param:
	 * @Description: 找回密码
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "password", notes = "password")
	@ResponseBody
	@RequestMapping(value = "password", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil findpwd(final @RequestBody FindUserPwdVo payload) {
		BoUtil bo = BoUtil.getDefaultTrueBo();
		// phone number format
		if (StringUtils.isBlank(payload.getPhoneNo())
				|| ValidateUtil.validatePhone(payload.getCountryCode(), payload.getPhoneNo()) == false) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.PHONE_FORMAT_ERROR);
			bo.setMsg("the phone number format is incorrect!");
			return bo;
		}
		if (StringUtils.isBlank(payload.getCaptcha())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.CAPTCHA_EMPTY_ERROR);
			bo.setMsg("the captcha is empty!");
			return bo;
		}
		if (StringUtils.isBlank(payload.getPassword())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.ACCOUNT_PWD_EMPTY_ERROR);
			bo.setMsg("The phone number or password can`t be blank.");
			return bo;
		}
		if (Pattern.compile("[\u0391-\uFFE5]+", Pattern.CASE_INSENSITIVE).matcher(payload.getPassword()).find()) { // 判断密码是否有中文和中文符号
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.PASSWORD_IS_CHINESE_ERROR);
			bo.setMsg("Password is chinese error");
			return bo;
		}
		if (!payload.getPassword().equals(payload.getRePassword())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.PWD_NOT_SAME_ERROR);
			bo.setMsg("Confirm password inconsistency!");
			return bo;
		}

		// 检测验证码
		GetSmsVo getSmsVo = GetSmsVo.builder().countryCode(payload.getCountryCode())
				.mobile(payload.getPhoneNo().replaceFirst(payload.getCountryCode(), ""))
				.captchaType(ConstantUtil.FORGET_PASSWORD_BY_PHONE).build();
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

		User basicUser = userService.hasRegisterByPhoneNo(payload.getPhoneNo());
		if (basicUser == null) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.ACCOUNT_IS_NOT_EXIST);
			bo.setMsg("account is not exist!");
			return bo;
		}
		bo.setResult(true);

		String md5Pwd = "";
		if (payload.getAppType() == 1) {
			md5Pwd = RSAUtil.decryptAndSha256(payload.getRePassword(), rSAConfig.getPrivateKey());
			log.debug("######## decrypt testing md5Pwd = {}", md5Pwd);
		} else if (payload.getAppType() == 2) {
			md5Pwd = IosRSAUtil.decryptAndSha256(payload.getRePassword(), rSAConfig.getPrivateKey());
			log.info("###########  IOS md5Pwd ={} ", md5Pwd);
		}
		// 修改密码
		basicUser.setPassword(md5Pwd);
		int result = userService.update(basicUser);
		log.info("Auth={}", result);

		bo.setCode(CommonErrorCode.SUCCESS);
		bo.setMsg("success");
		// 所有成功后改变验证码状态
		if (sms != null) {
			sms.setStatus(ConstantUtil.VerifyCodeStatus.INVALID);
			sms.setActiveFlag(CommonConstantUtil.ACTIVE_FLAG_D);
			smsClient.updateSms(sms);
			bo.setResult(true);
		}
		return bo;
	}

	/**
	 * 
	 * @Title: checkPhoneNo
	 * @param:
	 * @Description: 发送验证码
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "checkphoneno", notes = "checkphoneno")
	@ResponseBody
	@RequestMapping(value = "checkphoneno", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil checkPhoneNo(final @RequestBody CheckPhoneVo checkPhoneVo) {
		BoUtil bo = BoUtil.getDefaultFalseBo();
		String countryCode = checkPhoneVo.getCountryCode();
		String phoneNo = checkPhoneVo.getPhoneNo();
		log.info("countryCode={},phoneNo={}", countryCode, phoneNo);

		if (StringUtils.isBlank(countryCode)) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.PHONE_EMPTY);
			bo.setMsg("countrycode can't be null");
			return bo;
		}
		if (StringUtils.isBlank(phoneNo)) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.PHONE_EMPTY);
			bo.setMsg("phone number can't be null");
			return bo;
		}
		if (!ValidateUtil.validatePhone(countryCode, phoneNo)) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.PHONE_FORMAT_ERROR);
			bo.setMsg("phone number format error");
			return bo;
		}
		User saveValidate = userService.hasRegisterByPhoneNo(checkPhoneVo.getPhoneNo());
		if (checkPhoneVo.getType() == 1) {
			// 判断手机是否已被绑定
			if (null != saveValidate) {
				bo = BoUtil.getDefaultFalseBo();
				bo.setCode(ErrorCode.PHONE_IS_BINDING);
				bo.setMsg("the phone has Binding!");
				return bo;
			}
		} else {
			if (saveValidate == null) {
				bo = BoUtil.getDefaultFalseBo();
				bo.setCode(ErrorCode.ACCOUNT_IS_NOT_EXIST);
				bo.setMsg("user account is not exist");
				return bo;
			}
		}
		String newPhoneNo = phoneNo.replaceFirst(countryCode, "");
		Sms sms = Sms.builder().countryCode(countryCode).recordAccount(newPhoneNo).codeType(checkPhoneVo.getType())
				.checkType(checkPhoneVo.getType()).build();
		return smsClient.sendSms(sms);
	}

}
