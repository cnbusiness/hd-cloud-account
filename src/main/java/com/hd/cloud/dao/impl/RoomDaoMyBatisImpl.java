package com.hd.cloud.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.hd.cloud.dao.RoomDao;
import com.hd.cloud.dao.mapper.RoomMapper;
import com.hd.cloud.domain.Participant;
import com.hd.cloud.domain.Room;
import com.hd.cloud.vo.InviteRecordVo;
import com.hd.cloud.vo.ParticipantVo;
import com.hd.cloud.vo.RoomInformationVo;
import com.hd.cloud.vo.UserVo;

@Repository 
public class RoomDaoMyBatisImpl implements RoomDao {

	@Inject
    private RoomMapper roomMapper;

	/**
	 * 获取群列表
	 */
	@Override
	public List<RoomInformationVo> getRoomList(long loginUserId) {
		return roomMapper.getRoomList(loginUserId);
    }

	/**
	 * 创建群
	 */
	@Override
	public Room createRoom(Room room) {
		roomMapper.saveRoom(room);
		return room;
	}

	/**
	 * 修改群图片、群名称
	 */
	@Override
	public int updateRoom(Room room) {
		return roomMapper.updateRoom(room);
	}

	/**
	 * 获取单个群相关属性以及群成员
	 */
	@Override
	public List<RoomInformationVo> getRoomWithParticipant(long roomId, long userId, int pageIndex, int pageSize) {
		//获取群信息
		List<RoomInformationVo> roomInformationVoList = roomMapper.getRoomWithParticipant(roomId, userId);
		for(RoomInformationVo roomInformationVo:roomInformationVoList){
			//获取群成员
			roomInformationVo.setParticipantList(selectParticipant(roomId, pageIndex, pageSize));
		} 
		return roomInformationVoList;
	}
	
	/**
	 * 获取群成员
	 */
	private List<ParticipantVo>selectParticipant(long roomId, int pageIndex, int pageSize){
		return roomMapper.selectParticipant(roomId, pageIndex, pageSize);
	}

	/**
	 * 删除群
	 */
	@Override
	public int deleteRoom(long loginUserId, long roomId) {
		return roomMapper.deleteRoom(loginUserId, roomId);
	}

	/**
	  * 获取群信息
	  */
	@Override
	public RoomInformationVo getRoomById(long roomId) {
		return roomMapper.getRoomById(roomId);
	}

	/**
	 * 是否是群组
	 */
	@Override
	public boolean isOwnerInRoom(long roomId, long userId) {
		int role =  roomMapper.getGroupRole(roomId, userId);
	    return role == 3;
	}

	/**
	 * 验证是否是群组或管理员
	 */
	@Override
	public boolean isOwnerOrAdminInRoom(long roomId, long userId) {
		Integer role =  roomMapper.getGroupRole(roomId, userId);
		if(role == null){
			return  false;
		}else{
			return role == 2 || role == 3;
		} 
	}
	
	/**
	 * 获取在群里的角色
	 */
	@Override
	public Integer getGroupRole(long roomId, long userId) {
		 return roomMapper.getGroupRole(roomId, userId);
	}

	/**
	 * 群现有管理员人数
	 */
	@Override
	public List<Long> getAdminList(long roomId) {
		return roomMapper.getAdminList(roomId);
	}

	@Override
	public boolean userAllExistingInRoom(long roomId, Set<Long> userIds) {
		int result = roomMapper.userAllExistingInRoom(roomId, userIds);
		return result == userIds.size();
	}

	/**
	 * 验证是否存在已在群的成员
	 */
	@Override
	public boolean existingMemberInside(long roomId, Set<Long> userIds) {
		int result = roomMapper.userAllExistingInRoom(roomId, userIds);
		return result > 0;
	}

	/**
	 * 重置群二维码
	 */
	@Override
	public void updateRoomQRCodeLocation(String qrCodeFileLocation, long roomId, String key,long loginUserId) {
		roomMapper.updateRoomQRCodeLocation(qrCodeFileLocation, roomId, key,loginUserId);
	}

