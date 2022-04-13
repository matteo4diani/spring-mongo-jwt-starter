package com.sashacorp.springmongojwtapi.util.http.hateoas;

import com.sashacorp.springmongojwtapi.models.persistence.message.EventType;
import com.sashacorp.springmongojwtapi.models.persistence.message.Message;
import com.sashacorp.springmongojwtapi.models.persistence.user.Status;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.security.Authority;

/**
 * Defines and represents custom MIME types for our domain
 * @author matteo
 *
 */
public enum MimeType {
	USER("application/vnd.sashacorp.user.v1+json", User.class.getClass()), 
	MESSAGE("application/vnd.sashacorp.message.v1+json", Message.class.getClass()), 
	AUTHORITY("application/vnd.sashacorp.authority.v1+json", Authority.class.getClass()), 
	STATUS("application/vnd.sashacorp.status.v1+json", Status.class.getClass()), 
	EVENT_TYPE("application/vnd.sashacorp.eventtype.v1+json", EventType.class.getClass()),
	JSON("application/json", null);
		
	private final String type;
	private final Class<?> clazz;
	
	MimeType(String type, Class<?> clazz) {
		this.type = type;
		this.clazz = clazz;
	}
	
	public String mime() {
		return type;
	}
	
	public Class<?> clazz() {
		return clazz;
	}
}
