package com.hd.cloud.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder; 

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantListVo { 
	
	//群组id
	private long roomId;
	
	//是否需要邀请 1是
	private int needToInvite;
	
	private List<ParticipantVo> participantList;
}
