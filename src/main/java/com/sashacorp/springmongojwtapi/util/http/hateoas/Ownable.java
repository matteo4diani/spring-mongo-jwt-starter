package com.sashacorp.springmongojwtapi.util.http.hateoas;

import com.sashacorp.springmongojwtapi.models.persistence.user.User;

@FunctionalInterface
public interface Ownable {
	User getOwner();
}
