package com.tom.service.comms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.tom.service.comms.models.User;
import com.tom.service.comms.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService service;

	@MessageMapping("/user.addUser")
	@SendTo("/user/public")
	public User addUser(@Payload User user) {
		service.saveUser(user);
		return user;
	}

	@MessageMapping("/user.disconnectUser")
	@SendTo("/user/public")
	public User disconnectUser(@Payload User user) {
		service.disconnect(user);
		return user;
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> findConnectedUsers() {
		return ResponseEntity.status(HttpStatus.OK).body(service.findConnectedUsers());
	}
}
