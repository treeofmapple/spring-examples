package com.tom.sample.auth.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.tom.sample.auth.model.User;

public interface UserRepository extends MongoRepository<User, UUID> {

	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);

	// Find users by partial name or username (case-insensitive)
	@Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :input, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :input, '%'))")
	List<User> findByUsernameOrEmailContainingIgnoreCase(@Param("input") String input);

	// find most recent user
	
	// find by name or username
	
	// exists by name or username
	
	// remove by username
	
}
