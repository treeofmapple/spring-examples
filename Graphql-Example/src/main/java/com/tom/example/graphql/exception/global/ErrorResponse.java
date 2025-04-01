package com.tom.example.graphql.exception.global;

import java.util.Map;

public record ErrorResponse(Map<String, String> errors) {
}
