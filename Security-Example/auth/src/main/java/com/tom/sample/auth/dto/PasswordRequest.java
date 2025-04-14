package com.tom.sample.auth.dto;

public record PasswordRequest(
		
		
		String currentPassword,
		
		
		String newPassword,
		
		
		String confirmationPassword
) {
}

//Fix Validations
