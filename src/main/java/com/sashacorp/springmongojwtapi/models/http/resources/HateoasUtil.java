package com.sashacorp.springmongojwtapi.models.http.resources;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;

public class HateoasUtil {
	public static void setUserResources(User user) {
		if (user.hasEnoughAuthority(Authority.HR)) {
			Map<String, String> resources = Stream.of(Url.adminUserResources, Url.userResources)
					.flatMap(map -> map.entrySet().stream())
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			user.setResources(resources);
		} else {
			user.setResources(Url.userResources);
		}
	}
}
