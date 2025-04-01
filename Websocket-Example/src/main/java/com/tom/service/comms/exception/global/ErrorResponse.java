package com.tom.service.comms.exception.global;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {

}
