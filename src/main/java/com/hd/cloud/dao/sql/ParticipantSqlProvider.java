package com.hd.cloud.dao.sql;
 

import java.util.Map;
import java.util.Set;

import org.apache.ibatis.jdbc.SQL;

import com.hd.cloud.domain.Participant;


public class ParticipantSqlProvider {
	
	/**
	 * 
	* @Title: saveParticipant 
	* @param: 
	* @Description: 群组添加成员
	* @return String
	 */
	public String saveParticipant(final Participant participant) {
		return new SQL() {
			{
				INSERT_INTO("user_group_member_sb");
				VALUES("user_group_base_br_seq", "#{roomId}");
				VALUES("user_user_base_sb_seq", "#{userId}");
				VALUES("group_member_join_date", "#{joinDate}");  
				VALUES("pub_city_dict_sd_seq", "#{pubCityDict}");
				VALUES("group_member_alais_name", "#{nickName}");//群名片，首次来源于用户昵称 
				
				VALUES("user_icon_photo_url", "#{userIcon}");  //用户头像URL 
				VALUES("group_member_alais_itype", "0");  //  昵称是否更新过,1是0否
				VALUES("user_base_sex_itype", "#{sexType}");  //  性别，1男2女 
				
				VALUES("group_member_contact_itype", "#{saveToContacts}");
				VALUES("group_member_role_itype", "#{roleType}");
				VALUES("user_detail_mtalk_domain", "#{mtalkDomain}");
				VALUES("create_by", "#{createBy}");
				VALUES("create_time", "now()");
				//VALUES("update_by", "#{createBy}");
				//VALUES("update_time", "now()");
				VALUES("active_flag", "'y'");
			}
		}.toString();
	}
	
	/**
	 * 移除群
	 */
	public String removeParticipant(Participant participant) {
		return new SQL() {
			{
				UPDATE("user_group_member_sb");
				SET("active_flag = 'n'"); 
				SET("update_by = #{createBy} ");
				SET("update_time =  now()");
				WHERE("user_user_base_sb_seq = #{userId}");
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("active_flag = 'y'");
			}
		}.toString();
	}

	/**
	 * 修改群昵称
	 */
	public String modifyNickNameInRoom() {
		return new SQL() {
			{
				UPDATE("user_group_member_sb");
				SET("update_by = #{loginUserId} "); 
				SET("update_time =  now()");
				SET("group_member_alais_itype = 1");
				SET("group_member_alais_name = #{nickNameInRoom}"); 
				WHERE("user_user_base_sb_seq = #{loginUserId}");
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("active_flag = 'y'");
			}
		}.toString();
	}

	/**
	 * 保存通讯录
	 */
	public String saveToContacts() {
		return new SQL() {
			{
				UPDATE("user_group_member_sb");
				SET("update_by = #{userId} ");
				SET("update_time =  now()");
				SET("group_member_contact_itype = #{saveToContacts}");    
				WHERE("user_user_base_sb_seq = #{userId}");
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("active_flag = 'y'");
			}
		}.toString();
	} 
	
	/**
	 * 删除群成员
	 */
	public String deleteAllParticipant() {
		return new SQL() {
			{
				UPDATE("user_group_member_sb");
				SET("active_flag = 'n'"); 
				SET("update_by = #{loginUserId} ");
				SET("update_time =  now()");
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("active_flag = 'y'");
			}
		}.toString();
	} 
	
	/**
	 * 更新群角色
	 */
	public String assignNewRole() {
		return new SQL() {
			{
				UPDATE("user_group_member_sb");
				SET("update_by = #{loginUserId} ");
				SET("update_time =  now()");
				SET("group_member_contact_itype = #{saveToContacts}");
				SET("group_member_role_itype = #{role}"); 
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("user_user_base_sb_seq = #{userId}");
				WHERE("active_flag = 'y'");
			}
		}.toString();
	} 
	
	/**
	 * 更改新群组
	 */
	public String changeOwner() {
		return new SQL() {
			{
				UPDATE("user_group_member_sb");
				SET("update_by = #{loginUserId} ");
				SET("update_time =  now()");
				SET("group_member_contact_itype = 1");
				SET("group_member_role_itype = 3"); 
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("user_user_base_sb_seq = #{userId}");
				WHERE("active_flag = 'y'");
			}
		}.toString();
	} 
	
	/**
	 * 取消原群组
	 */
	public String disableOwner() {
		return new SQL() {
			{
				UPDATE("user_group_member_sb");
				SET("update_by = #{loginUserId} ");
				SET("update_time =  now()");
				SET("active_flag = 'n'"); 
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("group_member_role_itype = 3");
				WHERE("active_flag = 'y'");
			}
		}.toString();
	} 
	
	/**
	 * 是否包含管理员
	 */
	public String includeAdmin(Map<String, Object> para){
		Set<Long> userIds =  (Set<Long>) para.get("userIds");
		long roomId =  (long)para.get("roomId");
		StringBuffer inUserId = new StringBuffer();
		//组装in内的查询语句 
		for(Long userId: userIds){
			inUserId.append(userId).append(",");
		}
		String inUserIds = inUserId.toString();
		final String in = inUserIds.substring(0, inUserIds.length() - 1);
		 
		return new SQL(){
			{
				SELECT("count(ugms.user_user_base_sb_seq) AS userCount ");
				FROM("user_group_member_sb ugms ");
				WHERE("ugms.user_group_base_br_seq =  "+roomId);
				WHERE("ugms.group_member_role_itype > 1 ");
				WHERE("ugms.group_member_role_itype <= 3 ");
				WHERE("ugms.user_user_base_sb_seq in ("+in+")");
				WHERE("ugms.active_flag =  'y'");
				
			} 
			
		}.toString();
	}
	 
	
	public String updateNickName(){
		return new SQL() {
			{
				UPDATE("user_group_member_sb");
				SET("update_by = #{userId}");
				SET("update_time =  now()");
				SET("group_member_alais_name = #{nickName}"); 
				WHERE("user_user_base_sb_seq = #{userId}");
				WHERE("group_member_alais_itype = 0");
				WHERE("active_flag = 'y'");
			}
		}.toString();
	}
	
	

	public String updateUserIcon(){
		return new SQL() {
			{
				UPDATE("user_group_member_sb");
				SET("update_by = #{userId}");
				SET("update_time =  now()");
				SET("user_icon_photo_url = #{userIcon}"); 
				WHERE("user_user_base_sb_seq = #{userId}"); 
				WHERE("active_flag = 'y'");
			}
		}.toString();
	}
	
	/**
	 * 用户是否存在
	 */
	public String checkUserWhetherExist(Map<String, Set<Long>> para) {
		Set<Long> userIds = para.get("userIds");
		StringBuffer inUserId = new StringBuffer();
		//组装in内的查询语句
		for(Long userId: userIds){
			inUserId.append(userId).append(",");
		}
		String inUserIds = inUserId.toString();
		final String in = inUserIds.substring(0, inUserIds.length() - 1);
		String sql = new SQL(){
			{
				SELECT("count(user_user_base_sb_seq) AS userCount ");
				FROM("user_user_base_sb u ");
				WHERE("u.user_user_base_sb_seq in ("+in+")");
				WHERE("u.active_flag =  'y'");
			}
		}.toString();
		return sql;
	}
	 
	
	public String getMtalkDomain(){
		return new SQL(){
			{
				SELECT("user_detail_mtalk_domain");
				FROM("user_user_detail_sr uuds ");
				WHERE("uuds.user_user_base_sb_seq = #{userId}");
				WHERE("uuds.active_flag =  'y'");
			}
		}.toString(); 
	} 
	
}
 