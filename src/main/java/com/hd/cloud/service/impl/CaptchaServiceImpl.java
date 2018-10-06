
package com.hd.cloud.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.hd.cloud.dao.CaptchaCache;
import com.hd.cloud.service.CaptchaService;

/**
 * 
 * @ClassName: CaptchaServiceImpl
 * @Description: 验证
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年12月7日 下午4:42:01
 *
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

	@Inject
	private CaptchaCache captchaCache;

	@Override
	public boolean isAbleCheckCaptcha(String phone, int type) {
		// i 表示之前错误的次数，不包括当前这次
		int i = captchaCache.getCaptchaErrCount(phone, type);
		// 查询出来判断如果存在并且大于5就返回false
		if (i >= 5) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void recordCaptchaError(String phone, int type) {
		captchaCache.recordCaptchaError(phone, type);

	}

	@Override
	public void delCaptchaError(String phone, int type) {
		captchaCache.delCaptchaError(phone, type);
	}

}
