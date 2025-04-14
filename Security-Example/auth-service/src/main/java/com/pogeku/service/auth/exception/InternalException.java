package com.pogeku.service.auth.exception;

import com.pogeku.service.auth.exception.global.CustomGlobalException;

import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
public class InternalException extends CustomGlobalException {
	
	public InternalException(String msg) {
		super(msg);
	}

	public InternalException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
