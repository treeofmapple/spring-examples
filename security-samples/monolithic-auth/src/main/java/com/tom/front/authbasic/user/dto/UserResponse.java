package com.tom.front.authbasic.user.dto;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.tom.front.authbasic.user.model.enums.Role;

public record UserResponse(
		
		UUID id,
		
		String username,
		
		String email,
		
		Integer age,
		
		Role roles,
		
		ZonedDateTime createdAt
) {

}
