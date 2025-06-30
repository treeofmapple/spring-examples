package com.tom.front.authbasic.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.tom.front.authbasic.user.model.User;

@Repository
public interface UserRepository extends JpaRepository <User, UUID>, JpaSpecificationExecutor<User> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

}
