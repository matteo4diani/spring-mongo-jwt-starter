package com.sashacorp.springmongojwtapi.models.http.resources;

import java.util.Map;

public interface Hateoas {
	public Links get_resources();
	
	default void set_resources(String selfUri, Map<String, String> uris) {
		
	};
}
