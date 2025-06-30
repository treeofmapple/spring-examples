package com.tom.front.authbasic.user.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.tom.front.authbasic.user.dto.AuthenticationResponse;
import com.tom.front.authbasic.user.dto.RegisterRequest;
import com.tom.front.authbasic.user.dto.UpdateAccountRequest;
import com.tom.front.authbasic.user.dto.UserPageResponse;
import com.tom.front.authbasic.user.dto.UserResponse;
import com.tom.front.authbasic.user.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	User build(RegisterRequest request);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "password", ignore = true)
	User build(UpdateAccountRequest request);

	UserResponse toResponse(User user);
	
	@Mapping(source = "jwtToken", target = "accessToken")
	@Mapping(source = "refreshToken", target = "refreshToken")
	AuthenticationResponse toResponse(String jwtToken, String refreshToken);
	
	@Mapping(target = "id", ignore = true)
	User updateUser(@MappingTarget User user, UpdateAccountRequest request);
	
	List<UserResponse> toResponseList(List<User> users);
	
	default UserPageResponse toResponse(Page<User> page) {
		if(page == null) {
			return null;
		}
		List<UserResponse> content = toResponseList(page.getContent());
		return new UserPageResponse(content,
				page.getNumber(),
				page.getSize(),
				page.getTotalPages(),
				page.getTotalElements()
			);
	}
	
}
