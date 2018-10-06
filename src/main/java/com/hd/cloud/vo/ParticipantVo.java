package com.hd.cloud.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantVo {

	private long userId;
	private int role = 1;  //用户角色,3群主2管理员 1成员
	private String mtalkDomain; 
	
	@JsonIgnore  
	//加入群的时间
	private Date joinDateInDate;
	//加入群的时间
	private long joinDate;
	//群昵称
	private String nickName;
	
	//private String nickNameInRoom;
	//头像
	private String photoUrl; 
	
	public long getJoinDate	(){
		if(joinDateInDate != null){
			return joinDateInDate.getTime();
		}
		return 0;
	}
}
