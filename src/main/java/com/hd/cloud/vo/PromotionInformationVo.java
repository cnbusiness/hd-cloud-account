package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromotionInformationVo {
	
	private long userId;
	private long companyId;
	private String title;
	private String picUrl;
	private String description;
	private long productId;
	private int type;
	private String mopalIds;
	
	@Override
	public String toString() {
		return "PromotionInformationVo [userId=" + userId + ", companyId="
				+ companyId + ", title=" + title + ", picUrl=" + picUrl
				+ ", description=" + description + ", productId=" + productId
				+ ", type=" + type + ", mopalIds=" + mopalIds + "]";
	}
}
