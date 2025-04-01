package com.tom.service.comms.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tom.service.comms.models.ChatRoom;
import com.tom.service.comms.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository repository;
	
	public Optional<String> getChatRoomId(String senderId, String recipientId, boolean newRoom) {
		var chatroom = repository.findBySenderIdAndRecipientId(senderId, recipientId)
				.map(ChatRoom::getChatId)
				.or(() -> {
			if(newRoom) {
				var chatId = createChat(senderId, recipientId);
				return Optional.of(chatId);
			}
			return Optional.empty();
		}); 
		return chatroom;
	}
	
	private String createChat(String senderId, String recipientId) {
		var chatId = String.format("%s_%s", senderId, recipientId);
		
		ChatRoom senderRecipient = 
				ChatRoom.builder()
				.chatId(chatId)
				.senderId(senderId)
				.recipientId(recipientId)
				.build();
		
		ChatRoom recipientSender =
				ChatRoom.builder()
				.chatId(chatId)
				.senderId(senderId)
				.recipientId(recipientId)
				.build();
		
		repository.save(senderRecipient);
		repository.save(recipientSender);
		return chatId;
	}
	
}
