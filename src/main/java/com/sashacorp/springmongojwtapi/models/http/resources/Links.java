package com.sashacorp.springmongojwtapi.models.http.resources;

import java.util.Map;

public class Links {
	private Map<String, Link> _links;
	
	public Links(Map<String, Link> _links) {
		super();
		this._links = _links;
	}
	
	public Map<String, Link> get_links() {
		return _links;
	}
	public void set_links(Map<String, Link> _links) {
		this._links = _links;
	}
	
	
}
