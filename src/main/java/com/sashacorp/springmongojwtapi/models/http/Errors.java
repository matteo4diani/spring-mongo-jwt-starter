package com.sashacorp.springmongojwtapi.models.http;

public enum Errors {
	ERROR("Error: Something went wrong!"), USERNAME_NOT_FOUND("Authentication Error: Username not found!");
	
	private final String text;
	
	Errors(String text) {
		this.text = text;
	}

	public String text() {
		return text;
	}
	
	
}
