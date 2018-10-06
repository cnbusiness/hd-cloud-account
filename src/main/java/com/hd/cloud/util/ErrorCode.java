package com.hd.cloud.util;

/**
 * 
 * @ClassName: ErrorCode
 * @Description: 错误码
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:27:10
 *
 */
public class ErrorCode {

	// 用户名和密码不能为空
	public final static String USERNAME_PASSWORD_EMPTY = "hd0010001";

	// 用户名或密码错误
	public final static String USERNAME_OR_PASSWORD_ERROR = "hd0010002";

	// 该电话未注册
	public final static String PHONE_NOT_REGISTERED = "hd0010003";

	// 请输入电话号码
	public final static String PHONE_EMPTY = "hd0010004";

	// userId不能为空
	public final static String USERID_IS_EMPTY = "hd0010005";

	// token不能为空
	public final static String TOKEN_IS_EMPTY = "hd0010006";

	// token验证失败
	public final static String TOKEN_FAIL = "hd0010007";

	/**
	 * 手机号码格式错误
	 */
	public final static String PHONE_FORMAT_ERROR = "hd0010008";

	/**
	 * 验证码为空
	 */
	public final static String CAPTCHA_EMPTY_ERROR = "hd0010009";

	/**
	 * 账号密码为空
	 */
	public final static String ACCOUNT_PWD_EMPTY_ERROR = "hd0010010";

	/**
	 * 密码不能为中文
	 */
	public final static String PASSWORD_IS_CHINESE_ERROR = "hd0010011";

	/**
	 * 密码不一致
	 */
	public final static String PWD_NOT_SAME_ERROR = "hd0010012";

	/**
	 * 验证码不匹配
	 */
	public final static String CAPTCHA_NOT_MATCH_ERROR = "hd0010013";

	/**
	 * 验证码已过期
	 */
	public final static String SEND_VALID_TIME_ERROR = "hd0010014";

	/**
	 * 账号不存在，未注册
	 */
	public final static String ACCOUNT_IS_NOT_EXIST = "hd0010015";

	/**
	 * 该账号已经注册啦~
	 */
	public final static String REGISTER_ERROR = "hd0010016";

	/**
	 * 账号注册失败
	 */
	public final static String ACCOUN_REGISTED_ERROR = "hd0010017";

	/**
	 * 发送验证码错误
	 */
	public final static String SEND_REG_CAPTHCA_ERROR = "hd0010018";

	/**
	 * 每天发送限制
	 */
	public final static String SEND_DAY_LIMIT_ERROR = "hd0010019";

	/**
	 * 一分钟内已经发送过了
	 */
	public final static String SEND_ONE_MINUTE_LIMIT_ERROR = "hd0010020";

	/**
	 * 该手机号码已经绑定
	 */
	public final static String PHONE_IS_BINDING = "hd0010021";

	/**
	 * 昵称为空
	 */
	public final static String NICKNAME_EMPTY_ERROR = "hd0010022";

	/**
	 * 昵称字数超过上限了哦～
	 */
	public final static String NICKNAME_LENGTH_ERROR = "hd0010023";
	/**
	 * userId为空错误
	 */
	public final static String USERID_IS_ERROR = "hd0010024";

	/**
	 * 已关注该用户
	 */
	public final static String USER_HAD_FOLLOWING = "hd0010025";

	/**
	 * 不能关注/取消关注自己
	 */
	public final static String NOT_ADD_YOURSELF_ERROR = "hd0010026";

	/**
	 * 没有关注该用户
	 */
	public final static String NOT_FOLLOWING_USER = "hd0010027";

	/**
	 * 好友分组已存在
	 */
	public final static String FRIENDS_GROUP_EXISTS_ERROR = "hd0010028";

	/**
	 * 好友分组名不能为空
	 */
	public final static String FRIENDS_GROUP_EMPTY_ERROR = "hd0010029";

	/**
	 * 好友分组Id不能为空
	 */
	public final static String FRIENDS_GROUPID_EMPTY_ERROR = "hd0010030";

	/**
	 * 好友分组不存在
	 */
	public final static String FRIENDS_GROUPID_NOT_EXISTS = "hd0010031";

	/**
	 * 存在该分组的好友
	 */
	public final static String NOT_IN_NON_CURRENT_GROUP_ERROR = "hd0010032";

	/**
	 * 不存在该分组的好友
	 */
	public final static String IN_NON_CURRENT_GROUP_ERROR = "hd0010033";

