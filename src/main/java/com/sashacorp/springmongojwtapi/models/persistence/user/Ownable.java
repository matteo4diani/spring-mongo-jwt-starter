package com.sashacorp.springmongojwtapi.models.persistence.user;

@FunctionalInterface
public interface Ownable {
	User getOwner();
}
