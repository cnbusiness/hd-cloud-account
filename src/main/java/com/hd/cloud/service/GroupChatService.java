package com.hd.cloud.service;

import java.util.List;
import java.util.Set;

import com.hd.cloud.domain.Room;
import com.hd.cloud.vo.ModifyRoomVo;
import com.hd.cloud.vo.ParticipantVo;
import com.hd.cloud.vo.RoomAdminDetailVo;
import com.hd.cloud.vo.RoomInformationVo;
import com.hd.cloud.vo.RoomVo;

/**
 * 
  * @ClassName: GroupChatService
  * @Description: 群组
  * @author yaojie yao.jie@hadoop-tech.com
  * @Company hadoop-tech
  * @date 2018年5月11日 上午10:31:08
  *
 */
public interface GroupChatService {

	/**
	 * 
	* @Title: createNewRoom 
	* @param: 
	* @Description: 创建群组 
	* @return Room
	 */
	public Room createNewRoom(RoomVo newRoomVo,long loginUserId);
	
	/**
	 * 
	* @Title: getRoomList 
	* @param: 
	* @Description: 获取所有的群列表
	* @return List<RoomInformationVo>
	 */
	public List<RoomInformationVo> getRoomList(long loginUserId);
	
	/**
	 * 
	* @Title: isValidKey 
	* @param: 
	* @Description: 验证群的KEY是否有效
	* @return boolean
	 */
	public boolean isValidKey(long roomId, String key);
	
	/**
	 * 
	* @Title: isValidInviteKey 
	* @param: 
	* @Description: 邀请群链接KEY是否有效
	* @return boolean
	 */
	public boolean isValidInviteKey(long roomId, String key,long loginUserId);
	
	/**
	 * 
	* @Title: getRoomById 
	* @param: 
	* @Description: 获取群信息
	* @return RoomInformationVo
	 */
	public RoomInformationVo getRoomById(long roomId);
	
	/**
	 * 
	* @Title: getRoomWithParticipant 
	* @param: 
	* @Description: 获取单个群相关属性以及群成员
	* @return List<RoomInformationVo>
	 */
	public List<RoomInformationVo> getRoomWithParticipant(long roomId,long userId, int pageIndex, int pageSize);
	
	/**
	 * 
	* @Title: addParticipantList 
	* @param: 
	* @Description: 加入群
	* @return int
	 */
	public int addParticipantList(long roomId, List<ParticipantVo> list, boolean fromInviteLink, RoomInformationVo roomInformationVo);
	
	/**
	 * 
	* @Title: removeParticipant 
	* @param: 
	* @Description: 移除群
	* @return int
	 */
	public int removeParticipant(long roomId, int role, String jid);
	
	/**
	 * 
	* @Title: removeParticipant 
	* @param: 
	* @Description: 移除群
	* @return int
	 */
	public int removeParticipant(long roomId, List<ParticipantVo> list, boolean isQuitFromGroup, RoomInformationVo roomInformationVo,long loginUserId);  

	/**
	 * 
	* @Title: updateRoom 
	* @param: 
	* @Description: 修改群相关的属性, 1修改群昵称, 2群图片, 3 群主ID
	* @return int
	 */
	public int updateRoom(ModifyRoomVo modifyRoomVo, long roomId, RoomInformationVo roomInformationVo,long loginUserId);

	/**
	 * 
	* @Title: modifyNickNameInRoom 
	* @param: 
	* @Description: 修改群昵称
	* @return int
	 */
	public int modifyNickNameInRoom(long roomId, String nickNameInRoom, RoomInformationVo roomInformationVo,long loginUserId);

	/**
	 * 
	* @Title: deleteRoom 
	* @param: 
	* @Description: 解散群
	* @return int
	 */
	public int deleteRoom(long roomId, RoomInformationVo roomInformationVo,long loginUserId);

	/**
	 * 
	* @Title: setAdminList 
	* @param: 
	* @Description: 设置管理员
	* @return int
	 */
	public int setAdminList(long roomId, List<RoomAdminDetailVo> addList, RoomInformationVo roomInformationVo,long loginUserId);

	/**
	 * 
	* @Title: getAdminList 
	* @param: 
	* @Description: 群现有管理员人数
	* @return List<Long>
	 */
	public List<Long> getAdminList(long roomId); 
	
	/**
	 * 
	* @Title: cancelAdminList 
	* @param: 
	* @Description: 取消群管理员
	* @return int
	 */
	public int cancelAdminList(long roomId,List<RoomAdminDetailVo> removeList, RoomInformationVo roomInformationVo,long loginUserId);
	
	/**
	 * 
	* @Title: checkUserWhetherExist 
	* @param: 
	* @Description: 验证用户是否存在
	* @return boolean
	 */
	public boolean checkUserWhetherExist(Set<Long> userIds);
	
	/**
	 * 
	* @Title: userAllExistingInRoom 
	* @param: 
	* @Description: 验证用户是否都在群组
	* @return boolean
	 */
	public boolean userAllExistingInRoom(long roomId, Set<Long> userIds);
	
	/**
	 * 
	* @Title: existingMemberInside 
	* @param: 
	* @Description: 是否存在已在群的成员
	* @return boolean
	 */
	public boolean existingMemberInside(long roomId, Set<Long> userIds);
	
	/**
	 * 
	* @Title: isOwnerOrAdminInRoom 
	* @param: 
	* @Description: 验证是不是管理员或者群组
	* @return boolean
	 */
	public boolean isOwnerOrAdminInRoom(long roomId, long userId);
	
	/**
	 * 
	* @Title: isOwnerInRoom 
	* @param: 
	* @Description: 是否是群组
	* @return boolean
	 */
	public boolean isOwnerInRoom(long roomId, long userId);

	/**
	 * 
	* @Title: updateRoomQRCodeLocationAndKey 
	* @param: 
	* @Description: 重置群二维码
	* @return void
	 */
	public void updateRoomQRCodeLocationAndKey(String qrCodeFileLocation, long roomId, String key,long loginUserId);
	
	/**
	 * 
	* @Title: sendInvitation 
	* @param: 
	* @Description: 发送邀请链接
	* @return void
	 */
	public void sendInvitation(long loginUserId,String roomName, String photoUrl, long roomId, List<ParticipantVo> list); 

	/**
	 * 
	* @Title: saveToContacts 
	* @param: 
	* @Description: 保存到通讯录
	* @return int
	 */
	public int saveToContacts(long roomId, int saveToContacts,long loginUserId);

	/**
	 * 
	* @Title: getGroupRole 
	* @param: 
	* @Description: 获取用户在此群的角色
	* @return Integer
	 */
	public Integer getGroupRole(long roomId, long userId);

	/**
	 * 
	* @Title: includeAdmin 
	* @param: 
	* @Description: 是否包含群管理员
	* @return boolean
	 */
	public boolean includeAdmin(long roomId, Set<Long> userIds);

	/**
	 * 
	* @Title: getCreatedRoomCnt 
	* @param: 
	* @Description: 获取已有的群组
	* @return int
	 */
	public int getCreatedRoomCnt(long loginUserId);
	
}
