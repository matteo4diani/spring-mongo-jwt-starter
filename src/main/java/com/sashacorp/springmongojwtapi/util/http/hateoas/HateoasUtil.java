package com.sashacorp.springmongojwtapi.util.http.hateoas;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sashacorp.springmongojwtapi.models.persistence.message.Message;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.security.Authority;

/**
 * Utility class to centralize HATEOAS resource generation
 * @author matteo
 *
 */
public class HateoasUtil {
	
	public static <T extends Owned & Hateoas> void setOwnedResources(T obj, User user) {
		boolean objIsOwnedByUser = obj.getOwner().getUsername().equals(user.getUsername());
		MimeType type = getMimeType(obj);
		
		if (type != null) {			
			Map<String, String> adminResources = Rel.ResourceUtil.get(type, Authority.HR);
			Map<String, String> userResources = Rel.ResourceUtil.get(type, Authority.USER);
			
			if (user.hasEnoughAuthority(Authority.HR) && objIsOwnedByUser) {
				Map<String, String> resources = Stream.of(adminResources, userResources)
						.flatMap(map -> map.entrySet().stream())
						.sorted(Comparator.comparing(Map.Entry::getKey))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
				obj.setResources(resources);
			} else if (user.hasEnoughAuthority(Authority.HR)) {
				obj.setResources(adminResources);
			} else if (objIsOwnedByUser) {
				obj.setResources(userResources);			
			}
		}
	}
	
	public static <T extends Owned & Hateoas> List<T> hateoas(List<T> obj, User user) {
		obj.forEach((o) -> {setOwnedResources(o, user);});
		return obj;
	}
	public static <T extends Owned & Hateoas> T hateoas(T obj, User user) {
		setOwnedResources(obj, user);
		return obj;
	}
	
	public static <T> MimeType getMimeType(T obj) {
		if (obj instanceof Message) {
			return MimeType.MESSAGE;
		} else if (obj instanceof User) {
			return MimeType.USER;
		} else {
			return null;
		}
	}
}