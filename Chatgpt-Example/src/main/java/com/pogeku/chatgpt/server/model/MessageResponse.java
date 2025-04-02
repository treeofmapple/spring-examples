package com.pogeku.chatgpt.server.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record MessageResponse(

		String aiResponse,

		LocalDateTime dataehora) {

	public MessageResponse(String messageContent, LocalDate data, LocalTime hora) {
		this(messageContent, LocalDateTime.of(data, hora));
	}
}
