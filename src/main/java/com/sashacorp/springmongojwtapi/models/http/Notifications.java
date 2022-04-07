package com.sashacorp.springmongojwtapi.models.http;

public enum Notifications {
	POST_USER("POST", "USER"), PUT_ADMIN("PUT", "ADMIN"), DELETE_ADMIN("DELETE", "ADMIN"), DELETE_USER("DELETE", "USER");

	private final String type;
	private final String origin;
	
	Notifications(String type, String origin) {
		this.type = type;
		this.origin = origin;
	}

	public String getType() {
		return type;
	}

	public String getOrigin() {
		return origin;
	}
	
	public String text() {
		return this.name();
	}
}
