package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantInfoVo {
	//群组id
	private long roomId;
	//是否保存通讯录
	private int saveToContacts; 
	//群昵称
	private String nickName;
	 
}
