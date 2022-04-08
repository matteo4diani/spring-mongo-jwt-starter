package com.sashacorp.springmongojwtapi.models.http.resources;

public class Link {
	
	private String href;
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public Link(String href) {
		super();
		this.href = href;
	}
	
	
	
}
