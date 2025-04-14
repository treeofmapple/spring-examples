package com.tom.sample.auth.dto;

public record AuthenticationRequest(
		
		String name,
		
		String username,
		
		String email,
		
		String password
		
) {
}


//Fix Validations
