package com.sashacorp.springmongojwtapi.models.persistence.user;

public enum Authority {
	
	ADMIN, MANAGER, HR, USER, GUEST;


	public String getAuthority() {
		return this.name();
	}

	public int getAuthorityLevel() {
		return this.ordinal();
	}
	
	public static Authority getAuthority(String authority) {
		switch (authority) {
		case "ADMIN":
			return Authority.ADMIN;
		case "MANAGER":
			return Authority.MANAGER;
		case "HR":
			return Authority.HR;
		case "USER":
			return Authority.USER;
		default:
			return Authority.GUEST;
		}
	}

}