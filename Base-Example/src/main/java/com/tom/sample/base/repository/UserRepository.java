package com.tom.sample.base.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tom.sample.base.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, UUID> {

	
	
	
	
	
	
}
