package com.sashacorp.springmongojwtapi.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sashacorp.springmongojwtapi.models.http.MessageResponse;

public class HttpUtil {
	public static ResponseEntity<MessageResponse> getMessageResponse(String message, HttpStatus httpStatus) {
		return new ResponseEntity<MessageResponse>(new MessageResponse(message),
				httpStatus);
	}
}
