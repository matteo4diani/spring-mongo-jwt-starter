package com.sashacorp.springmongojwtapi.models.http.sse;

import java.time.LocalDateTime;

import com.sashacorp.springmongojwtapi.controller.sse.AdminSseController;
import com.sashacorp.springmongojwtapi.models.persistence.message.Message;
import com.sashacorp.springmongojwtapi.util.TimeUtil;

/**
 * Utility class representing an admin notification sent through SSE. <br/>
 * 'type' should represent the http request method that spawned the
 * notification.
 * 
 * @author matteo
 *
 */
public class AdminNotificationSse {

	private final String type;

	private final String requesterId;

	private final String requesterUsername;

	private final String messageId;

	/**
	 * Admin sending the response (admin notifications are always sent to all, see
	 * {@link AdminSseController})
	 */
	private String adminId;

	private final Message message;

	private final LocalDateTime created;

	/**
	 * 
	 * @param type
	 * @param requesterId
	 * @param requesterUsername
	 * @param messageId
	 * @param message
	 */
	public AdminNotificationSse(String type, String requesterId, String requesterUsername, String messageId,
			Message message) {
		super();
		this.type = type;
		this.requesterId = requesterId;
		this.requesterUsername = requesterUsername;
		this.messageId = messageId;
		this.message = message;
		this.created = TimeUtil.now();
	}

	/**
	 * Set admin sending the response (admin notifications are always sent to all,
	 * see {@link AdminSseController})
	 * 
	 * @param adminId
	 */
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
		return requesterId;
	}

	public String getMessageId() {
		return messageId;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public String getRequesterId() {
		return requesterId;
	}

	public String getRequesterUsername() {
		return requesterUsername;
	}

	/**
	 * 
	 * Get admin sending the response (admin notifications are always sent to all,
	 * see {@link AdminSseController})
	 *
	 * @return
	 */
	public String getAdminId() {
		return adminId;
	}

}
