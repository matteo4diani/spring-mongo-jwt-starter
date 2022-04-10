package com.sashacorp.springmongojwtapi.util.http.hateoas;

import com.sashacorp.springmongojwtapi.models.persistence.user.User;

/**
 * Functional interface defining an object that has a {@link User} owner.
 * In the case of a {@link User}, it could be the user themselves.
 * @author matteo
 *
 */
@FunctionalInterface
public interface Ownable {
	User getOwner();
}
