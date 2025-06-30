package com.tom.front.authbasic.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tom.front.authbasic.user.dto.DeleteAccountRequest;
import com.tom.front.authbasic.user.dto.PasswordUpdateRequest;
import com.tom.front.authbasic.user.dto.UpdateAccountRequest;
import com.tom.front.authbasic.user.dto.UserPageResponse;
import com.tom.front.authbasic.user.dto.UserResponse;
import com.tom.front.authbasic.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/v1/user")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping(value = "/search", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserPageResponse> findUserByParams(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String username,
			@RequestParam(required = false) String email,
			@RequestParam(required = false, defaultValue = "30") Integer age,
			HttpServletRequest httpRequest) {
		var response = userService.findUserByParams(page, username, email, age, httpRequest); 
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
		var response = userService.getCurrentUser(principal);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PutMapping(value = "/me/edit",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> editUser(@RequestBody UpdateAccountRequest request, Principal connectedUser, HttpServletRequest httpRequest) {
		var response = userService.editUser(request, connectedUser, httpRequest);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
	}

	@PutMapping(value = "/me/password",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> changeUserPassword(@RequestBody PasswordUpdateRequest request, Principal connectedUser, HttpServletRequest httpRequest) {
		userService.changePassword(request, connectedUser, httpRequest);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}
	
	@PostMapping(value = "logout")
	public ResponseEntity<Void> logout(Principal connectedUser, HttpServletRequest request) {
		userService.logout(connectedUser, request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@DeleteMapping(value = "/me/delete",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> deleteMyAccount(@RequestBody DeleteAccountRequest request, Principal connectedUser, HttpServletRequest httpRequest) {
		userService.deleteMyAccount(request, connectedUser, httpRequest);
		return ResponseEntity.status(HttpStatus.ACCEPTED).build();
	}

}

