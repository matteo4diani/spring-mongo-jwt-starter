package com.sashacorp.springmongojwtapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.sashacorp.springmongojwtapi.models.persistence.message.EventType;

/**
 * Repository interface for the {@link EventType} entity.
 * 
 * @author matteo
 *
 */
@Repository
public interface EventTypeRepository extends MongoRepository<EventType, String> {

	@Query(value = "{}", fields = "{'type' : 1}")
	List<String> findAllEventTypeNames();
	
	public EventType findByType(String name);
	public Boolean existsByType(String name);
}
