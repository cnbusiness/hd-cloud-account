package com.hd.cloud.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.hd.cloud.bo.FriendBo;

/***
 * 
  * @ClassName: FriendMapper
  * @Description: 好友
  * @author yaojie yao.jie@hadoop-tech.com
  * @Company hadoop-tech
  * @date 2018年4月12日 下午2:36:42
  *
 */
@Mapper
public interface FriendMapper {
  
  /**
   * 
  * @Title: findAllFriends 
  * @param: 
  * @Description: 批量查询用户
  * @return List<FriendBo>
   */
  @Select("<script> SELECT user_user_base_sb_seq,user_base_alais_name,user_base_sex_itype,user_base_photo_url "
		  +" FROM user_user_base_sb WHERE user_user_base_sb_seq IN  <foreach item='item' collection='ids' open ='(' separator=',' close=')'>#{item}</foreach> AND active_flag = 'y' ORDER BY createTime DESC</script>")
  @Results(value = {
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "name", column = "user_base_alais_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "sex", column = "user_base_sex_itype", javaType = int.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "photoUrl", column = "user_base_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR)
	})
  List<FriendBo> findAllFriends(@Param("ids") List<Long> ids);
  
  /**
   * 
  * @Title: getUserById 
  * @param: 
  * @Description: 查询用户
  * @return FriendBo
   */
  @Select("SELECT user_user_base_sb_seq,user_base_alais_name,user_base_sex_itype,user_icon_photo_url "
		  +" FROM user_user_base_sb WHERE user_user_base_sb_seq = #{userId} AND active_flag = 'y' ORDER BY create_time DESC limit 1")
  @Results(value = {
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "name", column = "user_base_alais_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "sex", column = "user_base_sex_itype", javaType = int.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "photoUrl", column = "user_icon_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR)
	})
  FriendBo getUserById(@Param("userId") long userId);
  
  /**
   * 
  * @Title: findAllConditions 
  * @param: 
  * @Description: 搜索用户  昵称/id/手机号
  * @return List<FriendBo>
   */
  @Select("SELECT user_user_base_sb_seq,user_base_alais_name,user_base_sex_itype,user_base_photo_url "
		  +" FROM user_user_base_sb WHERE (user_user_base_sb_seq = #{condition} OR user_base_alais_name = #{condition} OR user_base_phone = CONCAT(#{countryCode},#{condition}))")
  List<FriendBo> findAllConditions(@Param("countryCode") String countryCode,@Param("condition") String condition);

  /**
   * 
  * @Title: findAllFriends 
  * @param: 
  * @Description: 批量查询用户
  * @return List<FriendBo>
   */
  @Select("<script> SELECT user_user_base_sb_seq,user_base_alais_name,user_base_sex_itype,user_base_photo_url "
		  +" FROM user_user_base_sb WHERE user_base_phone IN  <foreach item='item' collection='phones' open ='(' separator=',' close=')'>#{item}</foreach> AND active_flag = 'y' ORDER BY createTime DESC</script>")
  @Results(value = {
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "name", column = "user_base_alais_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "sex", column = "user_base_sex_itype", javaType = int.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "photoUrl", column = "user_base_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR)
	})
  List<FriendBo> findUsersByPhoneBook(@Param("phones") List<String> phones);
 
}
