package com.tom.service.comms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tom.service.comms.models.ChatMessage;
import com.tom.service.comms.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatMessageRepository repository;
	private final ChatRoomService service;
	
	public ChatMessage save(ChatMessage message) {
		var chatId = service.getChatRoomId(message.getSenderId(), message.getRecipientId(), true)
				.orElseThrow();
		message.setChatId(chatId);
		repository.save(message);
		return message;
	}
	
	public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
		var chatId = service.getChatRoomId(senderId, recipientId, false)
				.map(repository::findByChatId)
				.orElse(new ArrayList<>());
		return chatId;
	}
}
