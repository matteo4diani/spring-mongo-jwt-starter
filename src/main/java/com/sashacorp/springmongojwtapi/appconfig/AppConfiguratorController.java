package com.sashacorp.springmongojwtapi.appconfig;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.EventTypeRepository;
import com.sashacorp.springmongojwtapi.repository.MessageRepository;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.security.Authority;
import com.sashacorp.springmongojwtapi.util.http.HttpUtil;
import com.sashacorp.springmongojwtapi.util.http.Par;
import com.sashacorp.springmongojwtapi.util.http.Url;
import com.sashacorp.springmongojwtapi.util.http.hateoas.HateoasUtil;

/**
 * API endpoints to manage application configuration
 * @author matteo
 *
 */
@RestController
@CrossOrigin
public class AppConfiguratorController {

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
	
	
	/**
	 * Endpoint for registering super-administrator on the first run.
	 * After registration the '/startup' mapping is switched off.
	 * @param startupRequest
	 * @return
	 */
	@PostMapping(Url.STARTUP)
	public ResponseEntity<?> registerFirstUser(@RequestBody StartupRequest startupRequest) {
		if (appConfigurator.getAppConfiguration() == null) {
			return HttpUtil.getHttpStatusResponse(HttpStatus.NOT_FOUND);
		}
		if (!appConfigurator.isAppUninitialized()) {
			return HttpUtil.getHttpStatusResponse(HttpStatus.FORBIDDEN);
		}
		
		appConfigurator.initEventTypeRepository();
		
		User user = new User(startupRequest.getUsername(), encoder.encode(startupRequest.getPassword()),
				startupRequest.getUsername());
		
		AppConfiguration appConfig = new AppConfiguration(startupRequest.getCompanyName(), true);
		
		Set<Authority> authorities = new HashSet<>();
		
		authorities.add(Authority.ADMIN);
		authorities.add(Authority.MANAGER);
		authorities.add(Authority.HR);
		authorities.add(Authority.USER);

		user.setAuthorities(authorities);
		User savedUser = userRepository.save(user);
		AppConfiguration savedConfig = appConfigurator.setAppConfiguration(appConfig);
		HateoasUtil.hateoas(savedUser, savedUser);
		return HttpUtil.getResponse(new StartupResponse(savedUser, savedConfig), HttpStatus.CREATED);
	}
	
	/**
	 * Endpoint for a complete clean-up of all resources and configuration.
	 * If the resetKey parameter is not present, returns the current application configuration UUID.
	 * If the resetKey parameter is present, accepts the current application configuration UUID and resets all collections and state.
	 * Admin only.
	 * @param startupRequest
	 * @return
	 */
	@PostMapping(Url.RESET)
	public ResponseEntity<?> stopAndClean(@RequestParam(name = Par.RESET_KEY, required = false) String resetKey) {
		if (appConfigurator.getAppConfiguration() == null) {
			return HttpUtil.getHttpStatusResponse(HttpStatus.NOT_FOUND);
		}
		if (resetKey == null) {
			resetKey = appConfigurator.getAppConfiguration().getId();
			return HttpUtil.getPlainTextResponse(resetKey, HttpStatus.OK);
		} else if (resetKey.equals(appConfigurator.getAppConfiguration().getId())) {
			userRepository.deleteAll();
			messageRepository.deleteAll();
			eventTypeRepository.deleteAll();
			AppConfiguration savedConfig = appConfigurator.setAppConfiguration(null);
			return HttpUtil.getResponse(savedConfig, HttpStatus.CREATED);			
		} else {
			return HttpUtil.getHttpStatusResponse(HttpStatus.FORBIDDEN);
		}
	}
	
}
