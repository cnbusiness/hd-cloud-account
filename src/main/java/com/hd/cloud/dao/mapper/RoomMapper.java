package com.hd.cloud.dao.mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.type.JdbcType;

import com.hd.cloud.dao.sql.ParticipantSqlProvider;
import com.hd.cloud.dao.sql.RoomSqlProvider;
import com.hd.cloud.domain.Participant;
import com.hd.cloud.domain.Room;
import com.hd.cloud.vo.InviteRecordVo;
import com.hd.cloud.vo.ParticipantVo;
import com.hd.cloud.vo.RoomInformationVo;
import com.hd.cloud.vo.UserVo;

@Mapper
public interface RoomMapper {

	/**
	 * 创建群
	 */
	@InsertProvider(type = RoomSqlProvider.class, method = "saveRoom")
	@SelectKey(keyProperty = "id", before = false, resultType = long.class, statement = { "SELECT LAST_INSERT_ID() AS id  " })
	int saveRoom(Room room);
	 
	/**
	 * 获取群列表
	 */
	@Select("SELECT ugbr.user_group_base_br_seq, ugbr.group_base_qrcode_url,  (case when ugbr.group_base_name  = '' then 0 else 1 end) as roomNameUpdated, ugms.group_member_contact_itype, ugbr.create_time, "
			+ " fun_get_groupname(ugbr.user_group_base_br_seq) as group_base_name, ugbr.group_base_verify_itype, ugbr.group_base_public_itype, ugbr.group_base_photo_url,"
			+ " ugbr.group_base_max_cnt, ugms.group_member_role_itype, ugms.group_member_alais_name as nickName, "
			+ " (select count(*) from user_group_member_sb ugms2 where ugms2.user_group_base_br_seq = ugbr.user_group_base_br_seq and ugms2.active_flag = 'y') as nowCnt,  "
			+ " (select ugms3.user_user_base_sb_seq from user_group_member_sb ugms3 where ugms3.user_group_base_br_seq = ugbr.user_group_base_br_seq and ugms3.active_flag = 'y' and ugms3.group_member_role_itype = 3 limit 0,1 ) as creatorId"
			+ " FROM user_group_member_sb ugms left join user_group_base_br ugbr on ugbr.user_group_base_br_seq = ugms.user_group_base_br_seq "
			+ " where ugms.user_user_base_sb_seq = #{loginUserId} and ugbr.active_flag = 'y' and ugms.active_flag = 'y' ")
	@Results(value = {
			@Result(property = "id", column = "user_group_base_br_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "roomName", column = "group_base_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "roomNameUpdated", column = "roomNameUpdated", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "verifyType", column = "group_base_verify_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "publicType", column = "group_base_public_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "photoUrl", column = "group_base_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "creatorId", column = "user_user_base_sb_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "qrcodeUrl", column = "group_base_qrcode_url", javaType = String.class, jdbcType = JdbcType.VARCHAR), 
			@Result(property = "roleType", column = "group_member_role_itype", javaType = Integer.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "saveToContacts", column = "group_member_contact_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER), 
			@Result(property = "nickName", column = "nickName", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "nowCnt", column = "nowCnt", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "createTimeInDate", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "maxCnt", column = "group_base_max_cnt", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			})
	List<RoomInformationVo> getRoomList(@Param("loginUserId") long loginUserId);
 
	/**
	 * 修改群图片、群名称
	 */
	@UpdateProvider(type = RoomSqlProvider.class, method = "updateRoom")
	int updateRoom(Room room);

	/**
	 * 获取群信息
	 */
	@Select("SELECT ugbr.user_group_base_br_seq, ugbr.group_base_qrcode_url, (case when ugbr.group_base_name  = '' then 0 else 1 end) as roomNameUpdated, (select group_member_role_itype from user_group_member_sb ugms3 where ugms3.user_group_base_br_seq = #{roomId}  and ugms3.active_flag = 'y' and ugms3.user_user_base_sb_seq = #{userId} limit 0,1) as roleType, ugbr.create_time,  fun_get_groupname(ugbr.user_group_base_br_seq) as group_base_name, ugbr.group_base_verify_itype, ugbr.group_base_public_itype, ugbr.group_base_photo_url,ugbr.group_base_max_cnt, "
			+ "(select count(*) from user_group_member_sb ugms2 where ugms2.user_group_base_br_seq = ugbr.user_group_base_br_seq and ugms2.active_flag = 'y') as nowCnt,  "
			+ "(select ugms5.group_member_alais_name from user_group_member_sb ugms5  where ugms5.user_group_base_br_seq = ugbr.user_group_base_br_seq and ugms5.active_flag = 'y' and ugms5.user_user_base_sb_seq = #{userId}  limit 0,1 ) as nickName, "
			+ "(select ugms4.group_member_contact_itype from user_group_member_sb ugms4  where ugms4.user_group_base_br_seq = ugbr.user_group_base_br_seq and ugms4.active_flag = 'y' and ugms4.user_user_base_sb_seq = #{userId}  limit 0,1 ) as group_member_contact_itype, "
			+ " (select ugms3.user_user_base_sb_seq from user_group_member_sb ugms3  where ugms3.user_group_base_br_seq = ugbr.user_group_base_br_seq and ugms3.active_flag = 'y' and ugms3.group_member_role_itype = 3 limit 0,1 ) as creatorId "
			+ "FROM user_group_base_br ugbr  where ugbr.user_group_base_br_seq = #{roomId} and ugbr.active_flag = 'y' ")
	@Results(value = {
			@Result(property = "id", column = "user_group_base_br_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "roomName", column = "group_base_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "roomNameUpdated", column = "roomNameUpdated", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "verifyType", column = "group_base_verify_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "publicType", column = "group_base_public_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "photoUrl", column = "group_base_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "creatorId", column = "user_user_base_sb_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "nowCnt", column = "nowCnt", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "saveToContacts", column = "group_member_contact_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),  
			@Result(property = "qrcodeUrl", column = "group_base_qrcode_url", javaType = String.class, jdbcType = JdbcType.VARCHAR), 
			@Result(property = "nickName", column = "nickName", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "roleType", column = "roleType", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "createTimeInDate", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "maxCnt", column = "group_base_max_cnt", javaType = Integer.class, jdbcType = JdbcType.BIGINT)
			})
	List<RoomInformationVo> getRoomWithParticipant(@Param("roomId") long roomId,  @Param("userId")long userId);
	  
	/**
	 * 获取群成员
	 */
	@Select("SELECT ugms.group_member_role_itype, ugms.user_detail_mtalk_domain, ugms.group_member_join_date, ugms.user_icon_photo_url, ugms.group_member_alais_name, ugms.user_user_base_sb_seq as userId   "
			+ " FROM user_group_member_sb ugms where ugms.active_flag = 'y' and ugms.user_group_base_br_seq = #{roomId} order by ugms.group_member_role_itype desc, ugms.user_group_member_sb_seq asc limit #{pageIndex}, #{pageSize}")		 
	@Results(value = {
			@Result(property = "userId", column = "userId", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "role", column = "group_member_role_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "joinDateInDate", column = "group_member_join_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "nickName", column = "group_member_alais_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "mtalkDomain", column = "user_detail_mtalk_domain", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "photoUrl", column = "user_icon_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			})
    List<ParticipantVo>selectParticipant(@Param("roomId") long roomId, @Param("pageIndex")int pageIndex, @Param("pageSize")int pageSize);

	/**
	 * 删除群
	 */
	@UpdateProvider(type = RoomSqlProvider.class, method = "deleteRoom")
	int deleteRoom(@Param("loginUserId") long loginUserId, @Param("roomId") long roomId);

	/**
	  * 获取群信息
	  */
	@Select("SELECT ugbr.user_group_base_br_seq, ugbr.group_base_qrcode_url, (case when ugbr.group_base_name  = '' then 0 else 1 end) as roomNameUpdated, ugbr.create_time, fun_get_groupname(ugbr.user_group_base_br_seq) as group_base_name, ugbr.group_base_verify_itype, ugbr.group_base_photo_url, ugbr.group_base_public_itype,ugbr.group_base_max_cnt, "
			+ "(select count(*) from user_group_member_sb ugms2 where ugms2.user_group_base_br_seq = ugbr.user_group_base_br_seq and ugms2.active_flag = 'y') as nowCnt,  "
			+ " (select ugms3.user_user_base_sb_seq from user_group_member_sb ugms3  where ugms3.user_group_base_br_seq = ugbr.user_group_base_br_seq and ugms3.active_flag = 'y' and ugms3.group_member_role_itype = 3 limit 0,1 ) as creatorId "
			+ "FROM user_group_base_br ugbr  where ugbr.user_group_base_br_seq = #{roomId} and ugbr.active_flag = 'y' ")
	@Results(value = {
			@Result(property = "id", column = "user_group_base_br_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "roomName", column = "group_base_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "roomNameUpdated", column = "roomNameUpdated", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "photoUrl", column = "group_base_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "verifyType", column = "group_base_verify_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "publicType", column = "group_base_public_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "creatorId", column = "user_user_base_sb_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "nowCnt", column = "nowCnt", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "qrcodeUrl", column = "group_base_qrcode_url", javaType = String.class, jdbcType = JdbcType.VARCHAR), 
			@Result(property = "createTimeInDate", column = "create_time", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "maxCnt", column = "group_base_max_cnt", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			})
	RoomInformationVo getRoomById(@Param("roomId")long roomId);

	/**
	 * 用户在群里的角色
	 */
	@SelectProvider(type = RoomSqlProvider.class, method = "getGroupRole")
	Integer getGroupRole(@Param("roomId")long roomId, @Param("userId")long userId);
	
	/**
	 * 群现有管理员人数
	 */
	@SelectProvider(type = RoomSqlProvider.class, method = "getAdminList")
	List<Long> getAdminList(@Param("roomId")long roomId);

	/**
	 * 验证是否存在已在群的成员
	 */
	@SelectProvider(type = RoomSqlProvider.class, method = "userAllExistingInRoom")
	int userAllExistingInRoom(@Param("roomId")long roomId, @Param("userIds")Set<Long> userIds);

	/**
	 * 重置群二维码
	 */
	@UpdateProvider(type = RoomSqlProvider.class, method = "updateRoomQRCodeLocation")
	void updateRoomQRCodeLocation(@Param("qrCodeFileLocation")String qrCodeFileLocation, @Param("roomId")long roomId, @Param("key")String key, @Param("loginUserId") long loginUserId);

	/**
	 * 保存邀请链接
	 */
	@InsertProvider(type = RoomSqlProvider.class, method = "saveInviteRecord")
	int saveInviteRecord(@Param("inviteRecord") InviteRecordVo inviteRecordVo);

	
	/**
	 * 校验群的KEY是否有效
	 */
	@SelectProvider(type = RoomSqlProvider.class, method = "isValidKey")
	int isValidKey(@Param("roomId")long roomId, @Param("key")String key);

	/**
	 * 邀请群链接KEY是否有效
	 */
	@SelectProvider(type = RoomSqlProvider.class, method = "isValidInviteKey")
	int isValidInviteKey(@Param("roomId")long roomId, @Param("toId")long toId,  @Param("key")String key,  @Param("currentDate")Date date);


	@Select("SELECT ugms.group_member_alais_name as nick_name "
			+ " FROM user_group_member_sb ugms where ugms.user_user_base_sb_seq = #{userId} and ugms.active_flag = 'y' and ugms.user_group_base_br_seq = #{roomId}")
	String getNickName(@Param("roomId")long roomId, @Param("userId")long userId);

	/**
	 * 获取已有的群组
	 */
	@Select("SELECT count(*) FROM user_group_base_br ugbb left join user_group_member_sb ugms on ugbb.user_group_base_br_seq = ugms.user_group_base_br_seq "
			+ "where ugms.user_user_base_sb_seq = #{userId} and ugms.group_member_role_itype = 3 and ugbb.active_flag = 'y' and  ugms.active_flag = 'y'  ")  
	int getCreatedRoomCnt(@Param("userId")long userId); 
	
	@Select("SELECT * FROM user_group_member_sb where user_group_base_br_seq = #{roomId}")
	@Results(value = {
			@Result(property = "id", column = "user_group_member_sb_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "userId", column = "user_user_base_sb_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "roomId", column = "user_group_base_br_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "joinDate", column = "group_member_join_date", javaType = Date.class, jdbcType = JdbcType.TIMESTAMP),
			@Result(property = "mtalkDomain", column = "user_detail_mtalk_domain", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "roleType", column = "group_member_role_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			})
	List<Participant> getRoomParticipantList(@Param("roomId") long roomId); 
	
	/**
	 * 群组添加成员
	 */
	@InsertProvider(type = ParticipantSqlProvider.class, method = "saveParticipant")
	@SelectKey(keyProperty = "id", before = false, resultType = long.class, statement = { "SELECT LAST_INSERT_ID() AS id  " })
	int saveParticipant(Participant participant);

	/**
	 * 移除群
	 */
	@UpdateProvider(type = ParticipantSqlProvider.class, method = "removeParticipant")
	int removeParticipant(Participant participant);
	
	/**
	 * 修改群昵称
	 */
	@UpdateProvider(type = ParticipantSqlProvider.class, method = "modifyNickNameInRoom")
	int modifyNickNameInRoom(@Param("loginUserId") long loginUserId, @Param("roomId") long roomId, @Param("nickNameInRoom") String nickNameInRoom);

	/**
	 * 删除群成员
	 */
	@UpdateProvider(type = ParticipantSqlProvider.class, method = "deleteAllParticipant")
	int deleteAllParticipant(@Param("loginUserId") long loginUserId,  @Param("roomId") long roomId);

	/**
	 * 更新群角色
	 */
	@UpdateProvider(type = ParticipantSqlProvider.class, method = "assignNewRole")
	int assignNewRole(@Param("loginUserId")long loginUserId,  @Param("roomId")long roomId, @Param("userId")long userId, @Param("role")int role,
			@Param("saveToContacts") int saveToContacts);

	/**
	 * 更改新群组
	 */
	@UpdateProvider(type = ParticipantSqlProvider.class, method = "changeOwner")
	int changeOwner(@Param("loginUserId")long loginUserId,  @Param("roomId")long roomId, @Param("userId")long userId);

	/**
	 * 取消原群组
	 */
	@UpdateProvider(type = ParticipantSqlProvider.class, method = "disableOwner")
	int disableOwner(@Param("loginUserId")long loginUserId,  @Param("roomId")long roomId);

	/**
	 * 保存到通讯录
	 */
	@UpdateProvider(type = ParticipantSqlProvider.class, method = "saveToContacts")
	int saveToContacts(@Param("userId")long userId, @Param("roomId")long roomId, @Param("saveToContacts")int saveToContacts);

	/**
	 * 是否包含群管理员
	 */
	@SelectProvider(type = ParticipantSqlProvider.class, method = "includeAdmin")
	int includeAdmin(@Param("roomId")long roomId, @Param("userIds")Set<Long> userIds);

	@UpdateProvider(type = ParticipantSqlProvider.class, method = "updateNickName")
	int updateNickName(@Param("userId")long userId, @Param("nickName")String nickName);

	@UpdateProvider(type = ParticipantSqlProvider.class, method = "updateUserIcon")
	int updateUserIcon(@Param("userId")long userId, @Param("userIcon")String userIcon);
	
	/**
	 * 用户是否存在
	 */
	@SelectProvider(type =ParticipantSqlProvider.class, method = "checkUserWhetherExist")
	int checkUserWhetherExist(@Param("userIds")Set<Long> userIds);

	
	@SelectProvider(type =ParticipantSqlProvider.class, method = "getMtalkDomain")
	String getMtalkDomain(@Param("userId")long userId);


	@Select("SELECT uuds.user_detail_mtalk_domain, uubs.user_base_alais_name FROM user_user_detail_sr uuds left join user_user_base_sb uubs on uubs.user_user_base_sb_seq = uuds.user_user_base_sb_seq"
			+ " where uuds.user_user_base_sb_seq = #{userId} and uuds.active_flag = 'y' and uubs.active_flag = 'y' ")
	@Results(value = { 
			@Result(property = "nickName", column = "user_base_alais_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "mtalkDomain", column = "user_detail_mtalk_domain", javaType = String.class, jdbcType = JdbcType.VARCHAR), 
			})
	UserVo getUser(@Param("userId")long userId);
 
	/**
	 * 获取用户信息
	 */
	@Select("SELECT uuds.user_detail_mtalk_domain, uubs.user_base_alais_name, uubs.user_icon_photo_url, uubs.user_base_sex_itype FROM user_user_detail_sr uuds "
			+ "left join user_user_base_sb uubs on uubs.user_user_base_sb_seq = uuds.user_user_base_sb_seq where uuds.user_user_base_sb_seq = #{userId} and uuds.active_flag = 'y' and uubs.active_flag = 'y' ")
	@Results(value = { 
			@Result(property = "nickName", column = "user_base_alais_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "mtalkDomain", column = "user_detail_mtalk_domain", javaType = String.class, jdbcType = JdbcType.VARCHAR), 
			@Result(property = "sexType", column = "user_base_sex_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER), 
			@Result(property = "userIcon", column = "user_icon_photo_url", javaType = String.class, jdbcType = JdbcType.VARCHAR), 
			})
	UserVo getUserV2(@Param("userId")long userId);
}
