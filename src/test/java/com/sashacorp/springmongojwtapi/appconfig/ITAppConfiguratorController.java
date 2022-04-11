package com.sashacorp.springmongojwtapi.appconfig;

import static org.junit.Assert.assertTrue;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.EventTypeRepository;
import com.sashacorp.springmongojwtapi.repository.MessageRepository;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.util.log.test.Emoji;
import com.sashacorp.springmongojwtapi.util.log.test.Log;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ITAppConfiguratorController {
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	AppConfigurator appConfigurator;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;
	@Autowired
	EventTypeRepository eventTypeRepository;

	@Autowired
	AppConfiguratorController appConfiguratorController;

	final static String USERNAME_TO_SAVE = "b.foo@sashacorp.dev";
	final static String PASSWORD_TO_SAVE = "foo";
	final static String COMPANY_NAME_TO_SAVE = "SashaCorp";

	final static Logger LOG = LoggerFactory.getLogger(ITAppConfiguratorController.class);

	@Test
	public void stage1_when_firstStartUp_registerFirstUser_should_registerUserAndReturnStartupResponse() {
		LOG.info(Log.msg(Emoji.TRAFFICLIGHT, "checking app configurator startup/shutdown controller..."));
		/*
		 * clean up before test
		 */
		userRepository.deleteAll();
		appConfigurator.setAppConfiguration(null);
		StartupRequest startupRequest = new StartupRequest(USERNAME_TO_SAVE, PASSWORD_TO_SAVE, COMPANY_NAME_TO_SAVE);
		ResponseEntity<?> response = appConfiguratorController.registerFirstUser(startupRequest);
		StartupResponse startupResponse = response.getBody() instanceof StartupResponse
				? (StartupResponse) response.getBody()
				: null;

		assertTrue(startupResponse instanceof StartupResponse);
		assertTrue(startupResponse != null);
		assertTrue(startupResponse.getAdmin().getUsername().equals(startupRequest.getUsername()));
		/*
		 * The password field is notated with {@link JsonIgnore}, so we have to recover
		 * the user from repo
		 */
		User savedUser = userRepository.findByUsername(USERNAME_TO_SAVE);
		try {
			assertTrue(encoder.matches(PASSWORD_TO_SAVE, savedUser.getPassword()));
			assertTrue(startupResponse.getAppConfig().isInitialized());
			assertTrue(startupResponse.getAppConfig().getCompanyName().equals(COMPANY_NAME_TO_SAVE));
			LOG.info(Log.msg(Emoji.TRAFFICLIGHT, true, "first user registered"));
		} catch (AssertionError e) {
			LOG.info(Log.msg(Emoji.TRAFFICLIGHT, false, "failed to register first user"));
		}
	}

	@Test
	public void stage1_when_not_firstStartUp_registerFirstUser_should_returnHttpStatusForbidden() {
		LOG.info(Log.msg(Emoji.TRAFFICLIGHT, "checking app configurator startup/shutdown controller..."));
		/*
		 * clean up before test
		 */
		StartupRequest startupRequest = new StartupRequest(USERNAME_TO_SAVE, PASSWORD_TO_SAVE, COMPANY_NAME_TO_SAVE);
		ResponseEntity<?> response = appConfiguratorController.registerFirstUser(startupRequest);

		try {
			assertTrue(response.getStatusCode().equals(HttpStatus.FORBIDDEN)
					|| response.getStatusCode().equals(HttpStatus.NOT_FOUND));
			LOG.info(Log.msg(Emoji.TRAFFICLIGHT, true, "app config controller is functioning correctly after init"));
		} catch (AssertionError e) {
			LOG.info(Log.msg(Emoji.TRAFFICLIGHT, false, "app config controller is malfunctioning: should not register user when app is already initialized"));
		}
	}

}
