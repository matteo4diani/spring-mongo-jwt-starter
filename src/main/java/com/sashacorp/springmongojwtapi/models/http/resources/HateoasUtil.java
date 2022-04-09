package com.sashacorp.springmongojwtapi.models.http.resources;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sashacorp.springmongojwtapi.models.persistence.msg.Message;
import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;
import com.sashacorp.springmongojwtapi.models.persistence.user.Ownable;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;

public class HateoasUtil {
	
	public static <T extends Ownable & Hateoas> void setOwnedResources(T obj, User user) {
		boolean objIsFromUser = obj.getOwner().getUsername().equals(user.getUsername());
		MimeTypes type = getMimeType(obj);
		
		if (type != null) {			
			Map<String, String> adminResources = Rel.ResourceUtil.get(type, Authority.HR);
			Map<String, String> userResources = Rel.ResourceUtil.get(type, Authority.USER);
			
			if (user.hasEnoughAuthority(Authority.HR) && objIsFromUser) {
				Map<String, String> resources = Stream.of(adminResources, userResources)
						.flatMap(map -> map.entrySet().stream())
						.sorted(Comparator.comparing(Map.Entry::getKey))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
				obj.setResources(resources);
			} else if (user.hasEnoughAuthority(Authority.HR)) {
				obj.setResources(adminResources);
			} else if (objIsFromUser) {
				obj.setResources(userResources);			
			}
		}
	}
	
	public static <T extends Ownable & Hateoas> List<T> hateoas(List<T> obj, User user) {
		obj.forEach((o) -> {setOwnedResources(o, user);});
		return obj;
	}
	public static <T extends Ownable & Hateoas> T hateoas(T obj, User user) {
		setOwnedResources(obj, user);
		return obj;
	}
	
	public static <T> MimeTypes getMimeType(T obj) {
		if (obj instanceof Message) {
			return MimeTypes.MESSAGE;
		} else if (obj instanceof User) {
			return MimeTypes.USER;
		} else {
			return null;
		}
	}
}