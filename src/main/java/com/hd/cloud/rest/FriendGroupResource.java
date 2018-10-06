package com.hd.cloud.rest;

import io.swagger.annotations.ApiOperation;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.bo.FriendBo;
import com.hd.cloud.bo.FriendGroupBo;
import com.hd.cloud.service.FriendGroupService;
import com.hd.cloud.util.ErrorCode;
import com.hd.cloud.vo.FriendGroupVo;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.controller.RestBase;
import com.hlb.cloud.util.StringUtil;

/**
 * 
 * @ClassName: FriendGroupResource
 * @Description: 好友分組
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月12日 下午4:19:47
 *
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/friendgroup")
public class FriendGroupResource extends RestBase {

	@Autowired
	private FriendGroupService friendGroupService;

	/**
	 * 
	 * @Title: createGroup
	 * @param:
	 * @Description: 创建好友分组
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "add", notes = "add")
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil createGroup(final @RequestBody FriendGroupVo payload) {
		long userId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		if (StringUtil.isBlank(payload.getGroupName())) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUP_EMPTY_ERROR);
			boUtil.setMsg("Friends group name cannot be empty");
			return boUtil;
		}
		boolean flag = friendGroupService.isGroupNameExist(userId,
				payload.getGroupName());
		if (flag) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUP_EXISTS_ERROR);
			boUtil.setMsg("Friends group already exists");
			return boUtil;
		} else {
			// 创建之后将群ID与群名对应关系存到redis
			FriendGroupBo bo = friendGroupService.addGroup(userId, payload);
			boUtil.setData(bo);
		}
		return boUtil;
	}

	/**
	 * 
	 * @Title: updateGroup
	 * @param:
	 * @Description: 编辑组名
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "edit", notes = "edit")
	@ResponseBody
	@RequestMapping(value = "edit", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public BoUtil updateGroup(final @RequestBody FriendGroupVo payload) {
		log.info("update group by FriendGroupVo @ {}", payload);
		long userId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		if (StringUtil.isBlank(payload.getGroupName())) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUP_EMPTY_ERROR);
			boUtil.setMsg("Friends group name cannot be empty");
			return boUtil;
		}
		if (payload.getGroupId() <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_EMPTY_ERROR);
			boUtil.setMsg("Group id cannot be empty");
			return boUtil;
		}
		FriendGroupBo bo = friendGroupService.findOne(userId,
				payload.getGroupId());
		if (bo == null) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_NOT_EXISTS);
			boUtil.setMsg("Group does not exist");
			return boUtil;
		}
		// 同名不处理
		if (!bo.getName().equals(payload.getGroupName())) {
			boolean flag = friendGroupService.isGroupNameExist(userId,
					payload.getGroupName());
			if (flag) {
				boUtil = BoUtil.getDefaultFalseBo();
				boUtil.setCode(ErrorCode.FRIENDS_GROUP_EXISTS_ERROR);
				boUtil.setMsg("Friends group already exists");
				return boUtil;
			} else {
				// 创建之后将群ID与群名对应关系存到redis
				FriendGroupBo friendGroupBo = friendGroupService.updateGroup(
						userId, payload);
				boUtil.setData(friendGroupBo);
			}
		}
		return boUtil;
	}

	/**
	 * 
	 * @Title: getAllGroup
	 * @param:
	 * @Description: 分组列表
	 * @return ListBoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "all", notes = "all")
	@ResponseBody
	@RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil getAllGroup() {
		log.debug("find group list");
		long userId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		// 从redis里获取用户加入的好友分组
		List<FriendGroupBo> list = friendGroupService.findAll(userId);
		boUtil.setData(list);
		return boUtil;
	}

	/**
	 * 
	 * @Title: delGroupById
	 * @param:
	 * @Description: 删除好友分组
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "DELETE", value = "delete", notes = "delete")
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/*")
	public BoUtil delGroupById(@QueryParam("groupId") Long groupId) {
		long userId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		if (groupId == null || groupId.longValue() <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_EMPTY_ERROR);
			boUtil.setMsg("Group id cannot be empty");
			return boUtil;
		}
		// 判断分组是否存在
		boolean groupExist = friendGroupService.isGroupExist(userId, groupId);
		if (!groupExist) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_NOT_EXISTS);
			boUtil.setMsg("Group does not exist");
			return boUtil;
		}
		// 删除用户与好友分组的关系(redis)
		friendGroupService.deleteGroup(userId, groupId);
		return boUtil;

	}

	/**
	 * 
	 * @Title: saveToGroup
	 * @param:
	 * @Description: 添加成员
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "member/add", notes = "member/add")
	@ResponseBody
	@RequestMapping(value = "member/add", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil saveToGroup(final @RequestBody FriendGroupVo vo) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long userId = super.getLoginUserID();
		if (vo.getGroupId() <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_EMPTY_ERROR);
			boUtil.setMsg("Group id cannot be empty");
			return boUtil;
		}
		if (vo.getFriendsId() == null || vo.getFriendsId().length == 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.USERID_IS_ERROR);
			boUtil.setMsg("friendsId is empty");
			return boUtil;
		}
		// 判断群是否存在
		boolean groupExist = friendGroupService.isGroupExist(userId,
				vo.getGroupId());
		if (!groupExist) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_NOT_EXISTS);
			boUtil.setMsg("Group does not exist");
			return boUtil;
		}
		// 查找没有加入分组的好友
		List<Long> notInCurrentGroupFriends = friendGroupService
				.findNotInCurrentGroupFriendIds(userId, vo.getGroupId());
		boolean flag = notInCurrentGroupFriends.containsAll(Arrays.asList(vo
				.getFriendsId()));
		if (!flag) {// 是否都在未加入分组好友里
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.NOT_IN_NON_CURRENT_GROUP_ERROR);
			boUtil.setMsg("There are already friends in this group");
			return boUtil;
		}
		// 将成员添加到好友分组
		friendGroupService.addFriendsToGroup(userId, vo.getGroupId(),
				vo.getFriendsId());
		return boUtil;
	}

	/**
	 * 
	 * @Title: removeFromGroup
	 * @param:
	 * @Description: 删除成员
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "member/remove", notes = "member/remove")
	@ResponseBody
	@RequestMapping(value = "member/remove", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil removeFromGroup(final @RequestBody FriendGroupVo vo) {
		long userId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		if (vo.getGroupId() <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_EMPTY_ERROR);
			boUtil.setMsg("Group id cannot be empty");
			return boUtil;
		}
		if (vo.getFriendsId() == null || vo.getFriendsId().length == 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.USERID_IS_ERROR);
			boUtil.setMsg("friendsId is empty");
			return boUtil;
		}
		// 判断群是否存在
		boolean groupExist = friendGroupService.isGroupExist(userId,
				vo.getGroupId());
		if (!groupExist) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_NOT_EXISTS);
			boUtil.setMsg("Group does not exist");
			return boUtil;
		}
		// 查找分组的好友
		List<Long> groupFriends = friendGroupService.findGroupFriends(userId,
				vo.getGroupId());
		boolean flag = groupFriends
				.containsAll(Arrays.asList(vo.getFriendsId()));
		if (!flag) {// 是否都在分组好友里
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.IN_NON_CURRENT_GROUP_ERROR);
			boUtil.setMsg("No friends of this group exist");
			return boUtil;
		}
		// 解除用户与好友分组的关系
		friendGroupService.removeFriendsFromGroup(userId, vo.getGroupId(),
				vo.getFriendsId());
		return boUtil;
	}

	/**
	 * 
	 * @Title: findNotInCurrentGroupFriends
	 * @param:
	 * @Description: 查询没有添加到此分组的好友，即未加组好友
	 * @return ListBoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "other/friends", notes = "other/friends")
	@ResponseBody
	@RequestMapping(value = "other/friends", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/*")
	public BoUtil findNotInCurrentGroupFriends(
			@QueryParam("groupId") Long groupId) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long userId = super.getLoginUserID();
		if (groupId == null || groupId.longValue() <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_EMPTY_ERROR);
			boUtil.setMsg("Group id cannot be empty");
			return boUtil;
		}
		// 判断群是否存在
		boolean groupExist = friendGroupService.isGroupExist(userId, groupId);
		if (!groupExist) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_NOT_EXISTS);
			boUtil.setMsg("Group does not exist");
			return boUtil;
		}
		// 查询没有添加到此群的好友
		List<FriendBo> list = friendGroupService.findNotInCurrentGroupFriends(
				userId, groupId);
		boUtil.setData(list);
		return boUtil;
	}

	/**
	 * 
	 * @Title: findGroupFriends
	 * @param:
	 * @Description: 查看组好友列表
	 * @return ListBoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "friends", notes = "friends")
	@ResponseBody
	@RequestMapping(value = "friends", method = RequestMethod.DELETE, produces = "application/json", consumes = "application/*")
	public BoUtil findGroupFriends(@QueryParam("groupId") Long groupId) {
		long userId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		if (groupId == null || groupId.longValue() <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_EMPTY_ERROR);
			boUtil.setMsg("Group id cannot be empty");
			return boUtil;
		}
		// 判断分组是否存在
		boolean groupExist = friendGroupService.isGroupExist(userId, groupId);
		if (!groupExist) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_NOT_EXISTS);
			boUtil.setMsg("Group does not exist");
			return boUtil;
		}
		List<FriendBo> list = friendGroupService.getGroupFriends(userId,
				groupId);
		boUtil.setData(list);
		return boUtil;
	}

	/**
	 * 
	 * @Title: findFriendGroups
	 * @param:
	 * @Description: 查询好友已添加的组
	 * @return ListBoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "groups", notes = "groups")
	@ResponseBody
	@RequestMapping(value = "groups", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil findFriendGroups(final @PathParam("friendId") Long friendId) {
		long userId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		if (friendId == null || friendId.longValue() == 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.USERID_IS_ERROR);
			boUtil.setMsg("friendsId is empty");
			return boUtil;
		}
		// 查询
		List<FriendGroupBo> list = friendGroupService.findFriendsInGroups(
				userId, friendId);
		// 数据返回
		boUtil.setData(list);
		return boUtil;
	}

	/**
	 * 
	 * @Title: saveToMultipleGroup
	 * @param:
	 * @Description: 添加好友到多个组
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "friends/multiple", notes = "friends/multiple")
	@ResponseBody
	@RequestMapping(value = "friends/multiple", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil saveToMultipleGroup(final @RequestBody FriendGroupVo payload) {
		long userId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		if (payload.getFriendId() == null || payload.getFriendId() == 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.USERID_IS_ERROR);
			boUtil.setMsg("friendsId is empty");
			return boUtil;
		}
		if (payload.getGroupsId() == null || payload.getGroupsId().length <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.FRIENDS_GROUPID_EMPTY_ERROR);
			boUtil.setMsg("Group id cannot be empty");
			return boUtil;
		}
		// 验证是否好友
		boolean friendFlag = friendGroupService.isFriends(userId,
				payload.getFriendId());
		if (!friendFlag) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.NOT_A_FRIEND_ERROR);
			boUtil.setMsg("Not a friend");
			return boUtil;
		}
		// 验证分组是否存在
		for (Long groupId : payload.getGroupsId()) {
			boolean isExit = friendGroupService.isGroupExist(userId, groupId);
			if (!isExit) {
				boUtil = BoUtil.getDefaultFalseBo();
				boUtil.setCode(ErrorCode.FRIENDS_GROUPID_NOT_EXISTS);
				boUtil.setMsg("Group does not exist");
				return boUtil;
			}
		}
		// 将好友添加到多个组
		friendGroupService.addFriendsToMultipleGroup(userId,
				payload.getGroupsId(), payload.getFriendId());
		return boUtil;
	}
}
