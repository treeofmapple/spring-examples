package com.tom.service.comms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tom.service.comms.models.User;
import com.tom.service.comms.models.enums.Status;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	List<User> findAllByStatus(Status status);
	
}
