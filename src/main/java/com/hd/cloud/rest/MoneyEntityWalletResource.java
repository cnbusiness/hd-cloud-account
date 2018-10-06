package com.hd.cloud.rest;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.AccountServiceApplication.RSAConfig;
import com.hd.cloud.bo.MoneyEntityWallet;
import com.hd.cloud.bo.Sms;
import com.hd.cloud.feign.SmsClient;
import com.hd.cloud.service.MoneyEntityWalletService;
import com.hd.cloud.util.ConstantUtil;
import com.hd.cloud.util.ErrorCode;
import com.hd.cloud.util.IosRSAUtil;
import com.hd.cloud.util.RSAUtil;
import com.hd.cloud.vo.ForgetPaymentPasswordVo;
import com.hd.cloud.vo.GetSmsVo;
import com.hd.cloud.vo.SetPaymentPasswordVo;
import com.hd.cloud.vo.UpdatePaymentPasswordVo;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.controller.RestBase;
import com.hlb.cloud.util.ValidateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: MoneyEntityWalletResource
 * @Description: 钱包管理
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月17日 下午5:08:46
 *
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("wallet")
public class MoneyEntityWalletResource extends RestBase {

	@Inject
	private MoneyEntityWalletService moneyEntityWalletService;

	@Autowired
	private RSAConfig rSAConfig;

	@Autowired
	private SmsClient smsClient;

