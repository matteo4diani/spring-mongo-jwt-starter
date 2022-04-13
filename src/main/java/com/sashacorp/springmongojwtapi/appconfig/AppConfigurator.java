package com.sashacorp.springmongojwtapi.appconfig;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sashacorp.springmongojwtapi.models.persistence.message.EventType;
import com.sashacorp.springmongojwtapi.models.persistence.user.DefaultStatus;
import com.sashacorp.springmongojwtapi.repository.EventTypeRepository;
import com.sashacorp.springmongojwtapi.repository.UserRepository;


public class AppConfigurator {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	AppConfigurationRepository appConfigRepository;
	@Autowired
	EventTypeRepository eventTypeRepository;
	
	/**
	 * Get current application configuration. 
	 * If none is found, a blank config is generated and returned.
	 * If more than one config is found, the first one is returned and the rest are deleted.
	 * @return
	 */
	public AppConfiguration getAppConfiguration() {
		List<AppConfiguration> configs = appConfigRepository.findAll();
		if (configs == null || configs.isEmpty()) 
			return appConfigRepository.save(new AppConfiguration("", false));
		else if  (configs.size() > 1) {
			AppConfiguration config = configs.remove(0);
			appConfigRepository.deleteAll(configs);
			return config;
		}
		return configs.get(0); 
	}
	
	/**
	 * Save an application configuration to persistence. If appConfig is null, creates new blank config.
	 * @param appConfig
	 * @return
	 */
	public AppConfiguration setAppConfiguration(AppConfiguration appConfig) {
		List<AppConfiguration> configs = appConfigRepository.findAll();
		if (appConfig == null) {
			appConfig = new AppConfiguration("", false);
		}
		if  (configs.size() > 0) {
			appConfigRepository.deleteAll(configs);
		}
		
		return appConfigRepository.save(appConfig);
 
	}
	public boolean isAppUninitialized() {
		return !getAppConfiguration().isInitialized();
	}
	
	/**
	 * Initializes the event types repository with basic defaults
	 * @return
	 */
	public List<EventType> initEventTypeRepository() {
		List<EventType> defaultEventTypes = new ArrayList<>();
		defaultEventTypes.add(new EventType(DefaultStatus.LONG.type(), DefaultStatus.LONG.description()));
		defaultEventTypes.add(new EventType(DefaultStatus.SHORT.type(),  DefaultStatus.SHORT.description()));
		return eventTypeRepository.saveAll(defaultEventTypes);
	}

}
