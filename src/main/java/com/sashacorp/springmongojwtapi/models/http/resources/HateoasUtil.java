package com.sashacorp.springmongojwtapi.models.http.resources;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;

public class HateoasUtil {
	public static void setUserResources(User user) {
		if (user.hasEnoughAuthority(Authority.HR)) {
			Map<String, String> resources = Stream.of(Rel.adminUserResources, Rel.userResources)
					.flatMap(map -> map.entrySet().stream())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			user.setResources(resources);
		} else {
			user.setResources(Rel.userResources);
		}
	}
	public static void setMessageResources(Message message, User user) {
		boolean messageIsFromUser = message.getRequirer().getUsername().equals(user.getUsername());
		
		if (user.hasEnoughAuthority(Authority.HR) && messageIsFromUser) {
			
			Map<String, String> resources = Stream.of(Rel.adminMessageResources, Rel.messageResources)
					.flatMap(map -> map.entrySet().stream())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			message.setResources(resources);
		} else if (user.hasEnoughAuthority(Authority.HR)) {
			message.setResources(Rel.adminMessageResources);
		} else if (messageIsFromUser) {
			message.setResources(Rel.messageResources);			
		}
	}
}
