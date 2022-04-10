package com.sashacorp.springmongojwtapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sashacorp.springmongojwtapi.models.persistence.user.User;

/**
 * Repository interface for the {@link User} entity.
 * 
 * @author matteo
 *
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

	public User findByUsername(String username);

	public User findByEmail(String username);

	public Boolean existsByUsername(String username);

	public Boolean existsByEmail(String email);

}
