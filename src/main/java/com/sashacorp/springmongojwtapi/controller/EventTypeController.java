package com.sashacorp.springmongojwtapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sashacorp.springmongojwtapi.models.persistence.message.EventType;
import com.sashacorp.springmongojwtapi.repository.EventTypeRepository;
import com.sashacorp.springmongojwtapi.util.http.HttpUtil;
import com.sashacorp.springmongojwtapi.util.http.Url;

/**
 * API endpoints to manage event (leaves) types
 * 
 * @author matteo
 *
 */
@RestController
@CrossOrigin
public class EventTypeController {

	@Autowired
	EventTypeRepository eventTypeRepository;

	/**
	 * Get all event/leave types. See {@link EventType}.
	 * 
	 * @return 
	 */
	@RequestMapping({ Url.EVENTS })
	public ResponseEntity<List<EventType>> getAllEventTypes() {
		return HttpUtil.getResponse(eventTypeRepository.findAll(), HttpStatus.OK);
	}
	/**
	 * Post a new event type
	 * @param eventType
	 * @return
	 */
	@PostMapping({ Url.NEW_EVENT })
	public ResponseEntity<EventType> postEventType(@RequestBody EventType eventType) {
		if (eventTypeRepository.existsByType(eventType.getType())) {
			return HttpUtil.getResponse(eventTypeRepository.findByType(eventType.getType()), HttpStatus.FORBIDDEN);
		}
		return HttpUtil.getResponse(eventTypeRepository.save(eventType), HttpStatus.CREATED);
	}

}
