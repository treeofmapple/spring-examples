package com.pogeku.chatgpt.server.exception;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
