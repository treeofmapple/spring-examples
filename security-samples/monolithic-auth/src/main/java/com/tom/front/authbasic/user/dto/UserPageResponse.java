package com.tom.front.authbasic.user.dto;

import java.util.List;

public record UserPageResponse(
		
		List<UserResponse> content,
		int page,
		int size,
		long totalPages,
		long totalElements
		
		
		) {

}
