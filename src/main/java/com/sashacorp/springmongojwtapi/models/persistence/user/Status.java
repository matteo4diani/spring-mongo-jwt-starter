package com.sashacorp.springmongojwtapi.models.persistence.user;

/**
 * Entity class representing a user's status. If 'hardcoded' has been set to
 * <b>true</b> this user's status overrides any ongoing activities and must
 * always be displayed as is until further notice.
 * 
 * @author matteo
 *
 */
public class Status {

	public static final String INCONSISTENT = "INCONSISTENT";
	private String availability;
	private boolean hardcoded;

	public Status() {
		super();
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public boolean isHardcoded() {
		return hardcoded;
	}

	public void setHardcoded(boolean hardcoded) {
		this.hardcoded = hardcoded;
	}

	public Status(String availability, boolean hardcoded) {
		super();
		this.availability = availability;
		this.hardcoded = hardcoded;
	}

	@Override
	public String toString() {
		return "Status [availability=" + availability + ", hardcoded=" + hardcoded + "]";
	}
}
