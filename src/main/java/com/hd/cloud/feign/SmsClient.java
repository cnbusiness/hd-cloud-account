package com.hd.cloud.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hd.cloud.bo.Sms;
import com.hd.cloud.hystrix.HystrixWrappedSmsClient;
import com.hd.cloud.vo.GetSmsVo;
import com.hlb.cloud.bo.BoUtil;

/**
 * 
 * @ClassName: SmsClient
 * @Description: 发送短信
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午3:51:25
 *
 */
@FeignClient(name = "hd-cloud-thirdparty",fallback=HystrixWrappedSmsClient.class)
public interface SmsClient {

	/**
	 * 
	 * @Title: sendSms
	 * @param:
	 * @Description: 发送验证码
	 * @return BoUtil
	 */
	@RequestMapping(value = "/sms", method = RequestMethod.POST)
	public BoUtil sendSms(@RequestBody Sms sms);

	/**
	 * 
	 * @Title: checkCaptchaByPhoneNo
	 * @param:
	 * @Description: 验证短信验证码
	 * @return Sms
	 */
	@RequestMapping(value = "/sms/verificationcode", method = RequestMethod.POST)
	public Sms checkCaptchaByPhoneNo(@RequestBody GetSmsVo getSmsVo);

	/**
	 * 
	 * @Title: updateSms
	 * @param:
	 * @Description: 修改验证码状态
	 * @return BoUtil
	 */
	@RequestMapping(value = "/sms/verificationcode", method = RequestMethod.PUT)
	public BoUtil updateSms(final @RequestBody Sms sms);
}
