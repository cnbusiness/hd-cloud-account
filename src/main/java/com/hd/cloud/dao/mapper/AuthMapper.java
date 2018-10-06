package com.hd.cloud.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.hd.cloud.bo.FriendBo;
import com.hd.cloud.bo.User;
import com.hd.cloud.bo.UserProfile;
import com.hd.cloud.dao.sql.AuthSqlProvider;

@Mapper
public interface AuthMapper {

	/**
	 * 
	 * @Title: login
	 * @Description: 用户登录userId与密码验证
	 * @Table user_user_base_sb_seq 用户基础信息表
	 */
	@Select("select user_user_base_sb_seq from user_user_base_sb where user_user_base_sb_seq=#{userId} and user_base_password=#{userPassword} AND active_flag='y' ")
	@Results(value = {
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = long.class, jdbcType = JdbcType.BIGINT) })
	Long login(@Param("userId") long userId, @Param("userPassword") String userPassword);

	/**
	 * 
	 * @Title: getIdByPhoneNumber
	 * @Description: 根据手机号码查询用户ID
	 * @Table user_user_base_sb_seq 用户基础信息表
	 */
	@Select("select user_user_base_sb_seq,user_base_status_itype from user_user_base_sb where user_base_phone=#{phoneNumber} AND active_flag='y'")
	@Results(value = {
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "status", column = "user_base_status_itype", javaType = Integer.class, jdbcType = JdbcType.TINYINT) })
	User getIdByPhoneNumber(@Param("phoneNumber") String phoneNumber);

	/**
	 * 
	 * @Title: getIdByEmail
	 * @Description: 根据邮箱查询用户ID
	 * @Table user_user_base_sb_seq 用户基础信息表
	 */
	@Select("select user_user_base_sb_seq,user_base_status_itype  from user_user_base_sb where user_base_email=#{email} AND active_flag='y'")
	@Results(value = {
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "status", column = "user_base_status_itype", javaType = Integer.class, jdbcType = JdbcType.TINYINT) })
	User getIdByEmail(@Param("email") String email);

	/**
	 * 
	 * @Title: getPhoneByUserId
	 * @Description: 通过userId查询手机号
	 * @Table String
	 */
	@Select("select user_base_phone from user_user_base_sb where user_user_base_sb_seq=#{userId} AND active_flag='y' ")
	String getPhoneByUserId(@Param("userId") long userId);

	/**
	 * 
	 * @Title: save
	 * @Description: 保存
	 * @Table int
	 */
	@InsertProvider(type = AuthSqlProvider.class, method = "save")
	@SelectKey(statement = {
			"SELECT LAST_INSERT_ID() AS userId  " }, keyProperty = "userId", before = false, resultType = long.class)
	int save(User user);

	/**
	 * @Title: updateUser
	 * @Description: 修改用户信息
	 * @Table user_user_base_sb 用户基本表
	 */
	@UpdateProvider(type = AuthSqlProvider.class, method = "updateUser")
	int updateUser(User user);

	/**
	 * 
	 * @Title: getUserProfileByUserId
	 * @Description: 查询用户详情
	 * @Table user_user_base_sb_seq 用户基础信息表
	 */
	@Select("SELECT user_user_base_sb_seq,user_base_alais_name,user_base_sex_itype,user_base_status_itype,city_dict_intl_code,user_base_phone,user_base_vip_itype,user_icon_photo_url,user_base_birth_date from user_user_base_sb where user_user_base_sb_seq=#{userId} AND active_flag='y' ")
	@Results(value = {
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "nickName", column = "user_base_alais_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "sex", column = "user_base_sex_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "status", column = "user_base_status_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "avatar", column = "user_icon_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "vipType", column = "user_base_vip_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "countryCode", column = "city_dict_intl_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "birthday", column = "user_base_birth_date", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "phone", column = "user_base_phone", javaType = String.class, jdbcType = JdbcType.VARCHAR) })
	UserProfile getUserProfileByUserId(@Param("userId") long userId);

	/**
	 * 
	 * @Title: getUserProfileListByUserIds
	 * @Description: 批量查询用户信息
	 * @Table user_user_base_sb_seq 用户基础信息表
	 */
	@Select("SELECT user_user_base_sb_seq,user_base_alais_name,user_base_sex_itype,user_base_status_itype,city_dict_intl_code,user_base_phone,user_base_vip_itype,user_icon_photo_url from user_user_base_sb where user_user_base_sb_seq in (#userIds) AND active_flag='y' ")
	@Results(value = {
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "nickName", column = "user_base_alais_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "sex", column = "user_base_sex_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "status", column = "user_base_status_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "avatar", column = "user_icon_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "vipType", column = "user_base_vip_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "countryCode", column = "city_dict_intl_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "phone", column = "user_base_phone", javaType = String.class, jdbcType = JdbcType.VARCHAR) })
	List<UserProfile> getUserProfileListByUserIds(@Param("userIds") String userIds);
	
	/**
	 * 
	* @Title: searchUser 
	* @param: 
	* @Description: 搜索用户
	* @return List<FriendBo>
	 */
	@Select("SELECT user_user_base_sb_seq,user_base_alais_name,user_icon_photo_url,user_base_phone,city_dict_intl_code FROM user_user_base_sb "
		  + " where user_base_phone LIKE CONCAT('%', #{word},'%') OR user_user_base_sb_seq = #{word}")
	@Results(value = {
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "nickName", column = "user_base_alais_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "avatar", column = "user_icon_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "countryCode", column = "city_dict_intl_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "phone", column = "user_base_phone", javaType = String.class, jdbcType = JdbcType.VARCHAR) 
	})
	List<UserProfile> searchUser(@Param("word") String word);
	
	/**
	 * 
	* @Title: searchUserLists 
	* @param: 
	* @Description: 批量搜索用户
	* @return List<UserProfile>
	 */
	@Select("<script> SELECT user_user_base_sb_seq,user_base_alais_name,user_icon_photo_url,user_base_phone,city_dict_intl_code FROM user_user_base_sb "
			  + "<if test='list!=null'> where user_user_base_sb_seq IN <foreach item='item' collection='list' open ='(' separator=',' close=')'>#{item}</foreach></if></script>")
	@Results(value = {
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "nickName", column = "user_base_alais_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "avatar", column = "user_icon_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "countryCode", column = "city_dict_intl_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "phone", column = "user_base_phone", javaType = String.class, jdbcType = JdbcType.VARCHAR) 
	})
	List<UserProfile> searchUserLists(@Param("list") List<String> list);
}
