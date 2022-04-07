package com.sashacorp.springmongojwtapi.models.http.resources;

public class Link {
	
	private String href;
	private String description = "";
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
