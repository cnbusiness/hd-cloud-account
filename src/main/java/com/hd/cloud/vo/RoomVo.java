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
public class RoomVo {

	//群组名
	private String roomName;

	//群公开，1公开0不公开
	private int publicType;

	//最大人数
	private int maxCnt;

	// 群验证，1允许任何人 2需要身份验证 3不允许任何人
	private int verifyType; 

	//群头像
	private String photoUrl;

	private List<ParticipantVo> participantList;

}
