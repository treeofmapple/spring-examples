package com.tom.sample.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.sample.auth.dto.AuthenticationRequest;
import com.tom.sample.auth.dto.AuthenticationResponse;
import com.tom.sample.auth.dto.RegisterRequest;
import com.tom.sample.auth.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
// @Tag(name = "Anonymous") # Swagger
@RequiredArgsConstructor
public class AnonymousController {

	private final UserService service;
	
	// register
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request) {
		var register = service.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(register);
	}
	
	// authenticate
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
		var authenticate = service.authenticate(request);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(authenticate);
	}
	
}
                                                                                                                                                                                                                                                                                                                                      