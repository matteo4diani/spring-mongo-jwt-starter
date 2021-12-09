package com.sashacorp.springmongojwtapi.models.http.auth;

import java.io.Serializable;
import java.util.Set;

import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;

/**
 * Utility class representing a succesful authentication response from the server.
 * Includes jwt, username and user authorities.
 * @author matteo
 *
 */
public class AuthenticationResponse implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String jwt;
	private final String username;
	private final Set<Authority> authorities;

    public AuthenticationResponse(String jwt, String username, Set<Authority> authorities) {
        this.jwt = jwt;
        this.username = username;
        this.authorities = authorities;
    }

    public String getJwt() {
        return jwt;
    }

	public String getUsername() {
		return username;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}
    
    
}
