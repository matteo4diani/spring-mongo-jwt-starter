package com.sashacorp.springmongojwtapi.appconfig;

import com.sashacorp.springmongojwtapi.models.persistence.user.User;

public class StartupResponse {
	private User admin;
	private AppConfiguration appConfig;
	
	public StartupResponse(User admin, AppConfiguration appConfig) {
		super();
		this.admin = admin;
		this.appConfig = appConfig;
	}
	
	public User getAdmin() {
		return admin;
	}
	public void setAdmin(User admin) {
		this.admin = admin;
	}
	public AppConfiguration getAppConfig() {
		return appConfig;
	}
	public void setAppConfig(AppConfiguration appConfig) {
		this.appConfig = appConfig;
	}
	
	
}
