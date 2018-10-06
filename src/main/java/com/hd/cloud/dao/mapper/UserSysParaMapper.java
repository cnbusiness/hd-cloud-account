package com.hd.cloud.dao.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.hd.cloud.bo.UserSysPara;

@Mapper
public interface UserSysParaMapper {
	
	
	/**
	 * 
	 * @Title: getUserSysParaByInternalCode
	 * @Description: 查询 参数
	 * @Table 2.9.1user_sys_para_sb  用户参数表 
	 */
	@Select("SELECT user_sys_para_sb_seq,sys_para_internal_code,sys_para_name,sys_para_desc,sys_para_value FROM user_sys_para_sb WHERE active_flag='y' AND sys_para_internal_code=#{internalCode}")
	@Results(value = {
			@Result(property = "id", column = "user_sys_para_sb_seq", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "internalCode", column = "sys_para_internal_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "paraName", column = "sys_para_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "paraDesc", column = "sys_para_desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "value", column = "sys_para_value", javaType = String.class, jdbcType = JdbcType.VARCHAR) })
	public UserSysPara getUserSysParaByInternalCode(@Param("internalCode") String internalCode);
}
