package com.tom.example.graphql.exception;

import com.tom.example.graphql.exception.global.CustomGlobalException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
@Data
public class InternalException extends CustomGlobalException {

	private final String msg;
	
}
