package com.sashacorp.springmongojwtapi.models.http.sse;

import java.time.LocalDateTime;

import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.util.TimeUtil;

/**
 * Utility class representing a user notification sent through SSE. <br/>
 * 'type' should represent the http request method that spawned the
 * notification.
 * 
 * @author matteo
 *
 */
public class UserNotificationSse {

	private final String type;

	/**
	 * User receiving notification
	 */
	private final String userId;

	private final String messageId;

	private final Message message;

	private final LocalDateTime created;

	/**
	 * 
	 * @param type
	 * @param userId    - User receiving notification
	 * @param messageId
	 * @param message
	 */
	public UserNotificationSse(String type, String userId, String messageId, Message message) {
		super();
		this.type = type;
		this.messageId = messageId;
		this.userId = userId;
		this.message = message;
		this.created = TimeUtil.now();
	}

	public String getType() {
		return type;
	}

	public String getMessageId() {
		return messageId;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public String getUserId() {
		return userId;
	}

	public Message getMessage() {
		return message;
	}

}
