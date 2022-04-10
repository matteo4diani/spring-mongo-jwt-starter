package com.sashacorp.springmongojwtapi.util.http.hateoas;

import java.util.Map;

/**
 * Represents an entity's relevant links, in the form of a Map< String, {@link Link}>, conventionally named {@code _links}.<br/>
 * Exposes a {@code public Map<String, Link> get_links()} method, violating Java naming conventions to leverage Spring's JSON serialization
 * and obtain a JSON with the desired '_links' properties. (FYI: Spring gets JSON property names by public getters, if you expose a public getter
 * with no underlying property it WILL be serialized)
 * @author matteo
 *
 */
public class Links {
	private Map<String, Link> _links;
	
	public Links(Map<String, Link> _links) {
		super();
		this._links = _links;
	}
	/**
	 * 
	 * @return the entities relevant links. Violates Java naming conventions to leverage Spring's JSON serialization
	 */
	public Map<String, Link> get_links() {
		return _links;
	}
	public void set_links(Map<String, Link> _links) {
		this._links = _links;
	}
	
	
}
