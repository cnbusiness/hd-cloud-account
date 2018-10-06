package com.hd.cloud.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hd.cloud.dao.RoomDao;
import com.hd.cloud.domain.Participant;
import com.hd.cloud.domain.Room;
import com.hd.cloud.service.GroupChatService;
import com.hd.cloud.util.RoleEnum;
import com.hd.cloud.vo.InviteRecordVo;
import com.hd.cloud.vo.ModifyRoomVo;
import com.hd.cloud.vo.ParticipantVo;
import com.hd.cloud.vo.RoomAdminDetailVo;
import com.hd.cloud.vo.RoomInformationVo;
import com.hd.cloud.vo.RoomVo;
import com.hd.cloud.vo.UserVo;

@Slf4j
@Service
public class GroupChatServiceImpl implements GroupChatService {
	//8.	xxx邀请ccc加入群组
	//9.	你邀请ccc加入群组
	//10.	你邀请ccc，ddd,ggg…加入群组
	/**
	 * 
	 * template 1 
	 * {0} 邀请 {1} 加入群组
	 * 
	 * template 2 
	 * {0}将群组名称更改为 {1}
	 * 
	 * template 3
	 * {0}退出群组，{1}成为了群主；
	 * 
	 * template 4
	 * {0}修改了群组头像；
	 * 
	 * template 5
	 * {0}设置{1}为管理员
	 * 
	 * template 6
	 * {0}加入了群组
	 * 
	 * template 7
	 * {0}退出了群组
	 * 
	 * template 8
	 * {0}将{1}移除了群组
	 * 
	 * template 9
	 * {0}已经将本群解散
	 * 
	 * template 10
	 * {0}取消了{1}管理员资格；
	 * 
	 */
	
	private String topic = "motalkmessageservice";
	
	@Inject
	private RoomDao roomDao;
 
	/**
	 * 创建群组
	 */
	@Override
	@Transactional   
	public Room createNewRoom(RoomVo newRoomVo,long loginUserId){
		int maxCnt = 500;
		//群验证，1允许任何人 2需要身份验证 3不允许任何人
		int verifyType = newRoomVo.getVerifyType();
		if(verifyType == 0){
			verifyType = 1;
		} 
		int publicType = newRoomVo.getPublicType();
		String roomName = newRoomVo.getRoomName();
		String photoUrl = newRoomVo.getPhotoUrl();
		//创建群
		Room room = Room.builder().roomName(roomName).verifyType(verifyType).createTimeInDate(new Date())
				.publicType(publicType).photoUrl(photoUrl).maxCnt(maxCnt).createBy(loginUserId).build();
		room = roomDao.createRoom(room);  
		
		//获取创建者信息
		UserVo userVo = roomDao.getUserV2(loginUserId);
		
		//群组添加自己为管理员
		saveParticipantToRoom(room.getId(), RoleEnum.OWNER.getIntValue(), loginUserId, 1, userVo);
		
		List<ParticipantVo> participantList = newRoomVo.getParticipantList();
		for(ParticipantVo participantVo: participantList){
			long userId = participantVo.getUserId();
			UserVo participantUserVo = roomDao.getUserV2(userId); 
			//群组添加成员
			saveParticipantToRoom(room.getId(), RoleEnum.MEMBER.getIntValue() , userId, 0, participantUserVo);
		} 
		return room;
	}

	/**
	 * 获取群列表
	 */
	@Override
	public List<RoomInformationVo> getRoomList(long loginUserId) {
		return roomDao.getRoomList(loginUserId);
	}

	/**
	 * 获取单个群相关属性以及群成员
	 */
	@Override
	public List<RoomInformationVo> getRoomWithParticipant(long roomId,long userId, int pageIndex, int pageSize) {
		return roomDao.getRoomWithParticipant(roomId, userId, pageIndex, pageSize);
	}

	/**
	 * 
	* @Title: saveParticipantToRoom 
	* @param: 
	* @Description: 群组添加成员 
	* @return Participant
	 */
	private Participant saveParticipantToRoom(long roomId, int role, long userId, int saveToContacts, UserVo userVo) { 
		Participant participant = Participant.builder().roomId(roomId).userId(userId).roleType(role).saveToContacts(saveToContacts)
				.nickName(userVo.getNickName()).sexType(userVo.getSexType()).userIcon(userVo.getUserIcon())
				.mtalkDomain(userVo.getMtalkDomain()).joinDate(new Timestamp(new Date().getTime())).createBy(userId).pubCityDict(0).build();
		roomDao.addParticipant(participant);
		return participant;
	} 
	
