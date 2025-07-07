package com.tom.auth.monolithic.user.dto.admin;

import java.time.ZonedDateTime;

public record AdminUserResponse(
		
		String username,
		String email,
		Integer age,
		String role,
		ZonedDateTime createdAt,
		ZonedDateTime lastLogin,
		boolean accountNonLocked,
		boolean enabled
		
		) {

}
