package com.tom.front.authbasic.controller;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tom.front.authbasic.user.dto.AuthenticationRequest;
import com.tom.front.authbasic.user.dto.AuthenticationResponse;
import com.tom.front.authbasic.user.dto.RegisterRequest;
import com.tom.front.authbasic.user.service.TokenService;
import com.tom.front.authbasic.user.service.UserService;
import com.tom.front.authbasic.user.service.utils.CookiesUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AnonymousController {

	private final UserService userService;
    private final TokenService tokenService;
    private final CookiesUtils cookiesUtils;
    
    @Value("${application.security.cookie-name}")
    private String refreshTokenCookieName;
    
    @Value("${application.security.refresh-token.expiration}")
    private Duration refreshExpiration;

	@PostMapping(value = "/sign-up",
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
		var register = userService.register(request, httpRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(register);
	}
	
	@PostMapping(value = "/sign-in",
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request, HttpServletRequest httpRequest) {
		var authenticate = userService.authenticate(request, httpRequest);
		return ResponseEntity.status(HttpStatus.OK).body(authenticate);
	}
    
	@PostMapping(value = "/refresh-token",
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationResponse> refreshToken(
			@CookieValue(name = "${application.security.cookie-name}") String refreshToken,
			HttpServletResponse response) {
		
		AuthenticationResponse authResponse = tokenService.refreshToken(refreshToken);
		cookiesUtils.addCookie(response, refreshTokenCookieName, authResponse.refreshToken(), refreshExpiration);

		return ResponseEntity.status(HttpStatus.OK).body(authResponse);
	}
	
}
