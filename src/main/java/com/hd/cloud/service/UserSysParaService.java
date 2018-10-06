package com.hd.cloud.service;

import com.hd.cloud.bo.UserSysPara;

/**
 * 
 * @ClassName: UserSysParaService
 * @Description: 用户参数
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年5月2日 上午9:54:00
 *
 */
public interface UserSysParaService {

	/**
	 * 
	 * @Title: getUserSysParaByInternalCode
	 * @param:
	 * @Description: 根据参数查询
	 * @return UserSysPara
	 */
	public UserSysPara getUserSysParaByInternalCode(String internalCode);
}