	 /**
	  * 获取群信息
	  */
	 public RoomInformationVo getRoomById(long roomId){ 
		 return roomDao.getRoomById(roomId);
	 }

	 /**
	  * 修改群相关的属性, 1修改群名称, 2群图片, 3 群主ID
	  */
	@Override
	@Transactional
	public int updateRoom(ModifyRoomVo modifyRoomVo, long roomId, RoomInformationVo roomInformationVo,long loginUserId) { 
		String ownerId = modifyRoomVo.getOwnerId();
		if(ownerId != null && !"".equals(ownerId)){   //修改群主
			//取消原群组
		    roomDao.disableOwner(loginUserId, roomId);
		    //更改新群组
		    int result = roomDao.changeOwner(loginUserId, roomId, Long.parseLong(modifyRoomVo.getOwnerId()));
		    return result;
		}else{
			String roomName = modifyRoomVo.getRoomName();
			String photoUrl = modifyRoomVo.getPhotoUrl(); 
			Room room = Room.builder().id(roomId).roomName(roomName).photoUrl(photoUrl).createBy(loginUserId).build();
			//修改群图片、群名称
		    int result = roomDao.updateRoom(room); 
		    return result;
		} 
	}

	@Override
	@Transactional
	public int removeParticipant(long roomId, int role, String jid) {
		
		long userId = 0;
		String domain = null;
		int index = jid.indexOf("@");
		if(index > 0){
			userId = Long.parseLong(jid.substring(0, index));
			domain = jid.substring(index+1);
		}
		
		Participant participant = Participant.builder().roomId(roomId).userId(userId).roleType(role)
				.mtalkDomain(domain).build();
				
		return roomDao.removeParticipant(participant);
	}

	 /**
	  * 加入群
	  */
	@Override
	@Transactional
	public int addParticipantList(long roomId, List<ParticipantVo> list, boolean fromInviteLink, RoomInformationVo roomInformationVo) {
		for(ParticipantVo participantVo: list){
			long userId = participantVo.getUserId();
			UserVo userVo = roomDao.getUserV2(userId);
			saveParticipantToRoom(roomId,participantVo.getRole(), userId, 0, userVo);
		} 
		return 1;
	}

	/**
	 * 移除群
	 */
	@Override
	@Transactional
	public int removeParticipant(long roomId, List<ParticipantVo> list, boolean isQuitFromGroup, RoomInformationVo roomInformationVo,long loginUserId) { 
		for(ParticipantVo participantVo: list){
			 long userId = participantVo.getUserId();
			 UserVo userVo = roomDao.getUserV2(userId);
			Participant participant = Participant.builder().roomId(roomId)
					.userId(userId).roleType(participantVo.getRole())
					.mtalkDomain(userVo.getMtalkDomain()).createBy(loginUserId).build();
			 roomDao.removeParticipant(participant);
		}
		return 1;
	}

	/**
	 * 修改自己在群里的昵称
	 */
	@Override
	@Transactional
	public int modifyNickNameInRoom(long roomId, String nickNameInRoom, RoomInformationVo roomInformationVo,long loginUserId) {
		//修改昵称
		int result = roomDao.modifyNickNameInRoom(loginUserId, roomId, nickNameInRoom);
		return result;
	}

	/**
	 * 解散群
	 */
	@Override
	@Transactional
	public int deleteRoom(long roomId, RoomInformationVo roomInformationVo,long loginUserId) { 
		//删除所有的群成员
		roomDao.deleteAllParticipant(loginUserId, roomId);
		//删除群
		int result = roomDao.deleteRoom(loginUserId, roomId);   
		return result;
	}

	/**
	 * 新增管理员
	 */
	@Override
	@Transactional
	public int setAdminList(long roomId, List<RoomAdminDetailVo> addList, RoomInformationVo roomInformationVo,long loginUserId) {
		for(RoomAdminDetailVo roomAdminDetailVo: addList){
			String userIdStr = roomAdminDetailVo.getUserId();
			long userId = Long.parseLong(userIdStr);
			//更改为管理员
			roomDao.assignNewRole(loginUserId, roomId, userId, RoleEnum.ADMIN.getIntValue(), 1);
		}
		return 1;
		
	}

