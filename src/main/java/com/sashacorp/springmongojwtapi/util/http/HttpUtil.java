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
import com.sashacorp.springmongojwtapi.util.http.hateoas.Owned;

/**
 * Utility class to centralize HTTP responses and apply filters and decorators. See {@link Hateoas}, {@link HateoasUtil} and {@link Owned}.
 * @author matteo
 *
 */
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

	/**
	 * Get an HTTP response body of type T, decorated with the resources owned by the requester
	 * @param <T>
	 * @param obj
	 * @param httpStatus
	 * @param requester
	 * @return
	 */
	public static <T extends Hateoas & Owned> ResponseEntity<T> getResponse(T obj, HttpStatus httpStatus,
			User requester) {
		if (obj != null && requester != null) {
			HateoasUtil.hateoas(obj, requester);
		}
		return new ResponseEntity<T>(obj, httpStatus);
	}
	
	/**
	 * Get an HTTP response body of type List<T>, decorated with the resources owned by the requester
	 * @param <T>
	 * @param obj
	 * @param httpStatus
	 * @param requester
	 * @return
	 */
	public static <T extends Hateoas & Owned> ResponseEntity<List<T>> getResponse(List<T> obj, HttpStatus httpStatus,
			User requester) {
		if (obj != null && requester != null) {
			HateoasUtil.hateoas(obj, requester);
		}
		return new ResponseEntity<List<T>>(obj, httpStatus);
	}
}
