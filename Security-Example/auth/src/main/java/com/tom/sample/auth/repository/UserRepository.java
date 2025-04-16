package com.tom.sample.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tom.sample.auth.model.User;

public interface UserRepository extends MongoRepository<User, UUID> {

	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);

	
	
	
	// find most recent user
	
	// find by name or username
	
	// exists by name or username
	
	// remove by username
	
	
}
