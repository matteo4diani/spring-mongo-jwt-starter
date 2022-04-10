package com.sashacorp.springmongojwtapi.appconfig;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppConfigurationRepository  extends MongoRepository<AppConfiguration, String>{
	
}
