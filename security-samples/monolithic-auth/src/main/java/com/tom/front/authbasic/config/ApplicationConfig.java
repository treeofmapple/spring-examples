package com.tom.front.authbasic.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tom.front.authbasic.auditing.ApplicationAuditAware;
import com.tom.front.authbasic.exception.NotFoundException;
import com.tom.front.authbasic.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

	private final UserRepository repository;

	@Bean
	UserDetailsService userDetailsService() {
		 return username -> repository.findByUsername(username)
	                .or(() -> repository.findByEmail(username))
        .orElseThrow(() -> new NotFoundException("User not found with identifier: " + username));
	}
	
	@Bean
	AuditorAware<UUID> auditorAware() {
		return new ApplicationAuditAware();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		
		// return new BCryptPasswordEncoder(12);
		
		return new Argon2PasswordEncoder(
				16, // saltLength
				32, // hashLength
				1, // parallelism
				19456, // memory
				2 // iterations
		);
	}
	
}
