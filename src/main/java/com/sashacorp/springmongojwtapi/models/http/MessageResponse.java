package com.sashacorp.springmongojwtapi.models.http;

/**
 * Utility class representing a simple JSON text message response
 * @author matteo
 *
 */
public class MessageResponse {
	private String message;

	public MessageResponse(String message) {
	    this.message = message;
	  }

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

