package com.hd.cloud.vo;
 

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;
 

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
	
	private String mtalkDomain;
	private String nickName; 
	
	private String userIcon;
	
	private int sexType;
	 
	

}
