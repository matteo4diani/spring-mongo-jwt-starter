package com.sashacorp.springmongojwtapi.models.persistence.user;

/**
 * Represents a {@link User}'s authority. Order of definition implies hierarchy
 * (e.g. ADMIN > MANAGER > HR > USER > GUEST).
 * Authority instances expose useful methods ({@code canGrantAuthority(Authority)} and {@code canEditAuthoritiesOf(Authority)})
 * to compare authorities and evaluate permissions
 * 
 * @author matteo
 *
 */
public enum Authority {

	ADMIN, MANAGER, HR, USER, GUEST;

	public String getAuthority() {
		return this.name();
	}
	
	/**
	 * 
	 * @return the {@link Authority}'s ordinal. The lower the number, the higher the authority level (e.g. 0 is admin, 1 is manager, 2 is hr, etc.)
	 */
	public int getAuthorityLevel() {
		return this.ordinal();
	}


	/**
	 * Deserializes {@link Authority} for {@link User} registration phase. See
	 * {@link SecurityController}.
	 * 
	 * @param authority
	 * @return the {@link Authority} that matches the given string or the baseline authority {@code GUEST}
	 */
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