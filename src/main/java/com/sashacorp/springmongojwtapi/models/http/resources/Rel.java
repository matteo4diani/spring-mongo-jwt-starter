package com.sashacorp.springmongojwtapi.models.http.resources;

import java.util.HashMap;
import java.util.Map;

public class Rel {
	public static Map<String, String> adminUserResources;
	public static Map<String, String> userResources;
	public static Map<String, String> adminMessageResources;
	public static Map<String, String> messageResources;
	
	static {
		userResources = new HashMap<>();
		userResources.put("_self", Url.ME);
		userResources.put("_messages", Url.MY_MESSAGES);
		
		adminUserResources = new HashMap<>();
		adminUserResources.put("_adminself", Url.USER_BY_USERNAME);
		adminUserResources.put("_adminmessages", Url.MESSAGES_BY_USERNAME);
		adminUserResources.put("_authorities", Url.USER_AUTHORITIES_BY_USERNAME);
		adminUserResources.put("_status", Url.USER_STATUS_BY_USERNAME);
	
		messageResources = new HashMap<>();
		messageResources.put("_self", Url.MY_MESSAGE_BY_MESSAGE_ID);
		messageResources.put("_user", Url.ME);
		
		adminMessageResources = new HashMap<>();
		adminMessageResources.put("_requirer", Url.USER_BY_USERNAME);
		adminMessageResources.put("_responder", Url.RESPONDER_BY_USERNAME);
		adminMessageResources.put("_usermessages", Url.MESSAGES_BY_USERNAME);
		
	}
}
