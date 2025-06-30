package com.tom.front.authbasic.user.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.front.authbasic.common.SystemUtils;
import com.tom.front.authbasic.security.JwtService;
import com.tom.front.authbasic.user.dto.AuthenticationRequest;
import com.tom.front.authbasic.user.dto.AuthenticationResponse;
import com.tom.front.authbasic.user.dto.DeleteAccountRequest;
import com.tom.front.authbasic.user.dto.PasswordUpdateRequest;
import com.tom.front.authbasic.user.dto.RegisterRequest;
import com.tom.front.authbasic.user.dto.UpdateAccountRequest;
import com.tom.front.authbasic.user.dto.UserPageResponse;
import com.tom.front.authbasic.user.dto.UserResponse;
import com.tom.front.authbasic.user.mapper.UserMapper;
import com.tom.front.authbasic.user.model.User;
import com.tom.front.authbasic.user.model.enums.Role;
import com.tom.front.authbasic.user.repository.UserRepository;
import com.tom.front.authbasic.user.repository.UserSpecification;
import com.tom.front.authbasic.user.service.utils.TokenUtils;
import com.tom.front.authbasic.user.service.utils.UserUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

	@Value("${application.page.size:10}")
	private int PAGE_SIZE;
	
	private final AuthenticationManager authManager;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository repository;
	private final UserMapper mapper;
	private final UserUtils userUtils;
	private final TokenUtils tokenUtils;
	private final SystemUtils systemUtil;
	private final JwtService jwtService;
	
	public UserResponse getCurrentUser(Principal connectedUser) {
		var user = userUtils.getAuthenticatedUser(connectedUser);
		return mapper.toResponse(user);
	}

	@Transactional(readOnly = true)
	public UserPageResponse findUserByParams(int page, String username, String email, Integer age, HttpServletRequest httpRequest) {
		Specification<User> spec = UserSpecification.findByCriteria(username, email, age);
		log.info("IP: {}, is finding params: {}", systemUtil.getRequestingClientIp(httpRequest), spec);

		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		Page<User> users = repository.findAll(spec, pageable);
		return mapper.toResponse(users);
	}
	
	@Transactional
	public AuthenticationResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
		log.info("IP: {}, is creating user.", systemUtil.getRequestingClientIp(httpRequest));
		
		userUtils.ensureUsernameAndEmailAreUnique(request.username(), request.email());
		
		if (!request.password().equals(request.confirmPassword())) {
			throw new BadCredentialsException("Passwords not matches");
		}

		var user = mapper.build(request);
		user.setPassword(passwordEncoder.encode(request.password()));
		user.setRole(Role.USER);
		var savedUser = repository.save(user);
		
		var jwtToken = jwtService.generateToken(savedUser);
		var refreshToken = jwtService.generateRefreshToken(savedUser);
		
		log.info("User registered: | Username: {}, | Email: {}", savedUser.getUsername(), savedUser.getEmail());
		var response = mapper.toResponse(jwtToken, refreshToken);
		return response;
	}

	@Transactional
	public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest httpRequest) {
		log.info("IP: {}, is authenticating user: {}", systemUtil.getRequestingClientIp(httpRequest), request.userinfo());
		
		var auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.userinfo(), request.password()));

		var user = (User) auth.getPrincipal();
		
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		
		tokenUtils.saveUserToken(user, refreshToken);
		
		log.info("IP: {}, the user: {}, has been authenticated", systemUtil.getRequestingClientIp(httpRequest), user.getUsername());
		return mapper.toResponse(jwtToken, refreshToken);
	}
	
	@Transactional
	public UserResponse editUser(UpdateAccountRequest request, Principal connectedUser, HttpServletRequest httpRequest) {
		var user = userUtils.getAuthenticatedUser(connectedUser);
		log.info("IP: {}, is being edited: {}", systemUtil.getRequestingClientIp(httpRequest), user.getUsername());
		
		userUtils.checkIfUsernameIsTakenByAnotherUser(user, request.username());
		userUtils.checkIfEmailIsTakenByAnotherUser(user, request.email());
		
		var newUser = mapper.updateUser(user, request);
		var userEdited = repository.save(newUser);
		
		log.info("IP: {}, changed the info of the user: {}, to user: {}", systemUtil.getRequestingClientIp(httpRequest), user.getUsername(), userEdited.getUsername());
		return mapper.toResponse(userEdited);
	}
	
	@Transactional
	public void changePassword(PasswordUpdateRequest request, Principal connectedUser, HttpServletRequest httpRequest) {
		var user = userUtils.getAuthenticatedUser(connectedUser);
		log.info("IP: {}, is changing password of user: {}", systemUtil.getRequestingClientIp(httpRequest), user.getUsername());
		
		if (!passwordEncoder.matches(request.currentpassword(), user.getPassword())) {
			throw new BadCredentialsException("Password not matches");
		}
		
		if (!request.newPassword().equals(request.confirmPassword())) {
			throw new BadCredentialsException("Passwords are not the same");
		}
		
		user.setPassword(passwordEncoder.encode(request.newPassword()));
		repository.save(user);
		log.info("IP: {}, changed successfully the password of user: {}", systemUtil.getRequestingClientIp(httpRequest), user.getUsername());
	}
	
	@Transactional
	public void logout(Principal connectedUser, HttpServletRequest request) {
		var user = userUtils.getAuthenticatedUser(connectedUser);
		log.info("IP: {}, is logging out: {}", systemUtil.getRequestingClientIp(request), user.getUsername());
		tokenUtils.revokeAllUserTokens(user);
	}

	@Transactional
	public void deleteMyAccount(DeleteAccountRequest request, Principal connectedUser, HttpServletRequest httpRequest) {
		var user = userUtils.getAuthenticatedUser(connectedUser);
		log.info("IP: {}, is deleting user: {}", systemUtil.getRequestingClientIp(httpRequest), user.getUsername());
		
		if(!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new BadCredentialsException("Incorrect password provided for account deletion.");
		}
		var session = httpRequest.getSession(false);
	    if (session != null) {
	        session.invalidate();
	    }

	    tokenUtils.revokeAllUserTokens(user);
		repository.deleteById(user.getId());
		log.info("The user {} has deleted their account", user.getUsername());
	}
	
}
