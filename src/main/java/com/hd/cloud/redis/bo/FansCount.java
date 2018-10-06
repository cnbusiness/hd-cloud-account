package com.hd.cloud.redis.bo;

import java.util.concurrent.atomic.AtomicLong;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
  * @ClassName: FansCount
  * @Description: 好友统计Bo
  * @author yaojie yao.jie@hadoop-tech.com
  * @Company hadoop-tech
  * @date 2018年4月11日 上午10:50:53
  *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FansCount {

	// 关注数
  private AtomicLong followingCnt;
  
  // 粉丝数
  private AtomicLong followersCnt;
  
  // 好友数
  private AtomicLong friendsCnt;

}
