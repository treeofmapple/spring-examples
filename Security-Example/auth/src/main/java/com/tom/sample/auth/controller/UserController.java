package com.tom.sample.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequestMapping("v1/user")
@PreAuthorize("hasRole('USER')")
// @Tag(name = "Anonymous") # Swagger
@RequiredArgsConstructor
public class UserController {

	// @PreAuthorize("hasAuthority('user:read')")
	
	// @PreAuthorize("hasAuthority('user:create')")
	
	// @PreAuthorize("hasAuthority('user:update')")
	
	// @PreAuthorize("hasAuthority('user:delete')")
	
}
