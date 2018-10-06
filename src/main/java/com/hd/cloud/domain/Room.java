package com.hd.cloud.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;


/**
 * 
  * @ClassName: Room
  * @Description: 群组
  * @author yaojie yao.jie@hadoop-tech.com
  * @Company hadoop-tech
  * @date 2018年5月10日 下午2:37:15
  *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room { 

	//主键Id
	private Long id;
	
	//群名称
	private String roomName; 

	//群验证，1允许任何人2需要身份验证3不允许任何人
	private int verifyType; 
	
	//群公开，1公开0不公开
	private int publicType;
	 
	//群二维码
	private String qrcodeUrl;
	 
	//群头像
	private String photoUrl;
	 
	private long createBy;
	
	private Date createTimeInDate;
	
	//最大人数
	private int maxCnt;
	
	private long createTime; 
	
	public long getCreateTime(){
		if(createTimeInDate != null){
			return createTimeInDate.getTime();
		}
		return 0;
	}
 
	
}
