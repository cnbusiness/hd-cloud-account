package com.hd.cloud.redis.util;

/**
 * The type Redis key utils.
 */
public class RedisKey {

  /**
   * The PAL.
   */
  static final String PAL = "PAL:";
  /**
   * The FEED.
   */
  static final String FEED = "FEED:";
  
  /**
   * 粉丝数量排序列表
   */
  static final String MOSTFANS = "MOSTFANS:";
  
  /**
   * 用户详情
   */
  static final String USERPROFILE = "USERPROFILE";
  
  public static String mostFans(){
	  return MOSTFANS;
  }
  
  public static String userProfile(long userId){
	  return USERPROFILE + userId + ":INFO";
  }
  
  /**
   * The constant size.
   */

   /**
   * Globalgid string.
   *
   * @return the string
   */
  public static String globalgid() {
    return PAL + "GLOBAL:GID";
  }

  /**
   * Group string.
   *
   * @param uid the uid
   * @return the string
   */
  public static String group(Long uid) {
    return PAL + uid + ":GROUP";
  }

  /**
   * Friends string.
   *
   * @param uid the uid
   * @return the string
   */
  public static String friends(Long uid) {
    return PAL + uid + ":FRIENDS:";
  }

  /**
   * Group friends.
   *
   * @param uid the uid
   * @param gid the gid
   * @return the string
   */
  public static String groupFriends(Long uid, Long gid) {
    return PAL + uid + ":" + gid + ":FRIENDS";
  }

  /**
   * Remark string.
   *
   * @param uid the uid
   * @return the string
   */
  public static String remark(Long uid) {
    return PAL + uid + ":REMARK:";
  }

  /**
   * Notify string.
   *
   * @param uid the uid
   * @return the string
   */
  public static String notify(Long uid) {
    return PAL + uid + ":NOTIFY:";
  }

  /**
   * Group friends count.
   *
   * @param uid the uid
   * @return the string
   */
  public static String groupFriendsCount(Long uid) {
    return PAL + uid + ":GROUPCNT";
  }

  /**
   * Followers string.
   *
   * @param uid the uid
   * @return the string
   */
  public static String followers(Long uid) {
    return PAL + uid + ":FOLLOWERS:";
  }

  /**
   * Following string.
   *
   * @param uid the uid
   * @return the string
   */
  public static String following(Long uid) {

    return PAL + uid + ":FOLLOWING:";
  }

  /**
   * Merchant following.
   *
   * @param uid the uid
   * @return the string
   */
  public static String merchantFollowing(Long uid) {
    return PAL + uid + ":MFOLLOWING";
  }

  /**
   * Fans count.
   *
   * @param uid the uid
   * @return the string
   */
  public static String fansCount(Long uid) {
    return PAL + uid + ":FANSCNT";
  }

  /**
   * Blacklist string.
   *
   * @param uid the uid
   * @return the string
   */
  public static String blacklist(Long uid) {
    return PAL + uid + ":BLACKLIST";
  }

  //###################  moment keys   #####################

  /**
   * Timeline string.
   *
   * @param uid the uid
   * @return the string
   */
  public static String timeline(Long uid) {
    return PAL + uid + ":TIMELINE:";
  }

 /*
 TODO pal:1:mtimeline
 TODO pal:1:sendfeed

 */

  /**
   * Post count.
   *
   * @param pid the pid
   * @return the string
   */
  public static String postCount(Long pid) {
    return FEED + pid + ":CNT";
  }

  /**
   * Star string.
   *
   * @param pid the pid
   * @return the string
   */
  public static String star(Long pid) {
    return FEED + pid + ":STAR";
  }

  /**
   * Top string.
   *
   * @param uid the uid
   * @return the string
   */
  public static String top(Long uid) {
    return PAL + uid + ":TOP:";
  }


}