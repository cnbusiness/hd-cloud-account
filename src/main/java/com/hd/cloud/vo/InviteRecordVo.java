package com.hd.cloud.vo;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
 


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteRecordVo {
	
	private long roomId;
	private long fromId; 
	private long toId;
	private String key;
	private Date inviteEndDate;
	
	
	private String userId;
	private String nickName; 
	private String mtalkDomain;
	private String userIcon; 
	 
	private String toUserId;
	private String toMtalkDomain;
	
}
