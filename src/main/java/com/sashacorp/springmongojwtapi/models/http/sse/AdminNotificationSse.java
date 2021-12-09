package com.sashacorp.springmongojwtapi.models.http.sse;

import java.time.LocalDateTime;

import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.util.TimeUtil;

/**
 * Utility class representing an admin notification sent through SSE.
 * <br/>'type' should represent the http request method that spawned the notification.
 * @author matteo
 *
 */
public class AdminNotificationSse {
	
	private final String type;
	
	private final String requirerId;
	
	private final String requirerUsername;

	private final String messageId;
	
	private String adminId;
	
	private final Message message;
	
	private final LocalDateTime created;

	

	public AdminNotificationSse(String type, String requirerId, String requirerUsername, String messageId,
			Message message) {
		super();
		this.type = type;
		this.requirerId = requirerId;
		this.requirerUsername = requirerUsername;
		this.messageId = messageId;
		this.message = message;
		this.created = TimeUtil.now();
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public Message getMessage() {
		return message;
	}

	public String getType() {
		return type;
	}

	public String getUsername() {
		return requirerId;
	}

	public String getMessageId() {
		return messageId;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public String getRequirerId() {
		return requirerId;
	}

	public String getRequirerUsername() {
		return requirerUsername;
	}

	public String getAdminId() {
		return adminId;
	}
	
	
}
