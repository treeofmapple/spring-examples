package com.tom.sample.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.sample.auth.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("v1/auth")
@PreAuthorize("hasRole('ANONYMOUS')")
// @Tag(name = "Anonymous") # Swagger
@RequiredArgsConstructor
public class AnonymousController {

	private final UserService service;
	
	@PreAuthorize("hasAuthority('anonymous')")
	// register
	@PostMapping("/authenticate")
	public ResponseEntity<?> registerUser() {
		var register = service.register(null);
		return ResponseEntity.status(HttpStatus.CREATED).body(register);
	}
	
	// authenticate
	@PostMapping("/authenticate")
	public ResponseEntity<?> authenticateUser() {
		var authenticate = service.authenticate(null);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(authenticate);
	}
	
	
	// refresh token
	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken() {
		var refreshToken = service.refreshToken(null, null);
		return ResponseEntity.status(HttpStatus.OK).body(refreshToken);
	}
	
	
	// change password without connection
	@PutMapping("/password")
	public ResponseEntity<?> changePassword() {
		var password = service.changePassword(null, null);
		return ResponseEntity.status(HttpStatus.OK).body(password);
	}
	
	
}
