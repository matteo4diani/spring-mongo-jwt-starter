package com.sashacorp.springmongojwtapi.appconfig;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.sashacorp.springmongojwtapi.models.persistence.msg.EventType;
import com.sashacorp.springmongojwtapi.models.persistence.user.DefaultStatus;
import com.sashacorp.springmongojwtapi.repository.EventTypeRepository;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.util.log.test.Emoji;
import com.sashacorp.springmongojwtapi.util.log.test.Log;

/**
 * Test for {@link AppConfigurator}
 * Tests basic functionality of startup/shutdown cycle
 * @author matteo
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ITAppConfigurator {
	@Autowired
	AppConfigurator appConfigurator;
	@Autowired
	UserRepository userRepository;
	@Autowired
	AppConfigurationRepository appConfigRepository;
	@Autowired
	EventTypeRepository eventTypeRepository;
	
	final static Logger LOG = LoggerFactory.getLogger(ITAppConfigurator.class);
	private static AppConfiguration savedAppConfiguration;
	private static List<EventType> savedDefaultEventTypeList;
	private static final String COMPANY_NAME_TO_SAVE = "SashaCorp";
	@Test
	public void stage1_setAppConfiguration_should_setAppConfigurationAndReturnIt() {
		LOG.info(Log.msg(Emoji.ROBOT, "checking app configurator..."));
		savedAppConfiguration = appConfigurator.setAppConfiguration(new AppConfiguration(COMPANY_NAME_TO_SAVE, false));
		
		try {			
			assertTrue(savedAppConfiguration != null 
					&& savedAppConfiguration.getCompanyName().equals(COMPANY_NAME_TO_SAVE)
					&& !savedAppConfiguration.isInitialized());
			LOG.info(Log.msg(Emoji.ROBOT, true, "set app config succeeded"));
		} catch (AssertionError e) {
			LOG.info(Log.msg(Emoji.ROBOT, false, "failed setting app configuration"));
		}
		
	}
	@Test
	public void stage2_getAppConfiguration_should_returnSavedAppConfiguration() {
		LOG.info(Log.msg(Emoji.ROBOT, "still checking app configurator..."));
		try {			
			assertTrue(appConfigurator.getAppConfiguration().equals(savedAppConfiguration));
			appConfigurator.setAppConfiguration(null);
			LOG.info(Log.msg(Emoji.ROBOT, true, "get app config succeeded"));
		} catch (AssertionError e) {
			LOG.info(Log.msg(Emoji.ROBOT, false, "failed getting app configuration"));
		}
	}
	@Test
	public void stage3_initEventTypesRepository_should_initEventTypeRepositoryAndReturnList() {
		LOG.info(Log.msg(Emoji.CALENDAR, "checking event type repo initialization..."));
		List<EventType> defaultEventTypes = new ArrayList<>();
		defaultEventTypes.add(new EventType(DefaultStatus.LONG.type(), DefaultStatus.LONG.description()));
		defaultEventTypes.add(new EventType(DefaultStatus.SHORT.type(),  DefaultStatus.SHORT.description()));
		savedDefaultEventTypeList = eventTypeRepository.saveAll(defaultEventTypes);
		try {			
			assertTrue(defaultEventTypes.equals(savedDefaultEventTypeList));
			eventTypeRepository.deleteAll();
			LOG.info(Log.msg(Emoji.CALENDAR, true, "event type repo initialized"));
		} catch (AssertionError e) {
			LOG.info(Log.msg(Emoji.CALENDAR, false, "failed to init event type repo"));
		}
	}
}
