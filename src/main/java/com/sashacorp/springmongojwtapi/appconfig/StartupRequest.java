package com.sashacorp.springmongojwtapi.appconfig;

/**
 * Utility class representing a JSON startup request. Accepts a username, a
 * password and a company name to initialize the web application.
 * 
 * @author matteo
 *
 */
public class StartupRequest {

	private String username;

	private String password;
	
	private String companyName;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public StartupRequest(String username, String password, String companyName) {
		super();
		this.username = username;
		this.password = password;
		this.companyName = companyName;
	}
	
}
