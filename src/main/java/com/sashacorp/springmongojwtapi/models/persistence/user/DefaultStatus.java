package com.sashacorp.springmongojwtapi.models.persistence.user;

/**
 * A collection of default statuses, corresponding to default event types
 * @author matteo
 *
 */
public enum DefaultStatus {
	ON("ON", "Available"), LONG("LONG", "Daily leave"), SHORT("SHORT", "Hourly leave"), INCONSISTENT("INCONSISTENT", "Inconsistent");

	private final String type;
	private final String description;
	
	DefaultStatus(String type, String description) {
		this.type = type;
		this.description = description;
	}
	
	public String type() {
		return type;
	}

	public String description() {
		return description;
	}
	
	
	
	
}
