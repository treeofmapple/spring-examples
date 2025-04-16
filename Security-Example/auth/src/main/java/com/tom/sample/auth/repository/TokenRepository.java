package com.tom.sample.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tom.sample.auth.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {

	@Query(value = """
			select t from Token t inner join User u\s
			on t.user.id = u.id\s
			where u.id = :id and (t.expired = false or t.revoked = false)\s
			""")
	List<Token> findAllValidTokenByUser(Integer id);
	
	Optional<Token> findByToken(String token);
	
	
	// find most recent user
	
	// find by name or username
	
	// exists by name or username
	
	// remove by username
	
}
