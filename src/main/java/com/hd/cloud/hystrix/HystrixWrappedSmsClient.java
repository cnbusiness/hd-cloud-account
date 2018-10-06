package com.hd.cloud.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hd.cloud.bo.Sms;
import com.hd.cloud.feign.SmsClient;
import com.hd.cloud.vo.GetSmsVo;
import com.hlb.cloud.bo.BoUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * 
 * @ClassName: HystrixWrappedSmsClient
 * @Description: 发送业务短信
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月23日 下午3:53:02
 *
 */
@Service
public class HystrixWrappedSmsClient implements SmsClient {

	@Autowired
	private SmsClient smsClient;

	@Override
	@HystrixCommand(groupKey = "sendSms", fallbackMethod = "sendSmsFailBackCall")
	public BoUtil sendSms(Sms sms) {
		return smsClient.sendSms(sms);
	}

	public BoUtil sendSmsFailBackCall(Sms sms) {
		return BoUtil.getDefaultFalseBo();
	}

	@Override
	@HystrixCommand(groupKey = "checkCaptchaByPhoneNo", fallbackMethod = "checkCaptchaByPhoneNoFailBackCall")
	public Sms checkCaptchaByPhoneNo(GetSmsVo getSmsVo) {
		return smsClient.checkCaptchaByPhoneNo(getSmsVo);
	}

	public Sms checkCaptchaByPhoneNoFailBackCall(GetSmsVo getSmsVo) {
		return null;
	}

	@Override
	@HystrixCommand(groupKey = "updateSms", fallbackMethod = "updateSmsFailBackCall")
	public BoUtil updateSms(Sms sms) {
		return smsClient.updateSms(sms);
	}

	public BoUtil updateSmsFailBackCall(Sms sms) {
		return BoUtil.getDefaultFalseBo();
	}
}
