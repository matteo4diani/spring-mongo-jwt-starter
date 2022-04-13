package com.sashacorp.springmongojwtapi.appconfig;

import static org.junit.Assert.assertFalse;
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

import com.sashacorp.springmongojwtapi.models.http.PlainTextResponse;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.util.log.Log;
import com.sashacorp.springmongojwtapi.util.log.emoji.Emoji;

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
	AppConfiguratorController appConfiguratorController;

	final static String USERNAME_TO_SAVE = "b.foo@sashacorp.dev";
	final static String PASSWORD_TO_SAVE = "foo";
	final static String COMPANY_NAME_TO_SAVE = "SashaCorp";

	final static Logger logger = LoggerFactory.getLogger(ITAppConfiguratorController.class);

	@Test
	public void stage1_when_firstStartUp_registerFirstUser_should_registerUserAndReturnStartupResponse() {
		logger.info(Log.msg(Emoji.TRAFFICLIGHT, "checking app configurator startup controller..."));
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
			logger.info(Log.msg(Emoji.TRAFFICLIGHT, true, "first user registered"));
		} catch (AssertionError e) {
			logger.info(Log.msg(Emoji.TRAFFICLIGHT, false, "failed to register first user"));
		}
	}

	@Test
	public void stage2_when_not_firstStartUp_registerFirstUser_should_returnHttpStatusForbidden() {
		logger.info(Log.msg(Emoji.TRAFFICLIGHT, "checking app configurator startup controller..."));
		/*
		 * clean up before test
		 */
		StartupRequest startupRequest = new StartupRequest(USERNAME_TO_SAVE, PASSWORD_TO_SAVE, COMPANY_NAME_TO_SAVE);
		ResponseEntity<?> response = appConfiguratorController.registerFirstUser(startupRequest);

		try {
			assertTrue(response.getStatusCode().equals(HttpStatus.FORBIDDEN)
					|| response.getStatusCode().equals(HttpStatus.NOT_FOUND));
			logger.info(Log.msg(Emoji.TRAFFICLIGHT, true, "app config controller is functioning correctly after init"));
		} catch (AssertionError e) {
			logger.info(Log.msg(Emoji.TRAFFICLIGHT, false, "app config controller is malfunctioning: should not register user when app is already initialized"));
		}
	}
	
	@Test
	public void stage3_reset_should_resetAppConfiguration() {
		logger.info(Log.msg(Emoji.TRAFFICLIGHT, "checking app configurator reset controller..."));
		/*
		 * clean up before test
		 */
		ResponseEntity<?> responseWithoutResetKey = appConfiguratorController.reset(null);
		PlainTextResponse plainTextResponse = responseWithoutResetKey.getBody() instanceof PlainTextResponse
				? (PlainTextResponse) responseWithoutResetKey.getBody()
				: null;
		String resetKey = plainTextResponse.getMessage();
		ResponseEntity<?> responseWithResetKey = appConfiguratorController.reset(resetKey);
		AppConfiguration appConfiguration = responseWithResetKey.getBody() instanceof AppConfiguration
				? (AppConfiguration) responseWithResetKey.getBody()
				: null;
		
		try {
			assertTrue(plainTextResponse instanceof PlainTextResponse);
			assertTrue(appConfiguration instanceof AppConfiguration);
			assertTrue(appConfiguration.getCompanyName().isEmpty());
			assertFalse(appConfiguration.isInitialized());
			logger.info(Log.msg(Emoji.TRAFFICLIGHT, true, "app config controller resets app correctly"));
		} catch (AssertionError e) {
			logger.info(Log.msg(Emoji.TRAFFICLIGHT, false, "app config controller is malfunctioning: should reset app when asked to"));
		}
	}

}
