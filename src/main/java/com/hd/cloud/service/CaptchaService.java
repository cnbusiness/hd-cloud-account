
package com.hd.cloud.service;

/**
 * 
 * @ClassName: CaptchaService
 * @Description: 验证服务
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年12月7日 下午4:42:17
 *
 */
public interface CaptchaService {
	/**
	 * 
	 * @Title: isAbleCheckCaptcha
	 * @param:
	 * @Description: 是否能够验证码
	 * @return boolean
	 */
	public boolean isAbleCheckCaptcha(String phone, int type);

	/**
	 * 
	 * @Title: recordCaptchaError
	 * @param:
	 * @Description: 记录验证码错误次数
	 * @return void
	 */
	public void recordCaptchaError(String phone, int type);

	/**
	 * 
	 * @Title: delCaptchaError
	 * @param:
	 * @Description: 删除验证码次数
	 * @return void
	 */
	public void delCaptchaError(String phone, int type);

}
