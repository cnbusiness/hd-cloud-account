package com.hd.cloud.service;

import java.io.UnsupportedEncodingException;

import com.hd.cloud.bo.Auth;
import com.hd.cloud.bo.User;
import com.hd.cloud.vo.UserRegVo;

/**
 * 
 * @ClassName: UserService
 * @Description: 用户管理
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月4日 下午4:47:27
 *
 */
public interface UserService {

	/**
	 * 
	 * @Title: hasRegisterByPhoneNo
	 * @param:
	 * @Description: 通过手机号查询用户是否注册
	 * @return User
	 */
	public User hasRegisterByPhoneNo(String phoneNo);

	/**
	 * 
	 * @Title: createUser
	 * @param: UserRegVo
	 *             reg
	 * @param: int
	 *             signSource
	 * @Description: 注册创建用户
	 * @return User
	 */
	public Auth createUser(UserRegVo reg) throws UnsupportedEncodingException;

	/**
	 * 
	 * @Title: update
	 * @param:
	 * @Description: 修改
	 * @return int
	 */
	public int update(User user);
}
