package com.sashacorp.springmongojwtapi.models.http.auth;

import java.util.Set;

/**
 * Utility class representing a JSON signup request.
 * Accepts a username, a password and an array of strings representing authorities.
 * @author matteo
 *
 */
public class SignupRequest {
	
	
    private String username;
    
    private Set<String> authorities;
    
	private String password;
  
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
    
    public Set<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}
    
   

}
