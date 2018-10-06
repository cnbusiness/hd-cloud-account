
package com.hd.cloud.dao;

/**
 * 
  * @ClassName: CaptchaCache
  * @Description: 验证码Cache方法
  * @author ShengHao shenghaohao@hadoop-tech.com
  * @Company hadoop-tech 
  * @date 2017年12月7日 下午4:44:09
  *
 */
public interface CaptchaCache {
	
	/**
	 * 
	 * @Title: recordCaptchaError 
	 * @Description: 记录验证码错误次数
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	public void recordCaptchaError(String phone,int type);
	
	/**
	 * 
	 * @Title: delCaptchaError 
	 * @Description: 清除验证码错误次数
	 * @param @param userId
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @throws
	 */
	public void delCaptchaError(String phone,int type);
	
	/**
	 * 
	 * @Title: getCaptchaErrCount 
	 * @Description: 获得验证码错误次数
	 * @param @param userId
	 * @param @param type
	 * @param @return    设定文件 
	 * @return int    返回类型 
	 * @throws
	 */
	public int getCaptchaErrCount(String phone,int type);
	

}
