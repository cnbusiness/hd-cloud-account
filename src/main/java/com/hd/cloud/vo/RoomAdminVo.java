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
public class RoomAdminVo { 
	
	//群组Id
	private long roomId;
	
	//添加管理员的人
	private List<RoomAdminDetailVo> addList;
	 
	private List <RoomAdminDetailVo>removeList;   
	

}
