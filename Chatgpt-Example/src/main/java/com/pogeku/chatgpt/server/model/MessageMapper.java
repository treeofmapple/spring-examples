package com.pogeku.chatgpt.server.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

@Service
public class MessageMapper {

	public Message toMessage(MessageRequest request) {
		if (request == null) {
			return null;
		}

		return Message.builder().messageContent(request.messageContent()).build();
	}

	public MessageResponse toMessageResponse(String message, LocalDate data, LocalTime hora) {
		return new MessageResponse(message, data, hora); // Fix there later to make message sanitization
	}
	
	public MessageResponse fromMessage(Message message) {
		return new MessageResponse(message.getMessageContent(), message.getData(), message.getHora());
	}
}
