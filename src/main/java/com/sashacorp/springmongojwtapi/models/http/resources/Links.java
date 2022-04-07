package com.sashacorp.springmongojwtapi.models.http.resources;

import java.util.Map;

public class Links {
	private Link _self;
	private Map<String, Link> _links;
	
	public Link get_self() {
		return _self;
	}
	public void set_self(Link _self) {
		this._self = _self;
	}
	public Map<String, Link> get_links() {
		return _links;
	}
	public void set_links(Map<String, Link> _links) {
		this._links = _links;
	}
	
	
}
