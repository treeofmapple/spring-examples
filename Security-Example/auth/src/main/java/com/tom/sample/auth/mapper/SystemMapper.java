package com.tom.sample.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SystemMapper {
	SystemMapper INSTANCE = Mappers.getMapper(SystemMapper.class);
	
	// @Mapping(source = "", target = "")
	// Object build();
	
	
	
	
	
	
	
	
	
	
}
