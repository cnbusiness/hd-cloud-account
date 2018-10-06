package com.hd.cloud.rest;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.domain.Room;
import com.hd.cloud.service.GroupChatService;
import com.hd.cloud.util.ErrorCode;
import com.hd.cloud.util.RoleEnum;
import com.hd.cloud.vo.InviteLinkVo;
import com.hd.cloud.vo.ModifyRoomVo;
import com.hd.cloud.vo.ParticipantInfoVo;
import com.hd.cloud.vo.ParticipantListVo;
import com.hd.cloud.vo.ParticipantVo;
import com.hd.cloud.vo.RoomAdminDetailVo;
import com.hd.cloud.vo.RoomAdminVo;
import com.hd.cloud.vo.RoomInformationVo;
import com.hd.cloud.vo.RoomVo;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.controller.RestBase;

/**
 * 
  * @ClassName: GroupChatResource
  * @Description: 群组
  * @author yaojie yao.jie@hadoop-tech.com
  * @Company hadoop-tech
  * @date 2018年5月9日 下午3:52:38
  *
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/chatrooms")
public class GroupChatResource extends RestBase {

	public static final int QRCODE_TYPE = 5;
	
	private static final int MAX_CREATED_ROOM =100;
	
	private static final int MIN_MEMBER_SIZE = 1;
	
	@Autowired
	private GroupChatService groupChatService;

	/**
	 * 
	* @Title: createNewRoom 
	* @param: 
	* @Description: 创建群组
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "", notes = "")
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil createNewRoom(final @RequestBody RoomVo roomVo) {
		long loginUserId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		String roomName = roomVo.getRoomName();
		if (roomName == null) {
			roomVo.setRoomName("");
		} else {
			roomVo.setRoomName(roomName.trim());
		}
		// 获取已有的群组
		int cnt = groupChatService.getCreatedRoomCnt(loginUserId);
		if (cnt >= MAX_CREATED_ROOM) {
			boUtil.setCode(ErrorCode.LIMIT_TO_100);
			boUtil.setMsg("one person only allow to create 100 rooms!");
			return boUtil;
		}
		List<ParticipantVo> participantList = roomVo.getParticipantList();
		// 验证是否有添加群成员
		if (participantList == null || participantList.size() == 0) {
			boUtil.setCode(ErrorCode.AT_LEAST_ONE_MEMBER);
			boUtil.setMsg("At least one member in group!");
			return boUtil;
		} else {
			Set<Long> userSet = new HashSet<Long>();
			// 添加自己
			userSet.add(loginUserId);
			for (ParticipantVo participantVo : participantList) {
				long userId = participantVo.getUserId();
				try {
					userSet.add(userId);
				} catch (NumberFormatException numberFormatException) {
					boUtil.setCode(ErrorCode.INVALID_USER_ID);
					boUtil.setMsg("Invalid user id  " + userId);
					return boUtil;
				}
			}
			//是否有重复人
			if (userSet.size() != participantList.size() + 1) {
				boUtil.setCode(ErrorCode.DUPLICATE_USER_ID);
				boUtil.setMsg("Duplicate user id inside");
				return boUtil;
			}
			//超过群组人数上限
			if (userSet.size() > 500) {
				boUtil.setCode(ErrorCode.ROOM_MEMBER_EXCEED);
				boUtil.setMsg("Room member exceed");
				return boUtil;
			}
			// 验证用户是否存在
			if (!groupChatService.checkUserWhetherExist(userSet)) {
				boUtil.setCode(ErrorCode.USER_NOT_EXIST);
				boUtil.setMsg("User not exist");
				return boUtil;
			}
		}
		// 创建群组
		Room room = groupChatService.createNewRoom(roomVo,loginUserId);
		if (room.getId() == null) {
			boUtil.setCode(ErrorCode.FAILED_TO_CREATE_ROOM);
			boUtil.setMsg("Failed to create room!");
			return boUtil;
		}
		// TODO 更新群组二维码
		try {
			// String randomKey = UUID.randomUUID().toString();
			// String qrCodeFileLocation =
			// fileThriftClient.getQrUrl(room.getId(), QRCODE_TYPE, randomKey);
			// String qrCodeFileLocation = "abc.jpg";
			// if(qrCodeFileLocation != null &&
			// !"".equals(qrCodeFileLocation.trim())){
			// room.setQrcodeUrl(qrCodeFileLocation);
			// groupChatService.updateRoomQRCodeLocationAndKey(qrCodeFileLocation,
			// room.getId(), randomKey);
			// }
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		boUtil = BoUtil.getDefaultTrueBo();
		boUtil.setData(room);
		return boUtil;
	}
 
	/**
	 * 
	* @Title: getRoomList 
	* @param: 
	* @Description: 获取所有的群列表
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "", notes = "")
	@ResponseBody
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil getRoomList() {
		long loginUserId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo(); 
		List<RoomInformationVo> roomList = groupChatService.getRoomList(loginUserId);
		boUtil.setData(roomList);
		return boUtil;
	}
 	
	/**
	 * 
	* @Title: getRoomParticipantList 
	* @param: 
	* @Description: 获取单个群相关属性以及群成员 
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "participants", notes = "participants")
	@ResponseBody
	@RequestMapping(value = "participants", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil getRoomParticipantList(@QueryParam("roomId") Long roomId,
			@QueryParam("qrcode") String qrcode, @QueryParam("key") String key,
			@QueryParam("pageIndex") Integer pageIndex,
			@QueryParam("pageSize") Integer pageSize) {
		long loginUserId = super.getLoginUserID();
		roomId = roomId == null ? 0 : roomId.longValue();
		pageIndex = pageIndex == null ? 0 : roomId.intValue();
		pageSize = pageSize == null ? 50 : roomId.intValue();
		if (pageIndex <= 1) {// 不合法数字过滤
			pageIndex = 0;
		} else {
			pageIndex = (pageIndex - 1) * pageSize;
		}
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		if(roomId <= 0){ 
			boUtil.setCode(ErrorCode.ROOM_ID_INVALID);
			boUtil.setMsg("Room id invalid or null!");
			return boUtil;
		}
		if(qrcode != null && !"".equals(qrcode.trim())){
			//校验群的KEY是否有效
			if(!groupChatService.isValidKey(roomId, qrcode)){//invalid
				boUtil.setCode(ErrorCode.ROOM_QRCODE_INVALID);
				boUtil.setMsg("Room QRCode invalid");
				return boUtil;
			}
		}
		//邀请群链接KEY是否有效
		if(key != null && !"".equals(key.trim())){//from invite link 
				//check key
			if(!groupChatService.isValidInviteKey(roomId, key,loginUserId)){
				boUtil.setCode(ErrorCode.INVITE_LINK_INVALID);
				boUtil.setMsg("Invite link invalid " + key);
				return boUtil;
			}  
		}
		//获取单个群相关属性以及群成员 
		List<RoomInformationVo> roomInformationBoList = groupChatService
				.getRoomWithParticipant(roomId, loginUserId, pageIndex,pageSize);
		if(roomInformationBoList == null || roomInformationBoList.size() == 0){
			boUtil.setCode(ErrorCode.ROOM_NOT_EXIST);
			boUtil.setMsg("Room not exist!");
			return boUtil;
		} 
		boUtil = BoUtil.getDefaultTrueBo(); 
		boUtil.setData(roomInformationBoList);
		return boUtil;
	}
	
	/**
	 * 
	* @Title: addToRoom 
	* @param: 
	* @Description: 加入群成员
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "participants", notes = "participants")
	@ResponseBody
	@RequestMapping(value = "participants", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil addToRoom(final @RequestBody ParticipantListVo participantListVo) {
		long loginUserId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		long roomId = participantListVo.getRoomId();
		//获取群信息
		RoomInformationVo roomInformationVo  = groupChatService.getRoomById(roomId); 
		if(roomInformationVo == null ){
			boUtil.setCode(ErrorCode.ROOM_NOT_EXIST);
			boUtil.setMsg("Room not exist");
			return boUtil;
		} 
		int maxCnt = roomInformationVo.getMaxCnt();
		int currentCnt = roomInformationVo.getNowCnt();
		if(currentCnt >= maxCnt){//人员已满
			boUtil.setCode(ErrorCode.ROOM_MEMBER_EXCEED);
			boUtil.setMsg("Room member exceed!");
			return boUtil;
		}
		//1==允许任何人
		if(roomInformationVo.getPublicType()  == 1){ //open to public， every can add member 
			
		}else{ //only admin/owner can add member
			//验证是否是群组或管理员
			if(!groupChatService.isOwnerOrAdminInRoom(roomId, getLoginUserID())){
				boUtil.setCode(ErrorCode.ONLY_OWNER_OR_ADMIN_CAN_ADD_MEMBER);
				boUtil.setMsg("Only owner or admin can add member");
				return boUtil;
			}
		} 
		 
		boolean inviteFromLink = false; 
		if(participantListVo == null || participantListVo.getParticipantList() == null ||  participantListVo.getParticipantList().size() == 0 ){
			boUtil.setCode(ErrorCode.AT_LEAST_ONE_MEMBER_WILL_BE_ADDED);
			boUtil.setMsg("At least one member will be added!");
			return boUtil;
		}else{  
			List<ParticipantVo> participantList = participantListVo.getParticipantList();
			Set<Long> userIds = new HashSet<Long>(); 
			for(ParticipantVo participantVo : participantList){
				try{
					userIds.add(participantVo.getUserId());
				}catch(NumberFormatException numberFormatException){
					boUtil.setCode(ErrorCode.INVALID_USER_ID);
					boUtil.setMsg("Invalid user id  " + participantVo.getUserId());
					return boUtil;
				} 
			}
			//是否有重复
			if(userIds.size() != participantList.size()){
				boUtil.setCode(ErrorCode.DUPLICATE_USER_ID);
				boUtil.setMsg("Duplicate user id inside");
				return boUtil;
			}
			 
			//验证用户是否存在
			if(!groupChatService.checkUserWhetherExist(userIds)){
				boUtil.setCode(ErrorCode.USER_NOT_EXIST);
				boUtil.setMsg("User not exist");
				return boUtil;
			}
			
			//验证是否存在已在群的成员
			if(groupChatService.existingMemberInside(roomId, userIds)){
				boUtil.setCode(ErrorCode.EXISTING_MEMBER_IN_ROOM);
				boUtil.setMsg("Existing member in this room " + roomId);
				return boUtil;
			} 
			
			if(participantListVo.getParticipantList().size() == 1 && loginUserId == participantListVo.getParticipantList().get(0).getUserId()){
				inviteFromLink = true; //自己加群，需要验证
			}else{
				if(currentCnt + participantList.size() > 50){//群超过50人，需要验证
					if(participantListVo.getNeedToInvite() == 1){
						boUtil = BoUtil.getDefaultTrueBo(); 
						//发送邀请
						groupChatService.sendInvitation(loginUserId,
								roomInformationVo.getRoomName(),
								roomInformationVo.getPhotoUrl(), roomId,
								participantListVo.getParticipantList());
						return boUtil;
					}else{
						boUtil.setCode(ErrorCode.NEED_TO_SEND_INVITATION);
						boUtil.setMsg("Need to send invitation!");
						return boUtil;
					}  
				}
			}  
		}
		boUtil = BoUtil.getDefaultTrueBo();
		//加群
		int result = groupChatService.addParticipantList(roomId,
				participantListVo.getParticipantList(), inviteFromLink,
				roomInformationVo);
		boUtil.setData(result);
		return boUtil;
	}
	
	/**
	 * 
	* @Title: addToRoomFromInviteLink 
	* @param: 
	* @Description: 通过邀请链接加入群
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "link", notes = "link")
	@ResponseBody
	@RequestMapping(value = "link", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil addToRoomFromInviteLink(final @RequestBody InviteLinkVo inviteLinkVo) {
		long loginUserId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		long roomId = inviteLinkVo.getRoomId();
		RoomInformationVo roomInformationVo = groupChatService.getRoomById(roomId); 
		if(roomInformationVo == null ){
			boUtil.setCode(ErrorCode.ROOM_NOT_EXIST);
			boUtil.setMsg("Room not exist");
			return boUtil;
		}
		int maxCnt = roomInformationVo.getMaxCnt();
		int currentCnt = roomInformationVo.getNowCnt();
		
		if(currentCnt + 1 > maxCnt){
			boUtil.setCode(ErrorCode.ROOM_MEMBER_EXCEED);
			boUtil.setMsg("Room member exceed!");
			return boUtil;
		}
		    
		Set<Long> userIds = new HashSet<>();
		userIds.add(loginUserId);
		//验证用户是否存在
		if(!groupChatService.checkUserWhetherExist(userIds)){
			boUtil.setCode(ErrorCode.USER_NOT_EXIST);
			boUtil.setMsg("User not exist");
			return boUtil;
		};   
		//验证是否已加入群组
		if(groupChatService.existingMemberInside(roomId, userIds)){
			boUtil.setCode(ErrorCode.EXISTING_MEMBER_IN_ROOM);
			boUtil.setMsg("Existing member in this room " + roomId);
			return boUtil;
		} 
		//验证key是否有效
		if(!groupChatService.isValidInviteKey(roomId, inviteLinkVo.getKey(),loginUserId)){
			boUtil.setCode(ErrorCode.INVITE_LINK_INVALID);
			boUtil.setMsg("Invite link invalid " + inviteLinkVo.getKey());
			return boUtil;
		} 
		boUtil = BoUtil.getDefaultTrueBo(); 
		List<ParticipantVo> participantList = new ArrayList<ParticipantVo>();
		ParticipantVo participantVo = new ParticipantVo();
		participantVo.setUserId(loginUserId);
		participantVo.setRole(RoleEnum.MEMBER.getIntValue());
		participantList.add(participantVo);
		int result = groupChatService.addParticipantList(roomId, participantList, true, roomInformationVo);
		boUtil.setData(result);
		return boUtil;
	}
	
	/**
	 * 
	* @Title: deleteParticipant 
	* @param: 
	* @Description: 删除成员
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "participant/remove", notes = "participant/remove")
	@ResponseBody
	@RequestMapping(value = "participant/remove", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil deleteParticipant(final @RequestBody ParticipantListVo participantListVo) {
		long loginUserId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		long roomId = participantListVo.getRoomId();
		RoomInformationVo roomInformationVo = groupChatService.getRoomById(roomId); 
		if(roomInformationVo == null ){
			boUtil.setCode(ErrorCode.ROOM_NOT_EXIST);
			boUtil.setMsg("Room not exist");
			return boUtil;
		}
		boolean isQuitFromGroup = false; 
		Set<Long> userIds = new HashSet<Long>(); 
		if(participantListVo == null || participantListVo.getParticipantList() == null ||  participantListVo.getParticipantList().size() == 0 ){
			boUtil.setCode(ErrorCode.AT_LEAST_ONE_MEMBER_WILL_BE_REMOVED);
			boUtil.setMsg("At least one member will be removed!");
			return boUtil;
		}else{  
			List<ParticipantVo> participantList = participantListVo.getParticipantList();
			if(participantList.size() ==  1 && loginUserId == participantList.get(0).getUserId()){
				isQuitFromGroup = true;//退出群组
			}else{
				for(ParticipantVo participantVo : participantList){
					long userId = participantVo.getUserId(); 
					try{
						userIds.add(userId);
					}catch(NumberFormatException numberFormatException){
						boUtil.setCode(ErrorCode.INVALID_USER_ID);
						boUtil.setMsg("Invalid user id  "+ userId);
						return boUtil;
					} 
				}
				if(userIds.size() != participantList.size()){
					boUtil.setCode(ErrorCode.DUPLICATE_USER_ID);
					boUtil.setMsg("Duplicate user id inside");
					return boUtil;
				}
				//验证用户是否都在群组
				if(!groupChatService.userAllExistingInRoom(roomId, userIds)){
					boUtil.setCode(ErrorCode.USER_NOT_IN_THIS_ROOM);
					boUtil.setMsg("User not in this room");
					return boUtil;
				};    
			}  
		} 
		if(isQuitFromGroup){  //自己退出群组
			
		}else{ 
			int nowCnt = roomInformationVo.getNowCnt();
			//获取在群里的角色
			Integer roleValue = groupChatService.getGroupRole(roomId, loginUserId);
			if(roleValue != null){
				if(roleValue == 3){  //owner
					if(nowCnt - userIds.size() < MIN_MEMBER_SIZE){//群里至少要有一个人
						boUtil.setCode(ErrorCode.MIN_MEMBER_SIZE);
						boUtil.setMsg("Group at least has more than one member");
						return boUtil;
					}
				}else if(roleValue == 2){ //admin
					//验证是否包含群管理员
					if(groupChatService.includeAdmin(roomId, userIds)){
						boUtil.setCode(ErrorCode.ONLY_OWNER_CAN_REMOVE_ADMIN);
						boUtil.setMsg("Only owner can remove admin");
						return boUtil;
					} 
					if(nowCnt - userIds.size() < MIN_MEMBER_SIZE){//群里至少要有一个人
						boUtil.setCode(ErrorCode.MIN_MEMBER_SIZE);
						boUtil.setMsg("Group at least has more than one member");
						return boUtil;
					}
				}else{//普通成员不能移除群员
					boUtil.setCode(ErrorCode.ONLY_OWNER_OR_ADMIN_CAN_REMOVE_MEMBER);
					boUtil.setMsg("Only owner or admin can remove member");
					return boUtil;
				}
			} 
		}
		boUtil = BoUtil.getDefaultTrueBo();  
		int result = groupChatService.removeParticipant(roomId,
				participantListVo.getParticipantList(), isQuitFromGroup,
				roomInformationVo,loginUserId);
		boUtil.setData(result);
		return boUtil;
	}
	
	/**
	 * 
	* @Title: updateRoom 
	* @param: 
	* @Description: 修改群相关的属性, 1修改群昵称, 2群图片, 3 群主ID
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "", notes = "")
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public BoUtil updateRoom(final @RequestBody ModifyRoomVo modifyRoomVo) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		long loginUserId = super.getLoginUserID();
		long roomId = modifyRoomVo.getRoomId();
		RoomInformationVo roomInformationVo = groupChatService.getRoomById(roomId);
		if(roomInformationVo == null ){
			boUtil.setCode(ErrorCode.ROOM_NOT_EXIST);
			boUtil.setMsg("Room not exist");
			return boUtil;
		} 
		String ownerId = modifyRoomVo.getOwnerId();
		String roomName = modifyRoomVo.getRoomName();
		String photoUrl = modifyRoomVo.getPhotoUrl();
		int roleType = roomInformationVo.getRoleType();
		if(ownerId != null && !"".equals(ownerId.trim())){  //change the owner
			if(roleType != RoleEnum.OWNER.getIntValue()){
				boUtil.setCode(ErrorCode.ONLY_OWNER_CAN_CHANGE_OWNER);
				boUtil.setMsg("Only owner can change owner");
				return boUtil;
			}
		}else if(roomName != null && !"".equals(roomName.trim())){
			if(roleType < RoleEnum.ADMIN.getIntValue() ){
				boUtil.setCode(ErrorCode.ONLY_OWNER_OR_ADMIN_CAN_CHANGE_GROUP_INF);
				boUtil.setMsg("Only owner or admin can change group information");
				return boUtil;
			}
			
		}else if(photoUrl != null && !"".equals(photoUrl.trim())){
			if(roleType < RoleEnum.ADMIN.getIntValue()){
				boUtil.setCode(ErrorCode.ONLY_OWNER_OR_ADMIN_CAN_CHANGE_GROUP_INF);
				boUtil.setMsg("Only owner or admin can change group information");
				return boUtil;
			}
		}else{
			boUtil.setCode(ErrorCode.NOTHING_GOT_CHANGED);
			boUtil.setMsg("Nothing got changed");
			return boUtil;
		} 
		int result = groupChatService.updateRoom(modifyRoomVo, roomId, roomInformationVo,loginUserId); 
		boUtil = BoUtil.getDefaultTrueBo();
		boUtil.setData(result);
		return boUtil;
	} 
	
	/**
	 * 
	* @Title: getRoom 
	* @param: 
	* @Description: 获取群相关属性
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "", notes = "")
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil getRoom(@QueryParam("roomId") Long roomId, @QueryParam("key") String key) {
		roomId = roomId == null ? 0 : roomId.longValue();
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		if(key != null && !"".equals(key.trim())){  //校验群的KEY是否有效
			if(!groupChatService.isValidKey(roomId, key)){
				boUtil.setCode(ErrorCode.ROOM_QRCODE_INVALID);
				boUtil.setMsg("Room QRCode invalid");
				return boUtil;
			}
		} 
		RoomInformationVo roomInformationVo = groupChatService.getRoomById(roomId);
		if(roomInformationVo == null ){
			boUtil.setCode(ErrorCode.ROOM_NOT_EXIST);
			boUtil.setMsg("Room not exist");
			return boUtil;
		} 
		boUtil = BoUtil.getDefaultTrueBo();
		boUtil.setData(roomInformationVo);
		return boUtil;
	}
	
	/**
	 * 
	* @Title: deleteRoom 
	* @param: 
	* @Description: 解散群
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "DELETE", value = "", notes = "")
	@ResponseBody
	@RequestMapping(value = "", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/*")
	public BoUtil deleteRoom(@QueryParam("roomId") Long roomId) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		long loginUserId = super.getLoginUserID();
		roomId = roomId == null ? 0 : roomId.longValue();
		RoomInformationVo roomInformationVo = groupChatService.getRoomById(roomId);
		if(roomInformationVo == null ){
			boUtil.setCode(ErrorCode.ROOM_NOT_EXIST);
			boUtil.setMsg("Room not exist");
			return boUtil;
		} 
		//是否是群组
		if(!groupChatService.isOwnerInRoom(roomId, loginUserId)){
			boUtil.setCode(ErrorCode.ONLY_OWNER_CAN_DESTORY_ROOM);
			boUtil.setMsg("Only owner can destroy the room");
			return boUtil;
		}
		boUtil = BoUtil.getDefaultTrueBo(); 
		int result = groupChatService.deleteRoom(roomId, roomInformationVo,loginUserId); 
		boUtil.setData(result);
		return boUtil;
	} 
	
	/**
	 * 
	* @Title: setGroupAdmin 
	* @param: 
	* @Description: 设置管理员 
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "admin", notes = "admin")
	@ResponseBody
	@RequestMapping(value = "admin", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/*")
	public BoUtil setGroupAdmin(final @RequestBody RoomAdminVo roomAdminVo) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		long loginUserId = super.getLoginUserID();
		long roomId = roomAdminVo.getRoomId();
		RoomInformationVo roomInformationVo = groupChatService.getRoomById(roomId);
		if(roomInformationVo == null ){
			boUtil.setCode(ErrorCode.ROOM_NOT_EXIST);
			boUtil.setMsg("Room not exist");
			return boUtil;
		} 
		
	    List<RoomAdminDetailVo> addList = roomAdminVo.getAddList();
	    List<RoomAdminDetailVo> removeList = roomAdminVo.getRemoveList();
		int result = 0;
		if(addList != null && addList.size() > 0){  //new admin list
			//验证是否是群组或管理员
			if(!groupChatService.isOwnerOrAdminInRoom(roomId, loginUserId)){
				boUtil.setCode(ErrorCode.ONLY_OWNER_OR_ADMIN_CAN_SET_ADMIN);
				boUtil.setMsg("Only owner or admin can set admin");
				return boUtil;
			}
			//群现有管理员人数
			List<Long> adminIds = groupChatService.getAdminList(roomId);  
			if((adminIds.size() + addList.size()) > 5){//一个人群只能有5个管理员
				boUtil.setCode(ErrorCode.ADMIN_CANNOT_MORE_THAN_FIVE);
				boUtil.setMsg("Admin cannot more than five");
				return boUtil;
			}
			//新增管理员
			result = groupChatService.setAdminList(roomId, addList, roomInformationVo,loginUserId);
		}else if(removeList != null && removeList.size() > 0){ //cancel admin
			if(!groupChatService.isOwnerOrAdminInRoom(roomId, loginUserId)){
				boUtil.setCode(ErrorCode.ONLY_OWNER_OR_ADMIN_CAN_CANCEL_ADMIN);
				boUtil.setMsg("Only owner or admin can cancel admin");
				return boUtil;
			}
			//取消管理员
			result = groupChatService.cancelAdminList(roomId, removeList, roomInformationVo,loginUserId);
		}
		boUtil = BoUtil.getDefaultTrueBo(); 
		boUtil.setData(result);
		return boUtil;
	} 
	
	/**
	 * 
	* @Title: generateQRCode 
	* @param: 
	* @Description: 重置二维码
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "qrcode", notes = "qrcode")
	@ResponseBody
	@RequestMapping(value = "qrcode", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil generateQRCode(@PathParam("roomId") Long roomId) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		roomId = roomId == null ? 0 : roomId.longValue();
		RoomInformationVo roomInformationVo = groupChatService.getRoomById(roomId);
		if(roomInformationVo == null ){
			boUtil.setCode(ErrorCode.ROOM_NOT_EXIST);
			boUtil.setMsg("Room not exist");
			return boUtil;
		} 
		boUtil = BoUtil.getDefaultTrueBo(); 
		try{
			//TODO 生成二维码
//			String newKey = UUID.randomUUID().toString();
//			String qrCodeFileLocation = fileThriftClient.getQrUrl(roomId, QRCODE_TYPE, newKey);  
//			if(qrCodeFileLocation != null && !"".equals(qrCodeFileLocation.trim())){ 
//				groupChatService.updateRoomQRCodeLocationAndKey(qrCodeFileLocation, roomId, newKey);
//			}
//			
//			boUtil.setData(qrCodeFileLocation);
		}catch(Exception e){
			log.error(e.getMessage(), e);
		} 
		return boUtil;
	}
	
	/**
	 * 
	* @Title: saveToContacts 
	* @param: 
	* @Description: 修改群成员属性
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "participants", notes = "participants")
	@ResponseBody
	@RequestMapping(value = "participants", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public BoUtil saveToContacts(final @RequestBody ParticipantInfoVo participantInfoVo) {
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		long loginUserId = super.getLoginUserID();
		long roomId = participantInfoVo.getRoomId();
		RoomInformationVo roomInformationVo = groupChatService.getRoomById(roomId);
		if(roomInformationVo == null ){
			boUtil.setCode(ErrorCode.ROOM_NOT_EXIST);
			boUtil.setMsg("Room not exist");
			return boUtil;
		} 
		String nickName = participantInfoVo.getNickName();
		if(nickName != null && !"".equals(nickName.trim())){  //修改自己在群里的昵称
			groupChatService.modifyNickNameInRoom(roomId, nickName, roomInformationVo,loginUserId);   
		}else{ //是否保存到通讯录
			groupChatService.saveToContacts(roomId, participantInfoVo.getSaveToContacts(),loginUserId);
		}     
		boUtil = BoUtil.getDefaultTrueBo();  
		return boUtil;
	}
	
}
