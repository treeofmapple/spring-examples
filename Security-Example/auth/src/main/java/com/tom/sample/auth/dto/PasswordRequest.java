package com.tom.sample.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordRequest(
		
	    @NotBlank(message = "Current password must not be blank")
	    String currentPassword,

	    @NotBlank(message = "New password must not be blank")
	    @Size(min = 8, message = "New password must be at least 8 characters")
	    String newPassword,

	    @NotBlank(message = "Confirmation password must not be blank")
	    String confirmationPassword
) {
}