	/**
	 * 
	 * @Title: setPaymentPassword
	 * @param:
	 * @Description: 设置支付密码
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "setPaymentPassword", notes = "setPaymentPassword")
	@ResponseBody
	@RequestMapping(value = "/payment/passsword", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil setPaymentPassword(final @RequestBody SetPaymentPasswordVo payload) {
		log.info("##########设置支付密码payload:{}", payload);
		BoUtil bo = BoUtil.getDefaultFalseBo();
		if (StringUtils.isBlank(payload.getPassword())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("Please enter your payment password!");
			bo.setCode(ErrorCode.PAYMENT_PASSWORD_EMPTY);
			return bo;
		}
		if (!payload.getPassword().equals(payload.getConfirmPassword())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("Confirm password inconsistency!");
			bo.setCode(ErrorCode.PWD_NOT_SAME_ERROR);
			return bo;
		}
		// 解密 RSA 加密
		String oldPwd = "";
		if (payload.getAppType() == 2) {
			oldPwd = IosRSAUtil.decryptAndSha256(payload.getPassword(), rSAConfig.getPrivateKey());
			log.info("########### userId:{}, IOS md5Pwd ={} ", getLoginUserID(), oldPwd);
		} else {
			oldPwd = RSAUtil.decryptAndSha256(payload.getPassword(), rSAConfig.getPrivateKey());
		}
		MoneyEntityWallet moneyEntityWallet = MoneyEntityWallet.builder().walletId(getLoginUserID())
				.updateBy(getLoginUserID()).type(payload.getType()).payPassword(oldPwd).build();
		int result = moneyEntityWalletService.update(moneyEntityWallet);
		if (result > 0) {
			bo = BoUtil.getDefaultTrueBo();
			return bo;
		} else {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("update password fail!");
			bo.setCode(ErrorCode.UPDATE_PASSWORDA_FAIL);
			return bo;
		}
	}

	/**
	 * 
	 * @Title: forgetPaymentPassword
	 * @param:
	 * @Description: 找回支付密码
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "forgetPaymentPassword", notes = "forgetPaymentPassword")
	@ResponseBody
	@RequestMapping(value = "/forget/payment/passsword", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil forgetPaymentPassword(final @RequestBody ForgetPaymentPasswordVo payload) {
		log.info("##########找回支付密码payload:{}", payload);
		BoUtil bo = BoUtil.getDefaultFalseBo();

		if (StringUtils.isBlank(payload.getPhone())
				|| ValidateUtil.validatePhone(payload.getCountryCode(), payload.getPhone()) == false) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setCode(ErrorCode.PHONE_FORMAT_ERROR);
			bo.setMsg("the phone number format is incorrect!");
			return bo;
		}

		if (StringUtils.isBlank(payload.getPassword())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("Please enter your payment password!");
			bo.setCode(ErrorCode.PAYMENT_PASSWORD_EMPTY);
			return bo;
		}
		if (StringUtils.isBlank(payload.getCaptcha())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("the captcha is empty!");
			bo.setCode(ErrorCode.CAPTCHA_EMPTY_ERROR);
			return bo;
		}
		if (!payload.getPassword().equals(payload.getConfirmPassword())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("Confirm password inconsistency!");
			bo.setCode(ErrorCode.PWD_NOT_SAME_ERROR);
			return bo;
		}

		GetSmsVo getSmsVo = GetSmsVo.builder().countryCode(payload.getCountryCode())
				.mobile(payload.getPhone().replaceFirst(payload.getCountryCode(), ""))
				.captchaType(ConstantUtil.FORGET_PAYMENT_PASSWORD_BY_PHONE).build();
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
		// 解密 RSA 加密
		String oldPwd = "";
		if (payload.getAppType() == 2) {
			oldPwd = IosRSAUtil.decryptAndSha256(payload.getPassword(), rSAConfig.getPrivateKey());
			log.info("###########userId:{},  IOS md5Pwd ={} ", getLoginUserID(), oldPwd);
		} else {
			oldPwd = RSAUtil.decryptAndSha256(payload.getPassword(), rSAConfig.getPrivateKey());
		}
		MoneyEntityWallet moneyEntityWallet = MoneyEntityWallet.builder().walletId(getLoginUserID()).type(payload.getType())
				.payPassword(oldPwd).updateBy(getLoginUserID()).build();
		int result = moneyEntityWalletService.update(moneyEntityWallet);
		if (result > 0) {
			bo = BoUtil.getDefaultTrueBo();
			return bo;
		} else {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("update password fail!");
			bo.setCode(ErrorCode.UPDATE_PASSWORDA_FAIL);
			return bo;
		}
	}
	
	
	/**
	 * 
	 * @Title: updatePaymentPassword
	 * @param:
	 * @Description: 修改支付密码
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "updatePaymentPassword", notes = "updatePaymentPassword")
	@ResponseBody
	@RequestMapping(value = "/payment/passsword", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public BoUtil updatePaymentPassword(final @RequestBody UpdatePaymentPasswordVo payload) {
		log.info("##########修改支付密码payload:{}", payload);
		BoUtil bo = BoUtil.getDefaultFalseBo();
		if (StringUtils.isBlank(payload.getOldPassword())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("Please enter your old password!");
			bo.setCode(ErrorCode.OLD_PASSWORD_EMPTY);
			return bo;
		}
		if (StringUtils.isBlank(payload.getNewPassWord())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("Please enter your new password!");
			bo.setCode(ErrorCode.NEW_PASSWORD_EMPTY);
			return bo;
		}
		if (StringUtils.isBlank(payload.getConfirmPassword())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("Please enter your confirm password!");
			bo.setCode(ErrorCode.CONFIRM_PASSWORD_EMPTY);
			return bo;
		}
		if (!payload.getNewPassWord().equals(payload.getConfirmPassword())) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("Confirm password inconsistency!");
			bo.setCode(ErrorCode.PWD_NOT_SAME_ERROR);
			return bo;
		}
		// 解密 RSA 加密
		String oldPwd = "";
		if (payload.getAppType() == 2) {
			oldPwd = IosRSAUtil.decryptAndSha256(payload.getOldPassword(), rSAConfig.getPrivateKey());
			log.info("###########  IOS md5Pwd ={} ", oldPwd);
		} else {
			oldPwd = RSAUtil.decryptAndSha256(payload.getOldPassword(), rSAConfig.getPrivateKey());
		}

		MoneyEntityWallet moneyEntityWallet = moneyEntityWalletService.findMoneyEntityWalletByUserIdAndPassword(getLoginUserID(),payload.getType(), oldPwd);
		if (moneyEntityWallet == null) {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("Old password wrong!");
			bo.setCode(ErrorCode.OLD_PASSWORD_ERROR);
			return bo;
		}
		// 新密码
		String newPassword = "";
		if (payload.getAppType() == 2) {
			newPassword = IosRSAUtil.decryptAndSha256(payload.getNewPassWord(), rSAConfig.getPrivateKey());
			log.info("###########  IOS newPassword ={} ", newPassword);
		} else {
			newPassword = RSAUtil.decryptAndSha256(payload.getNewPassWord(), rSAConfig.getPrivateKey());
		}
		moneyEntityWallet.setType(payload.getType());
		moneyEntityWallet.setPayPassword(newPassword);
		int result = moneyEntityWalletService.update(moneyEntityWallet);
		if (result > 0) {
			bo = BoUtil.getDefaultTrueBo();
			return bo;
		} else {
			bo = BoUtil.getDefaultFalseBo();
			bo.setMsg("update password fail!");
			bo.setCode(ErrorCode.UPDATE_PASSWORDA_FAIL);
			return bo;
		}
	}
	

	/**
	 * 
	 * @Title: getMoneyEntityWalletByWalletIdAndType
	 * @param:
	 * @Description: 获取用户钱包信息 (提供给其他微服务调用的接口)
	 * @return MoneyEntityWallet
	 */
	@ResponseBody
	@RequestMapping(value = "/{type}/{walletId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public MoneyEntityWallet getMoneyEntityWalletByWalletIdAndType(@PathVariable("type") int type,
			@PathVariable("walletId") long walletId) {
		MoneyEntityWallet moneyEntityWallet = moneyEntityWalletService.getMoneyEntityWalletByWalletIdAndType(walletId,
				type);
		return moneyEntityWallet;
	}

	/**
	 * 
	 * @Title: update
	 * @param:
	 * @Description: 编辑 (提供给其他微服务调用的接口)
	 * @return int
	 */
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public int update(@RequestBody MoneyEntityWallet moneyEntityWallet) {
		return moneyEntityWalletService.update(moneyEntityWallet);
	}
}
