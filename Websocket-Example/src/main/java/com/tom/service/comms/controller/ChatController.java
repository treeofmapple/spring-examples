package com.tom.service.comms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tom.service.comms.models.ChatMessage;
import com.tom.service.comms.models.ChatNotification;
import com.tom.service.comms.service.ChatMessageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {

	private final SimpMessagingTemplate template;
	private final ChatMessageService service;
	
	@MessageMapping("/chat")
	public void processMessage(@Payload ChatMessage message) {
		ChatMessage savedMsg = service.save(message);
		template.convertAndSendToUser(
				message.getRecipientId(), 
				"/queue/messages", 
				new ChatNotification(
						savedMsg.getId(), 
						savedMsg.getSenderId(),
						savedMsg.getRecipientId(),
						savedMsg.getContent()));
		
		
	}
	
	@GetMapping("/messages/{senderId}/{recipientId}")
	public ResponseEntity<List<ChatMessage>> findChatMessages(
			@PathVariable String senderId,
			@PathVariable String recipientId
			) {
		return ResponseEntity.status(HttpStatus.OK).body(service.findChatMessages(senderId, recipientId));
	}
}
