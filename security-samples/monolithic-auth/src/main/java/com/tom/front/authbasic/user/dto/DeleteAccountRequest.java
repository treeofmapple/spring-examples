package com.tom.front.authbasic.user.dto;

import jakarta.validation.constraints.NotBlank;

public record DeleteAccountRequest (
		
	    @NotBlank(message = "Password must not be blank")
		String password
		
		) {

}
