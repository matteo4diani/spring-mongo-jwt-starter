package com.sashacorp.springmongojwtapi.models.http.hateoas;

public class Url {
	
	/*
	 * Building blocks
	 */
	public static final String MESSAGES = "/messages";
	public static final String USERS = "/users";

	public static final String CURRENT = "/current";
	public static final String ONGOING = "/ongoing";
	public static final String OUTDATED = "/outdated";
	public static final String BETWEEN = "/between";
	
	public static final String ME = "/me";
	public static final String ALL = "/all";
	public static final String TEAM = "/team";
	
	public static final String AUTHORITIES = "/authorities";
	public static final String STATUS = "/status";
	
	public static final String BY_MESSAGE_ID = "/id/{messageId}";
	public static final String BY_USERNAME = "/{username}";
	public static final String BY_RESPONDER_USERNAME = "/{responderUsername}";
	public static final String BY_TEAM = "/{team}";
	
	/*
	 * Me Controller
	 */
	public static final String MY_MESSAGES = ME + MESSAGES;
	public static final String MY_CURRENT_MESSAGES = ME + MESSAGES + CURRENT;
	public static final String MY_OUTDATED_MESSAGES = ME + MESSAGES + OUTDATED;
	public static final String MY_ONGOING_MESSAGES = ME + MESSAGES + ONGOING;
	public static final String MY_MESSAGE_BY_MESSAGE_ID = ME + MESSAGES + BY_MESSAGE_ID;
	
	/*
	 * Message Controller
	 */
	public static final String CURRENT_MESSAGES = MESSAGES + CURRENT;
	public static final String OUTDATED_MESSAGES = MESSAGES + OUTDATED;
	public static final String ONGOING_MESSAGES = MESSAGES + ONGOING;
	public static final String MESSAGE_BY_MESSAGE_ID = MESSAGES + BY_MESSAGE_ID;
	public static final String MESSAGES_BY_USERNAME = MESSAGES + ALL + BY_USERNAME;
	public static final String CURRENT_MESSAGES_BY_USERNAME = MESSAGES + CURRENT + BY_USERNAME;
	public static final String OUTDATED_MESSAGES_BY_USERNAME = MESSAGES + OUTDATED + BY_USERNAME;
	public static final String ONGOING_MESSAGES_BY_USERNAME = MESSAGES + ONGOING + BY_USERNAME;
	public static final String MESSAGES_BETWEEN = MESSAGES + BETWEEN;
	public static final String MESSAGES_BETWEEN_BY_USERNAME = MESSAGES + BETWEEN + BY_USERNAME;
	public static final String MESSAGES_BETWEEN_BY_TEAM = MESSAGES + TEAM + BETWEEN + BY_TEAM;
	
	/*
	 * User Controller
	 */
	public static final String USER_BY_USERNAME = USERS + BY_USERNAME;
	public static final String RESPONDER_BY_USERNAME = USERS + BY_RESPONDER_USERNAME;
	public static final String USER_AUTHORITIES_BY_USERNAME = USERS + AUTHORITIES + BY_USERNAME;
	public static final String USER_STATUS_BY_USERNAME = USERS + STATUS + BY_USERNAME;

}
