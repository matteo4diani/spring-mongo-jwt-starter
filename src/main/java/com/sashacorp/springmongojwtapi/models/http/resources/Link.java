package com.sashacorp.springmongojwtapi.models.http.resources;

public class Link {
	
	private String href;
	private String mimeType;
	
	public Link(String href, String mimeType) {
		super();
		this.mimeType = mimeType;
		this.href = href;
	}
	
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	
	
	
	
}
