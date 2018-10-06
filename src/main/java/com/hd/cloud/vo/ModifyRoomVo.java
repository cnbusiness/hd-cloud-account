package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyRoomVo { 
	
	//群组id
	private long roomId;
	//群名称
	private String roomName;
	//群头像
	private String photoUrl;   
	//群主Id
	private String ownerId;
	

}
