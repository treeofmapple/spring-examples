package com.tom.service.comms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tom.service.comms.models.User;
import com.tom.service.comms.models.enums.Status;
import com.tom.service.comms.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository repository;
	
	public void saveUser(User user) {
		user.setStatus(Status.ONLINE);
		repository.save(user);
	}
	
	public void disconnect(User user) {
		var storedUser = repository.findById(user.getId()).orElse(null);
		if(storedUser != null) {
			storedUser.setStatus(Status.OFFLINE);
			repository.save(storedUser);
		}
	}
	
	public List<User> findConnectedUsers() {
		return repository.findAllByStatus(Status.ONLINE);
	}

}
