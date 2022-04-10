package com.sashacorp.springmongojwtapi.models.persistence.msg;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sashacorp.springmongojwtapi.controller.MeController;
import com.sashacorp.springmongojwtapi.controller.MessageController;
import com.sashacorp.springmongojwtapi.models.persistence.user.Ownable;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.util.http.hateoas.Hateoas;
import com.sashacorp.springmongojwtapi.util.http.hateoas.Links;

/**
 * Represents a request for a generic leave. The message begins in
 * {@link MeController} with 'pending'=true and is approved or rejected by an
 * ADMIN, MANAGER or HR via {@link MessageController}.
 * 
 * @author matteo
 *
 */
@Document(collection = "messages")
public class Message implements Hateoas, Ownable {

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
	 * 'true' if the message has been created, but not responded yet or if the user
	 * visits the me/messages/{messageId} mapping {@link MeController}
	 */
	private Boolean seen;

	private User requester;
	private User responder;

	private String leave;

	private Links _resources = null;

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

	public User getRequester() {
		return requester;
	}

	public void setRequester(User requester) {
		this.requester = requester;
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
	 * Helper method to get requester username with ::
	 */
	public String fetchRequesterUsernameIfPresent() {
		if (requester != null)
			return requester.getUsername();
		else
			return "";
	}

	@Override
	public Links get_resources() {
		return _resources;
	}

	@Override
	public void setResources(Map<String, String> uris) {
		this._resources = computeResources(uris);
	}

	@Override
	public String replacePathVariables(String url) {
		return url
				.replace(placeholder("messageId"), id)
				.replace(placeholder("username"), requester.getUsername())
				.replace(placeholder("responderUsername"), responder == null ? "?" : responder.getUsername());
	}
	@Override
	@JsonIgnore
	public User getOwner() {
		return requester;
	}

}
