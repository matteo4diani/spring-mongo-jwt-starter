package com.sashacorp.springmongojwtapi.util;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sashacorp.springmongojwtapi.models.http.PlainTextResponse;
import com.sashacorp.springmongojwtapi.models.http.auth.AuthenticationResponse;
import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;

public class HttpUtil {
	public static ResponseEntity<PlainTextResponse> getPlainTextResponse(String message, HttpStatus httpStatus) {
		return new ResponseEntity<PlainTextResponse>(new PlainTextResponse(message), httpStatus);
	}

	public static ResponseEntity<AuthenticationResponse> getAuthenticationResponse(String jwt, String username,
			Set<Authority> authorities, HttpStatus httpStatus) {
		return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwt, username, authorities),
				httpStatus);
	}

	public static ResponseEntity<Void> getHttpStatusResponse(HttpStatus httpStatus) {
		return new ResponseEntity<Void>(httpStatus);
	}

	public static <T> ResponseEntity<T> getResponse(T obj, HttpStatus httpStatus) {
		return new ResponseEntity<T>(obj, httpStatus);
	}
}
