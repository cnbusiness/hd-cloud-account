package com.hd.cloud.util;

public enum RoleEnum {
	
	
	OWNER(3, "Owner"),	
	ADMIN(2, "Admin"),
	MEMBER(1, "Member"),
	
	; 
	
	private int intValue;
	private String roleDescription;
	
	
	RoleEnum(int intValue, String roleDescription){
		this.intValue = intValue;
		this.roleDescription = roleDescription;
	}


	public int getIntValue() {
		return intValue;
	}


	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}


	public String getRoleDescription() {
		return roleDescription;
	}


	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	 

}
