package com.tom.service.comms.exception;

import com.tom.service.comms.exception.global.CustomGlobalException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@EqualsAndHashCode(callSuper = true)
@Data
public class InternalException extends CustomGlobalException {

	private String msg;
	
}
