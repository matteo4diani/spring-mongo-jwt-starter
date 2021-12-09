package com.sashacorp.springmongojwtapi.models.persistence.msg;

import java.time.LocalDateTime;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sashacorp.springmongojwtapi.controller.MeController;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;

/**
 * Represents a request for a generic leave. The message begins in {@link MeController} with 'pending'=true
 * and is approved or rejected by an ADMIN, MANAGER or HR via {@link MessageController}.
 * @author matteo
 *
 */
@Document(collection = "messages")
public class Message {
	
	@Id
	private String id;
	private String requestNote;
	private String responseNote;
	
	private LocalDateTime start;
	private LocalDateTime end;
	
	private Boolean pending;
	private LocalDateTime created;
	private LocalDateTime responded;
	private Boolean approved;
	
	
	/**
	 * 'true' if the message has been created, but not responded yet
	 * or if the user visits the me/messages/{messageId} mapping {@link MeController}
	 */
	private Boolean seen;
	
	private User requirer;
	private User responder;
	
	private String leave;
	
	public void setLeave(String leave) {
			this.leave = leave;
	}
	
	
	public Message() {
		super();
	}
	
	public Message(Message message) {
		super();
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRequestNote() {
		return requestNote;
	}
	public void setRequestNote(String requestNote) {
		this.requestNote = requestNote;
	}
	public String getResponseNote() {
		return responseNote;
	}
	public void setResponseNote(String responseNote) {
		this.responseNote = responseNote;
	}
	public LocalDateTime getStart() {
		return start;
	}
	public void setStart(LocalDateTime start) {
		this.start = start;
	}
	public LocalDateTime getEnd() {
		return end;
	}
	public void setEnd(LocalDateTime end) {
		this.end = end;
	}
	public LocalDateTime getCreated() {
		return created;
	}
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	public LocalDateTime getResponded() {
		return responded;
	}
	public void setResponded(LocalDateTime responded) {
		this.responded = responded;
	}
	public User getRequirer() {
		return requirer;
	}
	public void setRequirer(User requirer) {
		this.requirer = requirer;
	}
	public User getResponder() {
		return responder;
	}
	public void setResponder(User responder) {
		this.responder = responder;
	}
	public String getLeave() {
		return leave;
	}
	


	public Boolean isPending() {
		return pending;
	}


	public void setPending(Boolean pending) {
		this.pending = pending;
	}


	public Boolean isApproved() {
		return approved;
	}


	public void setApproved(Boolean approved) {
		this.approved = approved;
	}


	public Boolean getSeen() {
		return seen;
	}


	public void setSeen(Boolean seen) {
		this.seen = seen;
	}


	public Boolean getPending() {
		return pending;
	}


	public Boolean getApproved() {
		return approved;
	}
	
	/**
	 * Helper method to get requirer username with ::
	 */
	public String fetchRequirerUsernameIfPresent() {
		if(requirer != null) 
			return requirer.getUsername();
		else
			return "";
	}
	
	
	
	
	
	
}
