package com.hd.cloud.vo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
 


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomInformationVo { 
	
	//群Id
	private long id;
	
	//群名称
	private String roomName;
	
	//群名称备注
	private int roomNameUpdated;

	//群公开，1公开0不公开
	private int publicType;
	
	//群验证，1允许任何人2需要身份验证3不允许任何人
	private int verifyType;
	
	//最大人数
	private int maxCnt;  
	
	//现有人数
	private int nowCnt; 
	
	//群头像
	private String photoUrl; 
	
	//群创建者
	private long creatorId; // owner Id
	 
	//群昵称
	private String nickName; 
	
	//用户角色,1群主2管理员3成员
	private int roleType; 
	
	//保存通讯簿，1是2否
	private int saveToContacts;
	
	//群二维码
	private String qrcodeUrl;
	
	@JsonIgnore 
	//创建时间 
	private Date createTimeInDate; 
	
	//群成员
	private List<ParticipantVo> participantList; 
	
	//
	private long createTime; 
	
	public long getCreateTime(){
		if(createTimeInDate != null){
			return createTimeInDate.getTime();
		}
		return 0;
	}

}
