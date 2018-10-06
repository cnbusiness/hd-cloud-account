package com.hd.cloud.dao.sql;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.hd.cloud.bo.User;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: AuthSqlProvider
 * @Description: 用户sql
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年12月7日 下午3:35:22
 *
 */
@Slf4j
public class AuthSqlProvider {

	/**
	 * 
	 * @Title: save
	 * @param:
	 * @Description: 保存
	 * @return String
	 */
	public String save(User user) {
		String sql = new SQL() {
			{
				INSERT_INTO("user_user_base_sb");
				VALUES("user_base_alais_name", "#{nickName}");
				VALUES("user_base_sex_itype", "#{sex}");
				VALUES("user_base_status_itype", "#{status}");
				VALUES("city_dict_intl_code", "#{countryCode}");
				VALUES("user_base_phone", "#{phone}");
				VALUES("user_base_password", "#{password}");

				VALUES("create_time", "now()");
				VALUES("create_by", "#{createBy}");
				VALUES("active_flag", "#{activeFlag}");
			}
		}.toString();
		log.info("############sql:{}",sql);
		return sql;
	}

	/**
	 * 
	 * @Title: updateUser
	 * @param:
	 * @Description: 更新用户基本信息
	 * @return String
	 */
	public String updateUser(final User abstractUser) {
		return new SQL() {
			{
				UPDATE("user_user_base_sb");
			 
				if (abstractUser.getNickName() != null) {
					SET("user_base_alais_name = #{nickName}");
				}
				
				if(abstractUser.getSex()>0) {
					SET("user_base_sex_itype = #{sex}");
				}

				if (abstractUser.getPassword() != null) {
					SET("user_base_password = #{password}");
				}
				
				if(StringUtils.isNotBlank(abstractUser.getBirthday())) {
					SET("user_base_birth_date = #{birthday}");
				}
				
				if(StringUtils.isNotBlank(abstractUser.getAvatar())) {
					SET("user_icon_photo_url = #{avatar}");
				}
				
				if(abstractUser.getVipType()>0) {
					SET("user_base_vip_itype = #{vipType}");
				}

				if (abstractUser.getUpdateBy() != 0) {
					SET("update_by = #{updateBy}");
				}
				WHERE("user_user_base_sb_seq = #{userId}");
			}
		}.toString();
	}
}
