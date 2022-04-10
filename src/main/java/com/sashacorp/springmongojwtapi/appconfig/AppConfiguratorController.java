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
import org.springframework.web.bind.annotation.RestController;

import com.sashacorp.springmongojwtapi.models.persistence.user.Authority;
import com.sashacorp.springmongojwtapi.models.persistence.user.User;
import com.sashacorp.springmongojwtapi.repository.LeaveRepository;
import com.sashacorp.springmongojwtapi.repository.MessageRepository;
import com.sashacorp.springmongojwtapi.repository.UserRepository;
import com.sashacorp.springmongojwtapi.util.http.HttpUtil;
import com.sashacorp.springmongojwtapi.util.http.hateoas.HateoasUtil;


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
	LeaveRepository leaveRepository;
	
	
	/**
	 * Endpoint for registering super-administrator on the first run.
	 * After registration the mapping is switched off.
	 * @param startupRequest
	 * @return
	 */
	@PostMapping("/startup")
	public ResponseEntity<?> registerFirstUser(@RequestBody StartupRequest startupRequest) {
		if (appConfigurator.getAppConfiguration() == null) {
			return HttpUtil.getHttpStatusResponse(HttpStatus.NOT_FOUND);
		}
		if (!appConfigurator.isAppUninitialized()) {
			return HttpUtil.getHttpStatusResponse(HttpStatus.FORBIDDEN);
		}
		
		
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
	 * Admin only.
	 * @param startupRequest
	 * @return
	 */
	@PostMapping("/reset")
	public ResponseEntity<?> stopAndClean(@RequestBody StartupRequest startupRequest) {
		if (appConfigurator.getAppConfiguration() == null) {
			return HttpUtil.getHttpStatusResponse(HttpStatus.NOT_FOUND);
		}
		
		userRepository.deleteAll();
		messageRepository.deleteAll();
		leaveRepository.deleteAll();
		AppConfiguration appConfig = new AppConfiguration("", false);	
		AppConfiguration savedConfig = appConfigurator.setAppConfiguration(appConfig);
		return HttpUtil.getResponse(savedConfig, HttpStatus.CREATED);
	}
	
}
