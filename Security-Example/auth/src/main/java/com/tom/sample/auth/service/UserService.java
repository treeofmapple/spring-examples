package com.tom.sample.auth.service;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tom.sample.auth.common.EntityUpdater;
import com.tom.sample.auth.common.ServiceLogger;
import com.tom.sample.auth.dto.AuthenticationRequest;
import com.tom.sample.auth.dto.AuthenticationResponse;
import com.tom.sample.auth.dto.PasswordRequest;
import com.tom.sample.auth.dto.RegisterRequest;
import com.tom.sample.auth.dto.UpdateRequest;
import com.tom.sample.auth.dto.UserResponse;
import com.tom.sample.auth.exception.IllegalStatusException;
import com.tom.sample.auth.exception.NotFoundException;
import com.tom.sample.auth.mapper.SystemMapper;
import com.tom.sample.auth.model.User;
import com.tom.sample.auth.repository.UserRepository;
import com.tom.sample.auth.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final JwtService jwtService;
	private final SystemMapper mapper;
	private final AuthenticationManager authManager;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository repository;
	private final EntityUpdater updater;

	// get user by name or email
	public List<UserResponse> findUser(String userInfo, Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		ServiceLogger.info("The user {}, is searching for: {}", user.getUsername(), userInfo);
		
		var users = repository.findByUsernameOrEmailContainingIgnoreCase(userInfo);
		if (users.isEmpty()) {
			throw new NotFoundException("No users found matching: " + userInfo);
		}
		return users.stream()
		            .map(mapper::buildUserResponse)
		            .toList();
	}
	
	// edit connected user
	public void editUser(UpdateRequest request, Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		if(!request.password().equals(request.confirmPassword())) {
			throw new IllegalStatusException("Wrong Password");
		}
		var data = updater.mergeData(user, request);
		repository.save(data);
		ServiceLogger.info("User {} changed their password", user.getUsername());
	}
	
	// logout connected user
	public void logout(Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		updater.revokeAllUserTokens(user);
		ServiceLogger.info("User {} has logged out. All valid tokens revoked.", user.getUsername());
	}
	
	// connected user
	public void changePassword(PasswordRequest request, Principal connectedUser) {
		var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		
		if (!passwordEncoder.matches(request.confirmationPassword(), user.getPassword())) {
			ServiceLogger.warn("Wrong Password");
			throw new IllegalStatusException("Wrong Password");
		}

		if (!request.newPassword().equals(request.confirmationPassword())) {
			ServiceLogger.warn("Passwords are not the same");
			throw new IllegalStatusException("Passwords are not the same");
		}

		user.setPassword(passwordEncoder.encode(request.newPassword()));
		repository.save(user);
		ServiceLogger.info("User {} changed their password", user.getUsername());
	}

	// request user name
	public void changePassword(String userInfo, PasswordRequest request) {
		var user = repository.findByUsername(userInfo)
				.or(() -> repository.findByEmail(userInfo))
				.orElseThrow(() -> new NotFoundException(""));

		if (!passwordEncoder.matches(request.confirmationPassword(), user.getPassword())) {
			throw new IllegalStatusException("Wrong Password");
		}

		if (!request.newPassword().equals(request.confirmationPassword())) {
			throw new IllegalStatusException("Passwords are not the same");
		}

		user.setPassword(passwordEncoder.encode(request.newPassword()));
		repository.save(user);
		ServiceLogger.info("Password changed for user {}", userInfo);
	}

	@Transactional
	public AuthenticationResponse register(RegisterRequest request) {
		var user = mapper.buildAtributes(request.name(), request.username(), request.age(), request.email(),
				passwordEncoder.encode(request.password()));
		var savedUser = repository.save(user);
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		updater.saveUserToken(savedUser, jwtToken);
		
		ServiceLogger.info("User registered: {}", request.username());
		var response = mapper.buildResponse(jwtToken, refreshToken);
		return response;
	}

	// either or username or email
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		String userIdentifier = request.username() != null ? request.username() : request.email();
		
		authManager.authenticate(
				new UsernamePasswordAuthenticationToken(userIdentifier, request.password()));

		var user = repository.findByUsername(request.username())
				.or(() -> repository.findByEmail(request.email()))
				.orElseThrow(() -> new NotFoundException("Username or email wasn't found"));
		
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		updater.revokeAllUserTokens(user);
		updater.saveUserToken(user, jwtToken);
		ServiceLogger.info("User authenticated: {}", userIdentifier);
		var response = mapper.buildResponse(jwtToken, refreshToken);
		return response;
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userInfo;
		if (authHeader == null || authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userInfo = jwtService.extractUsername(refreshToken);
		if (userInfo != null) {
			var user = repository.findByUsername(userInfo).or(() -> repository.findByEmail(userInfo))
					.orElseThrow(() -> new NotFoundException("User username or email not found"));
			if(jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				updater.revokeAllUserTokens(user);
				updater.saveUserToken(user, accessToken);
				var authResponse = mapper.buildResponse(accessToken, refreshToken);
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
				ServiceLogger.info("Access token refreshed for user {}", userInfo);
			}
		}
	}

}
