package com.hd.cloud.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.bo.FriendBo;
import com.hd.cloud.redis.bo.FansCount;
import com.hd.cloud.service.FriendService;
import com.hd.cloud.util.ConstantUtil;
import com.hd.cloud.util.ErrorCode;
import com.hd.cloud.vo.FriendVo;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.controller.RestBase;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: FriendResource
 * @Description: 好友
 * @author yaojie yao.jie@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月11日 上午9:56:48
 *
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/friend")
public class FriendResource extends RestBase {

	@Autowired
	private FriendService friendService;

	/**
	 * 
	 * @Title: followUser
	 * @param:
	 * @Description: 关注用户
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "follow", notes = "follow")
	@ResponseBody
	@RequestMapping(value = "follow", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil followUser(final @RequestBody FriendVo friendVo) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long userId = super.getLoginUserID();
		long targetId = friendVo.getUserId();
		log.info("userId={},targetId={}", userId, targetId);
		if (targetId <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.USERID_IS_ERROR);
			boUtil.setMsg("targetId is empty");
			return boUtil;
		}
		if (userId == targetId) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.NOT_ADD_YOURSELF_ERROR);
			boUtil.setMsg("Can't attention to yourself");
			return boUtil;
		}
		int relationship;
		// 判断是否已关注
		if (friendService.isFollowing(userId, targetId)) {
			// 获取关注状态
			relationship = this.friendService.getRelationship(userId, targetId);
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setData(relationship);
			boUtil.setCode(ErrorCode.USER_HAD_FOLLOWING);
			boUtil.setMsg("User has been following");
			return boUtil;
		}
		friendService.attention(userId, targetId);
		relationship = friendService.getRelationship(userId, targetId);
		boUtil.setData(relationship);
		return boUtil;
	}

	/**
	 * 
	 * @Title: removeFans
	 * @param:
	 * @Description: 取消关注
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "unfollow", notes = "unfollow")
	@ResponseBody
	@RequestMapping(value = "unfollow", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil unFollowUser(final @RequestBody FriendVo friendVo) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long userId = super.getLoginUserID();
		long targetId = friendVo.getUserId();
		log.info("userId={},targetId={}", userId, targetId);
		if (targetId <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.USERID_IS_ERROR);
			boUtil.setMsg("targetId is empty");
			return boUtil;
		}
		if (userId == targetId) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.NOT_ADD_YOURSELF_ERROR);
			boUtil.setMsg("Can't attention to yourself");
			return boUtil;
		}
		int relationship;
		// 判断是否已关注
		if (!friendService.isFollowing(userId, targetId)) {
			// 获取关注状态
			relationship = this.friendService.getRelationship(userId, targetId);
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setData(relationship);
			boUtil.setCode(ErrorCode.NOT_FOLLOWING_USER);
			boUtil.setMsg("Not following this user");
			return boUtil;
		}
		friendService.unAttention(userId, targetId);
		// 查询关系
		relationship = friendService.getRelationship(userId, targetId);
		boUtil.setData(relationship);
		return boUtil;
	}

	/**
	 * 
	 * @Title: getAllFriends
	 * @param:
	 * @Description: 获取好友列表
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "all", notes = "all")
	@ResponseBody
	@RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil getAllFriends() {
		long userId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		List<FriendBo> list = friendService.findAllFriends(userId, ConstantUtil.PageAll.PAGE_ALL,
				ConstantUtil.PageAll.SIZ_ALL);
		FansCount fansCount = friendService.getFansCount(userId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("total", fansCount.getFriendsCnt().intValue());
		boUtil.setData(map);
		return boUtil;
	}

	/**
	 * 
	 * @Title: findUserFollowing
	 * @param:
	 * @Description: 用户关注人列表
	 * @return ListBoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "following", notes = "following")
	@ResponseBody
	@RequestMapping(value = "following", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil getFollowingUsers(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
		long userId = super.getLoginUserID();
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		List<FriendBo> list = friendService.findAllFollowing(userId, page, size);
		FansCount fansCount = friendService.getFansCount(userId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("total", fansCount.getFollowingCnt().intValue());
		boUtil.setData(map);
		return boUtil;
	}

	/**
	 * 
	 * @Title: getFollowerUsers
	 * @param:
	 * @Description: 获取粉丝列表
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "follower", notes = "follower")
	@ResponseBody
	@RequestMapping(value = "follower", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil getFollowerUsers(@QueryParam("page") Integer page, @QueryParam("size") Integer size) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long userId = super.getLoginUserID();
		List<FriendBo> list = friendService.findAllFollower(userId, page, size);
		FansCount fansCount = friendService.getFansCount(userId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("total", fansCount.getFollowersCnt().intValue());
		boUtil.setData(map);
		return boUtil;
	}

	/**
	 * 
	 * @Title: saveRemark
	 * @param:
	 * @Description: 修改备注
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "remark", notes = "remark")
	@ResponseBody
	@RequestMapping(value = "remark", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
	public BoUtil saveRemark(final @Valid FriendVo vo) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long targetId = vo.getUserId();
		long userId = super.getLoginUserID();
		int relationship;
		if (targetId <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.USERID_IS_ERROR);
			boUtil.setMsg("targetId is empty");
			return boUtil;
		}
		// 判断是否已关注
		if (!friendService.isFollowing(userId, targetId)) {
			// 获取关注状态
			relationship = this.friendService.getRelationship(userId, targetId);
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setData(relationship);
			boUtil.setCode(ErrorCode.NOT_FOLLOWING_USER);
			boUtil.setMsg("Not following this user");
			return boUtil;
		}
		friendService.saveOrUpdateRemark(userId, vo);
		return boUtil;
	}

	/**
	 * 
	 * @Title: getCount
	 * @param:
	 * @Description: 粉丝数统计
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "count", notes = "count")
	@ResponseBody
	@RequestMapping(value = "count", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil getCount() {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long userId = super.getLoginUserID();
		FansCount fansCount = friendService.getFansCount(userId);
		boUtil.setData(fansCount);
		return boUtil;
	}

	/**
	 * 
	 * @Title: getRelationship
	 * @param:
	 * @Description: 关系查询
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "relationship", notes = "relationship")
	@ResponseBody
	@RequestMapping(value = "relationship", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil getRelationship(@QueryParam("targetId") Long targetId) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long userId = super.getLoginUserID();
		int relationship = friendService.getRelationship(userId, targetId);
		boUtil.setData(relationship);
		return boUtil;
	}

	/**
	 * 
	 * @Title: getUserByPhoneBook
	 * @param:
	 * @Description: 根据电话查询用户，即通讯录搜索
	 * @return ListBoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "phones", notes = "phones")
	@ResponseBody
	@RequestMapping(value = "phones", method = RequestMethod.PUT, produces = "application/json", consumes = "application/*")
	public BoUtil getUserByPhoneBook(final @Valid FriendVo vo) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long userId = super.getLoginUserID();
		List<FriendBo> list = friendService.findUserByPhoneBook(vo.getPhones(), userId);
		boUtil.setData(list);
		return boUtil;
	}

	/**
	 * 
	 * @Title: getConditions
	 * @param:
	 * @Description: 搜索用户 昵称/id/手机号
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "search", notes = "search")
	@ResponseBody
	@RequestMapping(value = "search", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public BoUtil getConditions(@NotNull @QueryParam("condition") String condition,
			final @NotNull @QueryParam("countryCode") String countryCode) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long userId = super.getLoginUserID();
		log.info("userId={},condition={}", userId, condition);
		// 去掉查询条件的空格
		List<FriendBo> list = friendService.findAllConditions(countryCode, condition);
		boUtil.setData(list);
		return boUtil;
	}

	/**
	 * 
	 * @Title: getAllFriendsByUserId
	 * @param:
	 * @Description: 获取用户好友列表(提供其他接口调用)
	 * @return BoUtil
	 */
	@ApiOperation(httpMethod = "GET", value = "all", notes = "all")
	@ResponseBody
	@RequestMapping(value = "all/{userId}", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public List<FriendBo> getAllFriendsByUserId(@PathVariable("userId") long userId) {
		return friendService.findAllFriends(userId, ConstantUtil.PageAll.PAGE_ALL, ConstantUtil.PageAll.SIZ_ALL);
	}
	
	/**
	 * 
	* @Title: addFriend 
	* @param: 
	* @Description: 添加好友
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "POST", value = "add", notes = "add")
	@ResponseBody
	@RequestMapping(value = "add", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil addFriend(final @RequestBody FriendVo friendVo) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long targetId = super.getLoginUserID();
		long userId = friendVo.getUserId();
		log.info("userId={},targetId={}", userId, targetId);
		if (targetId <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.USERID_IS_ERROR);
			boUtil.setMsg("targetId is empty");
			return boUtil;
		}
		if (userId == targetId) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.NOT_ADD_YOURSELF_ERROR);
			boUtil.setMsg("Can't add to yourself");
			return boUtil;
		}
		friendService.addFriend(userId, targetId);
		return boUtil;
	}
	
	/**
	 * 
	* @Title: deleteFriend 
	* @param: 
	* @Description: 删除好友
	* @return BoUtil
	 */
	@ApiOperation(httpMethod = "PUT", value = "remove", notes = "remove")
	@ResponseBody
	@RequestMapping(value = "remove", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public BoUtil deleteFriend(final @RequestBody FriendVo friendVo) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		long targetId = super.getLoginUserID();
		long userId = friendVo.getUserId();
		log.info("userId={},targetId={}", userId, targetId);
		if (targetId <= 0) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.USERID_IS_ERROR);
			boUtil.setMsg("targetId is empty");
			return boUtil;
		}
		if (userId == targetId) {
			boUtil = BoUtil.getDefaultFalseBo();
			boUtil.setCode(ErrorCode.NOT_ADD_YOURSELF_ERROR);
			boUtil.setMsg("Can't remove to yourself");
			return boUtil;
		}
		friendService.deleteFriend(userId, targetId);
		return boUtil;
	}
}
