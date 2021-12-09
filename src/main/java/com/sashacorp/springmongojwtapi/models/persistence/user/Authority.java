package com.sashacorp.springmongojwtapi.models.persistence.user;

/**
 * Entity class representing a user's authority
 * @author matteo
 *
 */
public class Authority {
	
	private String authority;

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public Authority(String authority) {
		super();
		this.authority = authority;
	}

	public Authority() {
		super();
	}

	@Override
	public String toString() {
		return "Authority [authority=" + authority + "]";
	}
	
	

}