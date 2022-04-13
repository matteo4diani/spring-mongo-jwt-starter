package com.sashacorp.springmongojwtapi.controller;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.sashacorp.springmongojwtapi.appconfig.AppConfigurator;
import com.sashacorp.springmongojwtapi.appconfig.ITAppConfiguratorController;
import com.sashacorp.springmongojwtapi.models.persistence.message.EventType;
import com.sashacorp.springmongojwtapi.repository.EventTypeRepository;
import com.sashacorp.springmongojwtapi.util.log.Log;
import com.sashacorp.springmongojwtapi.util.log.emoji.Emoji;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ITEventTypeController {
	@Autowired
	AppConfigurator appConfigurator;
	@Autowired
	EventTypeRepository eventTypeRepository;
	@Autowired
	EventTypeController eventTypeController;
	
	private static final String EVENT_TYPE_TO_SAVE = "EXAMPLE";
	
	final static Logger logger = LoggerFactory.getLogger(ITAppConfiguratorController.class);

	@Test
	public void stage1_postEventType_should_saveEventType_but_refuseExistingEventTypes() {
		logger.info(Log.msg(Emoji.CALENDAR, "checking event type controller..."));
		appConfigurator.initEventTypeRepository();
		List<EventType> eventTypes = eventTypeController.getAllEventTypes().getBody();
		ResponseEntity<EventType> responseToInvalidRequest = eventTypeController.postEventType(eventTypes.get(0));
		ResponseEntity<EventType> responseToValidRequest = eventTypeController.postEventType(new EventType(EVENT_TYPE_TO_SAVE, ""));
		
		try {			
			assertTrue(responseToInvalidRequest.getStatusCode().equals(HttpStatus.FORBIDDEN));
			assertTrue(responseToValidRequest.getStatusCode().equals(HttpStatus.CREATED));
			assertTrue(responseToValidRequest.getBody() instanceof EventType);
			assertTrue(eventTypeRepository.existsByType(EVENT_TYPE_TO_SAVE));
			logger.info(Log.msg(Emoji.CALENDAR, true, "event type controller is working as intended"));
		} catch (AssertionError e) {
			logger.info(Log.msg(Emoji.CALENDAR, false, "event type controller is malfunctioning"));
			
		}
		eventTypeRepository.deleteAll();
	}
}
