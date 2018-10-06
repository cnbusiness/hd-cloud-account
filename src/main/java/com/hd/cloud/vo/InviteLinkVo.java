package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InviteLinkVo {
	//群组Id
	private long roomId;
	//邀请的KEY
	private String key;  
	 
}