	/**
	 * 取消管理员
	 */
	@Override
	@Transactional
	public int cancelAdminList(long roomId, List<RoomAdminDetailVo> removeList, RoomInformationVo roomInformationVo,long loginUserId) {
		for(RoomAdminDetailVo roomAdminDetailVo: removeList){
			String userIdStr = roomAdminDetailVo.getUserId();
			long userId = Long.parseLong(userIdStr);
			//更改为群员
			roomDao.assignNewRole(loginUserId, roomId, userId, RoleEnum.MEMBER.getIntValue(), 0);
		}
		return 1;
	}

	/**
	 * 验证用户是否存在
	 */
	@Override
	public boolean checkUserWhetherExist(Set<Long> userIds) {
		 return roomDao.checkUserWhetherExist(userIds);
	}

	/**
	 * 验证是否存在已在群的成员
	 */
	@Override
	public boolean existingMemberInside(long roomId, Set<Long> userIds) {
		return roomDao.existingMemberInside(roomId, userIds);
	}

	/**
	 * 验证是否是群组或管理员
	 */
	@Override
	public boolean isOwnerOrAdminInRoom(long roomId, long userId) {
		return roomDao.isOwnerOrAdminInRoom(roomId, userId);
	}
	
	/**
	 * 获取在群里的角色
	 */
	@Override
	public Integer getGroupRole(long roomId, long userId) {
		return roomDao.getGroupRole(roomId, userId);
	}


	@Override
	public boolean userAllExistingInRoom(long roomId, Set<Long> userIds) {
		return roomDao.userAllExistingInRoom(roomId, userIds);
	}

	/**
	 * 群现有管理员人数
	 */
	@Override
	public List<Long> getAdminList(long roomId) {
		return roomDao.getAdminList(roomId);
	}

	/**
	 * 是否是群组
	 */
	@Override
	public boolean isOwnerInRoom(long roomId, long userId) {
		return roomDao.isOwnerInRoom(roomId, userId);
	}

	/**
	 * 校验群的KEY是否有效
	 */
	@Override
	public boolean isValidKey(long roomId, String key) {
		return roomDao.isValidKey(roomId, key);
	}

	/**
	 * 重置群二维码
	 */
	@Override
	public void updateRoomQRCodeLocationAndKey(String qrCodeFileLocation, long roomId, String key,long loginUserId) {
		roomDao.updateRoomQRCodeLocation(qrCodeFileLocation, roomId, key,loginUserId);
	}

	/**
	 * 邀请群链接KEY是否有效
	 */
	@Override
	public boolean isValidInviteKey(long roomId, String key,long loginUserId) { 
		return roomDao.isValidInviteKey(roomId, loginUserId , key);
		 
	}
	
	/**
	 * 保存到通讯录
	 */
	@Override
	@Transactional
	public int saveToContacts(long roomId, int saveToContacts,long loginUserId) {
		return roomDao.saveToContacts(loginUserId, roomId, saveToContacts);
		
	}

	/**
	 * 是否包含群管理员
	 */
	@Override
	public boolean includeAdmin(long roomId, Set<Long> userIds) {
		 return roomDao.includeAdmin(roomId, userIds) > 0;
	} 
	
	
	private String notNullValue(String value){
		return value == null? "" : value.trim();
	}

	/**
	 * 获取已有的群组
	 */
	@Override
	public int getCreatedRoomCnt(long loginUserId) {
		return roomDao.getCreatedRoomCnt(loginUserId);
	}

	/**
	 * 发送邀请
	 */
	@Override 
	@Transactional
	public void sendInvitation(long loginUserId,String roomName, String photoUrl, long roomId, List<ParticipantVo> list) {
		for(ParticipantVo participantVo: list){
			long userId = participantVo.getUserId();
			saveInviteRecord(roomId, loginUserId, userId);  
		} 
	}
	
	/**
	 * 保存邀请链接
	 */
	private InviteRecordVo saveInviteRecord(long roomId, long fromId, long toId) { 
		InviteRecordVo inviteRecordVo = InviteRecordVo.builder().roomId(roomId)
				.fromId(fromId).toId(toId).inviteEndDate(addDays(3)).key(UUID.randomUUID().toString()).build();  
		roomDao.saveInviteRecord(inviteRecordVo); 
		return inviteRecordVo;
	}
	
	private Date addDays(int addDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, addDays);
		return calendar.getTime();
	}
}
