package com.sashacorp.springmongojwtapi.appconfig;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "appconfig")
public class AppConfiguration {
	@Id
	private String id;
	
	private String companyName;
	private boolean isInitialized;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AppConfiguration(String companyName, boolean isInitialized) {
		super();
		this.companyName = companyName;
		this.isInitialized = isInitialized;
	}

	public String getCompanyName() {
		return companyName;
	}

	public boolean isInitialized() {
		return isInitialized;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setInitialized(boolean isInitialized) {
		this.isInitialized = isInitialized;
	}

	@Override
	public String toString() {
		return "AppConfiguration [id=" + id + ", companyName=" + companyName + ", isInitialized=" + isInitialized + "]";
	}
	
	
	
	
	
	
}
