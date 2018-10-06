package com.hd.cloud.dao;

import java.util.List;

import com.hd.cloud.bo.FriendBo;

/**
 * 
  * @ClassName: FriendDao
  * @Description: 好友
  * @author yaojie yao.jie@hadoop-tech.com
  * @Company hadoop-tech
  * @date 2018年4月12日 上午11:57:50
  *
 */
public interface FriendDao {

  /**
   * 
  * @Title: findAllFriends 
  * @param: 
  * @Description: 批量查询
  * @return List<FriendBo>
   */
  List<FriendBo> findAllFriends(List<Long> ids);

  /**
   * 
  * @Title: findAllConditions 
  * @param: 
  * @Description: 搜索用户  昵称/id/手机号
  * @return List<FriendBo>
   */
  List<FriendBo> findAllConditions(String countryCode,String condition);

  /**
   * 
  * @Title: getUserPhone 
  * @param: 
  * @Description: 根据Id查询用户 
  * @return FriendBo
   */
  FriendBo getUserById(long userId);
  
  /**
   * 
  * @Title: findUsersByPhoneBook 
  * @param: 
  * @Description: 批量查询 
  * @return List<FriendBo>
   */
  List<FriendBo> findUsersByPhoneBook(List<String> phones);
  
}
