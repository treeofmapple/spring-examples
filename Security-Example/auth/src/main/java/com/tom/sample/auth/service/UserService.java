package com.tom.sample.auth.service;

import java.security.Principal;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tom.sample.auth.dto.PasswordRequest;
import com.tom.sample.auth.mapper.SystemMapper;
import com.tom.sample.auth.model.User;
import com.tom.sample.auth.repository.TokenRepository;
import com.tom.sample.auth.repository.UserRepository;
import com.tom.sample.auth.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final JwtService jwtService;
	private final SystemMapper mapper;
	private final AuthenticationManager authManager;
	private final PasswordEncoder passwordEncoder;
	private final TokenRepository tokenRepository;
	private final UserRepository userRepository;

	public void changePassword(PasswordRequest request, Principal connectedUser) {
		var user = (User) 
				((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
		
		if(!passwordEncoder.matches(null, null)) {
			
		}
		
		if(!request.newPassword().equals(request.confirmationPassword())) {
			
		}
		
		user.setPassword(passwordEncoder.encode(request.newPassword()));
		userRepository.save(user);
	}
	
	
	
}
