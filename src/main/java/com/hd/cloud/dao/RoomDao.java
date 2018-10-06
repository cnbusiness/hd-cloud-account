package com.hd.cloud.dao;

import java.util.List;
import java.util.Set;

import com.hd.cloud.domain.Participant;
import com.hd.cloud.domain.Room;
import com.hd.cloud.vo.InviteRecordVo;
import com.hd.cloud.vo.RoomInformationVo;
import com.hd.cloud.vo.UserVo;

/**
 * 
  * @ClassName: RoomDao
  * @Description: 群成员
  * @author yaojie yao.jie@hadoop-tech.com
  * @Company hadoop-tech
  * @date 2018年5月11日 上午10:57:24
  *
 */
public interface RoomDao {

	/**
	 * 创建群
	 */
	public Room createRoom(Room room);

	/**
	 * 修改群图片、群名称
	 */
	public int updateRoom(Room room);

	/**
	 * 获取群列表
	 */
	public List<RoomInformationVo> getRoomList(long loginUserId);

	/**
	 * 获取单个群相关属性以及群成员
	 */
	public List<RoomInformationVo> getRoomWithParticipant(long roomId,
			long userId, int pageIndex, int pageSize);

	/**
	 * 删除群
	 */
	public int deleteRoom(long loginUserId, long roomId);

	/**
	  * 获取群信息
	  */
	public RoomInformationVo getRoomById(long roomId);

	/**
	 * 是否是群组
	 */
	public boolean isOwnerInRoom(long roomId, long userId);

	/**
	 * 验证是否是群组或管理员
	 */
	public boolean isOwnerOrAdminInRoom(long roomId, long userId);

	/**
	 * 群现有管理员人数
	 */
	public List<Long> getAdminList(long roomId);

	public boolean userAllExistingInRoom(long roomId, Set<Long> userIds);

	/**
	 * 验证是否存在已在群的成员
	 */
	public boolean existingMemberInside(long roomId, Set<Long> userIds);

	/**
	 * 重置群二维码
	 */
	public void updateRoomQRCodeLocation(String qrCodeFileLocation,
			long roomId, String key,long loginUserId);

	/**
	 * 保存邀请链接
	 */
	public int saveInviteRecord(InviteRecordVo inviteRecordVo);
	
	/**
	 * 校验群的KEY是否有效
	 */
	public boolean isValidKey(long roomId, String key);

	/**
	 * 邀请群链接KEY是否有效
	 */
	public boolean isValidInviteKey(long roomId, long toId, String key);

	public String getNickName(long roomId, long userId);

	/**
	 * 获取在群里的角色
	 */
	public Integer getGroupRole(long roomId, long userId);

	/**
	 * 获取已有的群组
	 */
	public int getCreatedRoomCnt(long loginUserId);

	/**
	 * 群组添加成员
	 */
	public int addParticipant(Participant participant);

	/**
	 * 移除群
	 */
	public int removeParticipant(Participant participant);

	public List<Participant> getParticipantList(long roomId);

	/**
	 * 修改群昵称
	 */
	public int modifyNickNameInRoom(long loginUserId, long roomId,
			String nickNameInRoom);

	/**
	 * 删除群成员
	 */
	public int deleteAllParticipant(long loginUserId, long roomId);

	/**
	 * 更新群角色
	 */
	public int assignNewRole(long loginUserId, long roomId, long userId,
			int role, int saveToContacts);

	/**
	 * 更改新群组
	 */
	public int changeOwner(long loginUserId, long roomId, long userId);

	/**
	 * 取消原群组
	 */
	public int disableOwner(long loginUserId, long roomId);

	/**
	 * 保存到通讯录
	 */
	public int saveToContacts(long userId, long roomId, int saveToContacts);

	/**
	 * 是否包含群管理员
	 */
	public int includeAdmin(long roomId, Set<Long> userIds);

	public int updateNickName(long userId, String nickName);

	public int updateUserIcon(long userId, String userIcon);

	public String getMtalkDomain(long userId);

	public UserVo getUser(long userId);

	/**
	 * 
	* @Title: getUserV2 
	* @param: 
	* @Description: 获取用户信息
	* @return UserVo
	 */
	public UserVo getUserV2(long userId);

	/**
	 * 验证用户是否存在
	 */
	public boolean checkUserWhetherExist(Set<Long> userIds);
}
