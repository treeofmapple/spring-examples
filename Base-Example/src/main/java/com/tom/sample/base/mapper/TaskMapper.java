package com.tom.sample.base.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@Mapper(componentModel="spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

	TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);
	
	
	
	
	
}
