package com.sashacorp.springmongojwtapi.security;

import java.util.HashSet;
import java.util.Set;

import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;

/**
 * Utility class to centralize {@link Authority} policies.
 * 
 * @author matteo
 *
 */
public class AuthorityUtil {
	/**
	 * Utility method to build a Set<{@link Authority}> from a Set<String>, checking
	 * if the {@link Authority} {@code requesterAuthority} is high enough to grant
	 * the selected authorities
	 * 
	 * @param requesterAuthority
	 * @param stringAuthorities
	 * @return
	 */
	public static Set<Authority> buildAuthorities(Authority requesterAuthority, Set<String> stringAuthorities) {
		Set<Authority> authorities = new HashSet<>();

		if (stringAuthorities == null || requesterAuthority.equals(Authority.GUEST)) {
			Authority authorityToGrant = Authority.GUEST;
			authorities.add(authorityToGrant);
		} else {
			stringAuthorities.forEach(authority -> {
				Authority authorityToGrant = Authority.getAuthority(authority);
				if (canGrantAuthority(requesterAuthority, authorityToGrant)) {
					authorities.add(authorityToGrant);
				}
			});
		}
		return authorities;
	}

	/**
	 * Users and guests have no editing privilege. Admins (ADMIN, HR, MANAGER) can
	 * edit only their subjects' authorities, not their peers'.
	 * 
	 * @param authorityToGrant
	 * @return
	 */
	public static boolean canEditAuthoritiesOf(Authority requesterAuthority, Authority authorityToGrant) {
		if (requesterAuthority.equals(Authority.GUEST) || requesterAuthority.equals(Authority.USER))
			return false;

		if (requesterAuthority.getAuthorityLevel() < authorityToGrant.getAuthorityLevel()) {
			return true;
		}

		return false;
	}

	/**
	 * Users and guests have no editing privilege. Admins (ADMIN, HR, MANAGER) can
	 * only grant authorities lower or equal than their own
	 * 
	 * @param authorityToGrant
	 * @return
	 */
	public static boolean canGrantAuthority(Authority requesterAuthority, Authority authorityToGrant) {
		if (requesterAuthority.equals(Authority.GUEST) || requesterAuthority.equals(Authority.USER))
			return false;

		return requesterAuthority.getAuthorityLevel() <= authorityToGrant.getAuthorityLevel();
	}

}
