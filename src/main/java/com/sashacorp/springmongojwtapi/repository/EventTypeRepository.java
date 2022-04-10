package com.sashacorp.springmongojwtapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sashacorp.springmongojwtapi.models.persistence.msg.EventType;

/**
 * Repository interface for the {@link EventType} entity.
 * 
 * @author matteo
 *
 */
public interface EventTypeRepository extends MongoRepository<EventType, String> {

	@Query(value = "{}", fields = "{'name' : 1}")
	List<String> findAllEventTypeNames();
	
	public EventType findByName(String name);
	public Boolean existsByName(String name);

}
