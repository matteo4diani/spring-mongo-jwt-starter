package com.sashacorp.springmongojwtapi.util.http.hateoas;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;

/**
 * Utility class holding together: 
 * - {@link Url} Mappings for the user-facing controllers
 * - 
 * @author matteo
 *
 */
public class Rel {
	/*
	 * User relations
	 */
	private static Map<String, String> adminUserResources;
	private static Map<String, String> userResources;
	
	/*
	 * Message relations
	 */
	private static Map<String, String> adminMessageResources;
	private static Map<String, String> messageResources;
	
	/**
	 * Helper {@code enum} to represent entity relations as special JSON properties (hence the leading '_')
	 * Exposes a {@code public static String join(Slug...slugs)} method to join slugs and compose complex properties.
	 * @author matteo
	 *
	 */
	private enum Slug {
		SELF("_self"), HR("_hr"), USER("_user"),
		MESSAGES("_messages"), AUTHORITIES("_authorities"), 
		STATUS("_status"), REQUESTER("_requester"), RESPONDER("_responder");
		
		private final String rel;
		
		Slug(String rel) {
			this.rel = rel;
		}
		/**
		 * 
		 * @return the {@link Slug}s associated string representing a JSON property name (same as enum name, but lowercase with leading '_',)
		 * Example: {@code Slugs.SELF -> "_self"}
		 */
		public String rel() {
			return rel;
		}
		
		/**
		 * Joins together an array of {@link Slug}s
		 * @param slugs
		 * @return
		 */
		public static String join(Slug...slugs) {
			String[] slugsToJoin = Stream.of(slugs).map((slug) -> slug.rel()).toArray(String[]::new);
			return String.join("", slugsToJoin);
		}
	}
	
	static {
		userResources = new ConcurrentHashMap<>();
		userResources.put(Slug.SELF.rel(), Url.ME);
		userResources.put(Slug.MESSAGES.rel(), Url.MY_MESSAGES);
		
		adminUserResources = new ConcurrentHashMap<>();
		adminUserResources.put(Slug.join(Slug.HR, Slug.USER), Url.USER_BY_USERNAME);
		adminUserResources.put(Slug.join(Slug.HR, Slug.MESSAGES), Url.MESSAGES_BY_USERNAME);
		adminUserResources.put(Slug.join(Slug.HR, Slug.AUTHORITIES), Url.USER_AUTHORITIES_BY_USERNAME);
		adminUserResources.put(Slug.join(Slug.HR, Slug.STATUS), Url.USER_STATUS_BY_USERNAME);
	
		messageResources = new ConcurrentHashMap<>();
		messageResources.put(Slug.SELF.rel(), Url.MY_MESSAGE_BY_MESSAGE_ID);
		messageResources.put(Slug.USER.rel(), Url.ME);
		
		adminMessageResources = new ConcurrentHashMap<>();
		adminMessageResources.put(Slug.join(Slug.HR, Slug.SELF), Url.MESSAGE_BY_MESSAGE_ID);
		adminMessageResources.put(Slug.join(Slug.HR, Slug.REQUESTER), Url.USER_BY_USERNAME);
		adminMessageResources.put(Slug.join(Slug.HR, Slug.RESPONDER), Url.RESPONDER_BY_USERNAME);
		adminMessageResources.put(Slug.join(Slug.HR, Slug.USER, Slug.MESSAGES), Url.MESSAGES_BY_USERNAME);
		
	}
	
	/**
	 * Helper static class defined in {@link Rel}: all URLs from {@link Url} registered in {@link Rel} should be notated
	 * with a MIME type to comply with HATEOAS.
	 * Exposes a {@code public static String mime(String url)} method to get the MIME type string for entity {@link Url}s registered
	 * in {@link Rel}. 
	 * @author matteo
	 *
	 */
	public static class MimeUtil {
		private static Map<String, MimeTypes> mimeTypesMap;
		
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
		
		/**
		 * Get the MIME type string for {@link Url}s registered in {@link Rel}
		 * In accordance with REST,'application/json' is assumed by default if {@link Url} is not registered in {@code mimeTypesMap}
		 * @param uri
		 * @return
		 */
		public static String mime(String uri) {
			return mimeTypesMap.containsKey(uri) ? mimeTypesMap.get(uri).mime() : MimeTypes.JSON.mime();
		}
	}
	
	/**
	 * Helper static class defined in {@link Rel}.<br/>
	 * Associates a Map<{@link Slug}, {@link Url}> (both as strings) to the requested {@link MimeTypes} 
	 * and the {@link Authority} of the {@link User} making the request.
	 * @author matteo
	 *
	 */
	public static class ResourceUtil {
		private static Map<MimeTypes, Map<Authority, Map<String, String>>> resourceMap;
		
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
