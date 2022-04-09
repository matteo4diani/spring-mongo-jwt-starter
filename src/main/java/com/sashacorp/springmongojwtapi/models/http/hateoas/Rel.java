package com.sashacorp.springmongojwtapi.models.http.hateoas;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;

public class Rel {
	public static Map<String, String> adminUserResources;
	public static Map<String, String> userResources;
	public static Map<String, String> adminMessageResources;
	public static Map<String, String> messageResources;
	
	
	static {
		userResources = new ConcurrentHashMap<>();
		userResources.put("_self", Url.ME);
		userResources.put("_messages", Url.MY_MESSAGES);
		
		adminUserResources = new ConcurrentHashMap<>();
		adminUserResources.put("_hr_self", Url.USER_BY_USERNAME);
		adminUserResources.put("_hr_messages", Url.MESSAGES_BY_USERNAME);
		adminUserResources.put("_hr_authorities", Url.USER_AUTHORITIES_BY_USERNAME);
		adminUserResources.put("_hr_status", Url.USER_STATUS_BY_USERNAME);
	
		messageResources = new ConcurrentHashMap<>();
		messageResources.put("_self", Url.MY_MESSAGE_BY_MESSAGE_ID);
		messageResources.put("_user", Url.ME);
		
		adminMessageResources = new ConcurrentHashMap<>();
		adminMessageResources.put("_hr_self", Url.MESSAGE_BY_MESSAGE_ID);
		adminMessageResources.put("_hr_requester", Url.USER_BY_USERNAME);
		adminMessageResources.put("_hr_responder", Url.RESPONDER_BY_USERNAME);
		adminMessageResources.put("_hr_usermessages", Url.MESSAGES_BY_USERNAME);
		
	}
	
	/**
	 * Helper static class defined in {@link Rel}: all Urls from {@link Url} registered in {@link Rel} must be notated
	 * with a mimeType to comply with hateoas.
	 * @author matteo
	 *
	 */
	public static class MimeUtil {
		public static Map<String, MimeTypes> mimeTypesMap;
		
		static {
			mimeTypesMap = new ConcurrentHashMap<>();
			mimeTypesMap.put(Url.ME, MimeTypes.USER);
			mimeTypesMap.put(Url.MY_MESSAGES, MimeTypes.MESSAGE);
			mimeTypesMap.put(Url.MY_MESSAGE_BY_MESSAGE_ID, MimeTypes.MESSAGE);
			mimeTypesMap.put(Url.MESSAGE_BY_MESSAGE_ID, MimeTypes.MESSAGE);
			mimeTypesMap.put(Url.USER_BY_USERNAME, MimeTypes.USER);
			mimeTypesMap.put(Url.RESPONDER_BY_USERNAME, MimeTypes.USER);
			mimeTypesMap.put(Url.MESSAGES_BY_USERNAME, MimeTypes.MESSAGE);
			mimeTypesMap.put(Url.USER_AUTHORITIES_BY_USERNAME, MimeTypes.AUTHORITY);
			mimeTypesMap.put(Url.USER_STATUS_BY_USERNAME, MimeTypes.STATUS);
		}
		
		public static String mime(String uri) {
			return mimeTypesMap.containsKey(uri) ? mimeTypesMap.get(uri).mime() : MimeTypes.JSON.mime();
		}
	}
	public static class ResourceUtil {
		public static Map<MimeTypes, Map<Authority, Map<String, String>>> resourceMap;
		
		static {
			resourceMap = new ConcurrentHashMap<>();
			
			Map<Authority, Map<String, String>> userTypeMap = new ConcurrentHashMap<>();
			userTypeMap.put(Authority.USER, Rel.userResources);
			userTypeMap.put(Authority.HR, Rel.adminUserResources);
			resourceMap.put(MimeTypes.USER, userTypeMap);
			
			Map<Authority, Map<String, String>> messageTypeMap = new ConcurrentHashMap<>();
			messageTypeMap.put(Authority.USER, Rel.messageResources);
			messageTypeMap.put(Authority.HR, Rel.adminMessageResources);
			resourceMap.put(MimeTypes.MESSAGE, messageTypeMap);
			
		}
		
		public static Map<String, String> get(MimeTypes type, Authority authority) {
			return resourceMap.get(type).get(authority);
		}
		
		
	}
}
