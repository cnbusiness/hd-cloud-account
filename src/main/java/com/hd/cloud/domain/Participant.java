package com.hd.cloud.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;



/**
 * 
  * @ClassName: Participant
  * @Description: 群成员
  * @author yaojie yao.jie@hadoop-tech.com
  * @Company hadoop-tech
  * @date 2018年5月11日 上午10:56:48
  *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participant { 

	private Long id;

	private long userId;
  
	private Long roomId;
	 
    private Date joinDate;
    
    private int sexType;
    
    private int pubCityDict;
    
    private int roleType; 
    
    private String userIcon; 
    
    private int saveToContacts;
    
    private String nickName;
     
    
    private long createBy;
    
    private Date createTime; 
    
    private String mtalkDomain; 
}
