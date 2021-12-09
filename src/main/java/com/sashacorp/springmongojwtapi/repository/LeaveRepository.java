package com.sashacorp.springmongojwtapi.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.sashacorp.springmongojwtapi.models.persistence.msg.Leave;

/**
 * Repository interface for the {@link Leave} entity.
 * @author matteo
 *
 */
public interface LeaveRepository extends MongoRepository <Leave, String>{
	
	@Query(value="{}", fields="{'name' : 1}")
	List<String> findAllLeaveNames();
	
	
}
