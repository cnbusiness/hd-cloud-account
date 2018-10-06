package com.hd.cloud.bo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: Auth
 * @Description: 认证
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:35:40
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth {
	/**
	 * id主键
	 */
	private long id;

	/**
	 * 用户id
	 */
	private long userId;

	/**
	 * token令牌
	 */
	private String token;

	/**
	 * token的生成时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date authTime;
}
