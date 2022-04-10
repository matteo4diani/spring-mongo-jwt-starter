package com.sashacorp.springmongojwtapi.util.http;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sashacorp.springmongojwtapi.models.http.PlainTextResponse;
import com.sashacorp.springmongojwtapi.models.http.auth.AuthenticationResponse;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.security.Authority;
import com.sashacorp.springmongojwtapi.util.http.hateoas.Hateoas;
import com.sashacorp.springmongojwtapi.util.http.hateoas.HateoasUtil;
import com.sashacorp.springmongojwtapi.util.http.hateoas.Ownable;

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

	public static <T> ResponseEntity<List<T>> getResponse(List<T> obj, HttpStatus httpStatus) {
		return new ResponseEntity<List<T>>(obj, httpStatus);
	}

	public static <T extends Hateoas & Ownable> ResponseEntity<T> getResponse(T obj, HttpStatus httpStatus,
			User requester) {
		if (obj != null && requester != null) {
			HateoasUtil.hateoas(obj, requester);
		}
		return new ResponseEntity<T>(obj, httpStatus);
	}

	public static <T extends Hateoas & Ownable> ResponseEntity<List<T>> getResponse(List<T> obj, HttpStatus httpStatus,
			User requester) {
		if (obj != null && requester != null) {
			HateoasUtil.hateoas(obj, requester);
		}
		return new ResponseEntity<List<T>>(obj, httpStatus);
	}
}
