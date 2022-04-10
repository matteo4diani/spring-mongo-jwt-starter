package com.sashacorp.springmongojwtapi.appconfig;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sashacorp.springmongojwtapi.models.persistence.msg.EventType;
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
	public AppConfiguration setAppConfiguration(AppConfiguration appConfig) {
		List<AppConfiguration> configs = appConfigRepository.findAll();
		
		if  (configs.size() > 0) {
			appConfigRepository.deleteAll(configs);
		}
		
		return appConfigRepository.save(appConfig);
 
	}
	public boolean isAppUninitialized() {
		return !getAppConfiguration().isInitialized();
	}
	
	public List<EventType> initEventTypeRepository() {
		List<EventType> defaultEventTypes = new ArrayList<>();
		defaultEventTypes.add(new EventType(DefaultStatus.LONG.type(), DefaultStatus.LONG.description()));
		defaultEventTypes.add(new EventType(DefaultStatus.SHORT.type(),  DefaultStatus.SHORT.description()));
		return eventTypeRepository.saveAll(defaultEventTypes);
	}

}