	/**
	 * 不是好友
	 */
	public final static String NOT_A_FRIEND_ERROR = "hd0010034";

	/**
	 * 请输入支付密码
	 */
	public final static String PAYMENT_PASSWORD_EMPTY = "hd0010035";

	/**
	 * 修改密码失败
	 */
	public final static String UPDATE_PASSWORDA_FAIL = "hd0010036";

	/**
	 * 请输入登录密码
	 */
	public final static String OLD_PASSWORD_EMPTY = "hd0010037";

	/**
	 * 请输入新密码
	 */
	public final static String NEW_PASSWORD_EMPTY = "hd0010038";

	/**
	 * 请输入确认密码
	 */
	public final static String CONFIRM_PASSWORD_EMPTY = "hd0010039";

	/**
	 * 旧密码错误
	 */
	public final static String OLD_PASSWORD_ERROR = "hd0010040";

	/* 最多创建100个群组 */
	public final static String LIMIT_TO_100 = "hd0010041";

	/* 房间至少有一个成员 */
	public final static String AT_LEAST_ONE_MEMBER = "hd0010042";

	/* 用户ID不正确 */
	public final static String INVALID_USER_ID = "hd0010043";

	/* 用户ID重复 */
	public final static String DUPLICATE_USER_ID = "hd0010044";

	/* 超过群组人数上限 */
	public final static String ROOM_MEMBER_EXCEED = "hd0010045";

	/* 用户不存在 */
	public final static String USER_NOT_EXIST = "hd0010046";

	/* 创建群组失败 */
	public final static String FAILED_TO_CREATE_ROOM = "hd0010047";

	/* 群组ID不正确（包括空） */
	public final static String ROOM_ID_INVALID = "hd0010048";

	/* 群二维码无效 */
	public final static String ROOM_QRCODE_INVALID = "hd0010049";

	/* 邀请群组链接无效 */
	public final static String INVITE_LINK_INVALID = "hd0010050";

	/* 群组不存在 */
	public final static String ROOM_NOT_EXIST = "hd0010051";

	/* 只有群组或管理员可以添加用户 */
	public final static String ONLY_OWNER_OR_ADMIN_CAN_ADD_MEMBER = "hd0010052";

	/* 至少增加一个成员 */
	public final static String AT_LEAST_ONE_MEMBER_WILL_BE_ADDED = "hd0010053";

	/* 成员已经存在群组里 */
	public final static String EXISTING_MEMBER_IN_ROOM = "hd0010054";

	/* 需要发送邀请链接 */
	public final static String NEED_TO_SEND_INVITATION = "hd0010055";

	/* 至少删除一个成员 */
	public final static String AT_LEAST_ONE_MEMBER_WILL_BE_REMOVED = "hd0010056";

	/* 用户不在这个群组 */
	public final static String USER_NOT_IN_THIS_ROOM = "hd0010057";

	/* 群成员需要超过一个人 */
	public final static String MIN_MEMBER_SIZE = "hd0010058";

	/* 只有群主才可以删除管理员 */
	public final static String ONLY_OWNER_CAN_REMOVE_ADMIN = "hd0010059";

	/* 只有群组或管理员可以删除用户 */
	public final static String ONLY_OWNER_OR_ADMIN_CAN_REMOVE_MEMBER = "hd0010060";

	/* 只有群主才能换群主 */
	public final static String ONLY_OWNER_CAN_CHANGE_OWNER = "hd0010061";

	/* 只有群主或管理员才能修改群属性 */
	public final static String ONLY_OWNER_OR_ADMIN_CAN_CHANGE_GROUP_INF = "hd0010062";

	/* 没有修改 */
	public final static String NOTHING_GOT_CHANGED = "hd0010063";

	/* 只有群组可以解散群 */
	public final static String ONLY_OWNER_CAN_DESTORY_ROOM = "hd0010064";

	/* 只有群主或管理员可以设置管理员 */
	public final static String ONLY_OWNER_OR_ADMIN_CAN_SET_ADMIN = "hd0010065";

	/* 管理员不能超过五个 */
	public final static String ADMIN_CANNOT_MORE_THAN_FIVE = "hd0010066";

	/* 只有群主或管理员可以取消管理员 */
	public final static String ONLY_OWNER_OR_ADMIN_CAN_CANCEL_ADMIN = "hd0010067";

	/**
	 * 系统错误 升级vip信息未配置
	 */
	public final static String VIP_SYSTEM_ERROR = "hd0010068";

}