	/**
	 * 保存邀请链接
	 */
	@Override
	public int saveInviteRecord(InviteRecordVo inviteRecordVo) {
		return roomMapper.saveInviteRecord(inviteRecordVo);
	}
	
	/**
	 * 校验群的KEY是否有效
	 */
	@Override
	public boolean isValidKey(long roomId, String key) {
		int result = roomMapper.isValidKey(roomId, key);
		return result > 0;
	}

	/**
	 * 邀请群链接KEY是否有效
	 */
	@Override
	public boolean isValidInviteKey(long roomId, long toId, String key) {
		int result = roomMapper.isValidInviteKey(roomId, toId, key, new Date());
		return result > 0;
	} 
	

	@Override
	public String getNickName(long roomId, long userId) {
		return roomMapper.getNickName(roomId, userId);
	}

	/**
	 * 获取已有的群组
	 */
	@Override
	public int getCreatedRoomCnt(long loginUserId) {
		return roomMapper.getCreatedRoomCnt(loginUserId);
	}
	
	/**
	 * 群组添加成员
	 */
	@Override
	public int addParticipant(Participant participant) {
		return roomMapper.saveParticipant(participant);
	}

	@Override
	public List<Participant> getParticipantList(long roomId) {
		return roomMapper.getRoomParticipantList(roomId);
	}

	/**
	 * 移除群
	 */
	@Override
	public int removeParticipant(Participant participant) {
		return roomMapper.removeParticipant(participant);
	}

	/**
	 * 修改群昵称
	 */
	@Override
	public int modifyNickNameInRoom(long loginUserId, long roomId,
			String nickNameInRoom) {
		return roomMapper.modifyNickNameInRoom(loginUserId, roomId, nickNameInRoom);
	}

	/**
	 * 删除群成员
	 */
	@Override
	public int deleteAllParticipant(long loginUserId, long roomId) {
		return roomMapper.deleteAllParticipant(loginUserId, roomId);
	}

	/**
	 * 更新群角色
	 */
	@Override
	public int assignNewRole(long loginUserId, long roomId, long userId, int role, int saveToContacts) {
		return roomMapper.assignNewRole(loginUserId, roomId, userId, role, saveToContacts);
	}

	/**
	 * 更改新群组
	 */
	@Override
	public int changeOwner(long loginUserId, long roomId, long userId) {
		return roomMapper.changeOwner(loginUserId, roomId, userId);
	}

	/**
	 * 取消原群组
	 */
	@Override
	public int disableOwner(long loginUserId, long roomId) {
		return roomMapper.disableOwner(loginUserId, roomId);
		
	}

	/**
	 * 保存到通讯录
	 */
	@Override
	public int saveToContacts(long userId, long roomId, int saveToContacts) {
		return roomMapper.saveToContacts(userId, roomId, saveToContacts);
	}

	/**
	 * 是否包含群管理员
	 */
	@Override
	public int includeAdmin(long roomId, Set<Long> userIds) {
		return roomMapper.includeAdmin(roomId, userIds);
	}

	@Override
	public int updateNickName(long userId, String nickName) {
		return roomMapper.updateNickName(userId, nickName);
	}

	@Override
	public int updateUserIcon(long userId, String userIcon) {
		return roomMapper.updateUserIcon(userId, userIcon);
	}
	
	@Override
	public String getMtalkDomain(long userId) {
		return roomMapper.getMtalkDomain(userId);
	}

	/**
	 * 验证用户是否存在
	 */
	@Override
	public boolean checkUserWhetherExist(Set<Long> userIds) {
		int count = roomMapper.checkUserWhetherExist(userIds);
		return userIds.size() == count;
	}

	@Override
	public UserVo getUser(long userId) {
		return roomMapper.getUser(userId);
	}

	/**
	 * 获取用户信息
	 */
	@Override
	public UserVo getUserV2(long userId) {
		return roomMapper.getUserV2(userId);
	} 
}

