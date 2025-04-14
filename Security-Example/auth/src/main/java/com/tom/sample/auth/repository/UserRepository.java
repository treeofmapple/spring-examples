package com.tom.sample.auth.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tom.sample.auth.model.model.User;

public interface UserRepository extends MongoRepository<User, UUID> {

	
	
	
	// find most recent user
	
	// find by name or username
	
	// exists by name or username
	
	// remove by username
	
	
}
