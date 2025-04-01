package com.tom.example.graphql.exception.global;

@SuppressWarnings("serial")
public abstract class CustomGlobalException extends RuntimeException {
	public abstract String getMsg();
}
