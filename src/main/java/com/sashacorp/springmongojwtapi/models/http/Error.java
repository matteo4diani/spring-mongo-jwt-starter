package com.sashacorp.springmongojwtapi.models.http;

/**
 * Utility enum to centralize custom errors
 * @author matteo
 *
 */
public enum Error {
	ERROR("Error: Something went wrong!"), AUTH_USERNAME_NOT_FOUND("Authentication Error: Username not found!"),
	AUTH_WRONG_CREDENTIALS("Authentication Error: Incorrect username or password!"),
	REG_USERNAME_EXISTS("Registration Error: Username already exists!");

	private final String text;

	Error(String text) {
		this.text = text;
	}

	public String text() {
		return text;
	}

}
