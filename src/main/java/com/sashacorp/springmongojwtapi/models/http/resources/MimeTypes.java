package com.sashacorp.springmongojwtapi.models.http.resources;

import com.sashacorp.springmongojwtapi.models.persistence.msg.Leave;
import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;
import com.sashacorp.springmongojwtapi.models.persistence.user.Status;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;

public enum MimeTypes {
	USER("application/vnd.sashacorp.user.v1+json", User.class.getClass()), 
	MESSAGE("application/vnd.sashacorp.message.v1+json", Message.class.getClass()), 
	AUTHORITY("application/vnd.sashacorp.authority.v1+json", Authority.class.getClass()), 
	STATUS("application/vnd.sashacorp.status.v1+json", Status.class.getClass()), 
	LEAVE("application/vnd.sashacorp.leave.v1+json", Leave.class.getClass()),
	JSON("application/json", null);
		
	private final String type;
	private final Class<?> clazz;
	
	MimeTypes(String type, Class<?> clazz) {
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
