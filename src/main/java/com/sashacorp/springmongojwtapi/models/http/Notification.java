package com.sashacorp.springmongojwtapi.models.http;

/**
 * Utility enum to centralize custom notification types and structure
 * @author matteo
 *
 */
public enum Notification {
	POST_USER("POST", "USER"), PUT_ADMIN("PUT", "ADMIN"), DELETE_ADMIN("DELETE", "ADMIN"), DELETE_USER("DELETE", "USER");

	private final String type;
	private final String origin;
	
	Notification(String type, String origin) {
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
