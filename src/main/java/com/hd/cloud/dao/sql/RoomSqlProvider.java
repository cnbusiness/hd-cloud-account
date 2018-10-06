package com.hd.cloud.dao.sql;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.jdbc.SQL;

import com.hd.cloud.domain.Room;

public class RoomSqlProvider {
	
	/**
	 * 
	 * @Title: saveRoom
	 * @param:
	 * @Description: 创建群
	 * @return String
	 */
	public String saveRoom(Room room) {
		return new SQL() {
			{
				INSERT_INTO("user_group_base_br");
				VALUES("user_group_type_bd_seq", "1");
				VALUES("group_base_name", "#{roomName}");
				VALUES("group_base_desc", "#{roomName}");
				VALUES("group_base_key", "'group key word'");
				VALUES("group_base_verify_itype", "#{verifyType}");
				VALUES("group_base_public_itype", "#{publicType}");
				VALUES("group_base_max_cnt", "#{maxCnt}"); 
				VALUES("group_base_photo_url", "#{photoUrl}");
				VALUES("user_user_base_sb_seq", "#{createBy}"); 
				VALUES("create_by", "#{createBy}");
				VALUES("create_time", "#{createTimeInDate}");
				VALUES("update_by", "#{createBy}");
				VALUES("update_time", "#{createTimeInDate}");
				VALUES("active_flag", "'y'");
			}
		}.toString();
	}
	
	/**
	 * 修改群名称、头像
	 */
	public String updateRoom(Room room){
		return new SQL() {
			{ 
				UPDATE("user_group_base_br");
				
				if (room.getRoomName() != null) {
					SET("group_base_name = #{roomName}");
				}
				if (room.getPhotoUrl() != null) {
					SET("group_base_photo_url = #{photoUrl}");
				} 
				SET("update_by = #{createBy} ");
				SET("update_time =  now()");
				WHERE("user_group_base_br_seq = #{id}");
				WHERE("active_flag = 'y'");
			}
		}.toString();
	}
	
	public String getRoomInformation(long roomId) {
		return new SQL() {
			{
				UPDATE("user_group_base_br");
				SET("group_base_now_cnt = group_base_now_cnt + 1");
				WHERE("user_group_base_br_seq = "+ roomId);
				WHERE("active_flag = 'y'");
			}
		}.toString();
	}
	
	/**
	 * 删除群
	 */
	public String deleteRoom() {
		return new SQL() {
			{
				UPDATE("user_group_base_br");
				SET("update_by = #{loginUserId} ");
				SET("update_time =  now()");
				SET("active_flag = 'n'");
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("active_flag = 'y'");
			}
		}.toString();
	}
	
	
	/**
	 * 用户在群里的角色
	 */
	public String getGroupRole(){
		return new SQL() {
			{
				SELECT("ugms.group_member_role_itype");
				FROM("user_group_member_sb ugms");
				WHERE("ugms.user_group_base_br_seq = #{roomId}");
				WHERE("ugms.user_user_base_sb_seq = #{userId}");
				WHERE("ugms.active_flag = 'y'");
			}
		}.toString();
	}
	
	/**
	 * 群现有管理员数量
	 */
	public String getAdminList(){
		return new SQL() {
			{
				SELECT("ugms.user_user_base_sb_seq");
				FROM("user_group_member_sb ugms");
				WHERE("ugms.user_group_base_br_seq = #{roomId}");
				WHERE("ugms.active_flag = 'y'");
				WHERE("ugms.group_member_role_itype = 2");
			}
		}.toString();
	}
	
	/**
	 * 验证是否存在已在群的成员
	 */
	public String userAllExistingInRoom(Map<String, Object> para){
		 
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
				WHERE("ugms.user_user_base_sb_seq in ("+in+")");
				WHERE("ugms.active_flag =  'y'");
				
			} 
			
		}.toString();
	}
	 
	/**
	 * 重置群二维码
	 */
	public String updateRoomQRCodeLocation(){
		return new SQL(){
			{ 
				UPDATE("user_group_base_br");
				SET("group_base_qrcode_url = #{qrCodeFileLocation}");
				SET("group_base_qrcode_value = #{key}");
				SET("update_by = #{loginUserId} ");
				SET("update_time =  now()");
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("active_flag = 'y'");
			} 
			
		}.toString();
	}
	
	/**
	 * 校验群的KEY是否有效
	 */
	public String isValidKey(){
		return new SQL(){
			{ 
				SELECT("count(*) ");
				FROM("user_group_base_br");
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("active_flag = 'y'");
				WHERE("group_base_qrcode_value = #{key}");   
			} 
			
		}.toString();
	}
	 
	/**
	 * 保存邀请链接
	 */
	public String saveInviteRecord(){
		return new SQL() {
			{
				INSERT_INTO("user_group_invite_bt");
				VALUES("user_user_base_sb_seq", "#{inviteRecord.fromId}");
				VALUES("user_group_base_br_seq", "#{inviteRecord.roomId}");
				VALUES("user_user_seq", "#{inviteRecord.toId}");
				VALUES("group_invite_key", "#{inviteRecord.key}"); 
				VALUES("group_invite_edate", "#{inviteRecord.inviteEndDate}");  
				VALUES("create_by", "#{inviteRecord.fromId}");
				VALUES("active_flag", "'y'"); 
			}
		}.toString(); 
	}
	
	/**
	 * 群邀请KEY是否有效
	 */
	public String isValidInviteKey(){
		return new SQL() {
			{ 
				SELECT("count(*) ");
				FROM("user_group_invite_bt");
				WHERE("user_group_base_br_seq = #{roomId}");
				WHERE("user_user_seq = #{toId}");
				WHERE("group_invite_key = #{key}"); 
				WHERE("active_flag ='y'");
				WHERE("group_invite_edate >= #{currentDate}");
			}
		}.toString(); 
	}
}
