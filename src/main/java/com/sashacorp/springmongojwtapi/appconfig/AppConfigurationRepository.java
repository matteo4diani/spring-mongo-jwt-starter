package com.sashacorp.springmongojwtapi.appconfig;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositor interface for the {@link AppConfiguration} entity
 * @author matteo
 *
 */
@Repository
public interface AppConfigurationRepository  extends MongoRepository<AppConfiguration, String>{
	
}
