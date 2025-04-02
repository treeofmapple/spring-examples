package com.pogeku.chatgpt.server.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.pogeku.chatgpt.server.exception.NotAllowedException;
import com.pogeku.chatgpt.server.exception.NotFoundException;
import com.pogeku.chatgpt.server.model.Message;
import com.pogeku.chatgpt.server.model.MessageMapper;
import com.pogeku.chatgpt.server.model.MessageRequest;
import com.pogeku.chatgpt.server.model.MessageResponse;
import com.pogeku.chatgpt.server.model.MessagesRepository;
import com.pogeku.chatgpt.server.request.TypeAIRequest;
import com.pogeku.chatgpt.server.request.TypeAIResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiService {

	@Value("${openai.api-key}")
	private String apiKey;

	@Value("${openai.api-url}")
	private String apiUrl;

	private static String AImodel = "chatgpt-4o-mini"; // user specific model selection mechanism
	
    private final WebClient.Builder webClientBuilder;
	private final MessagesRepository repository;
	private final MessageMapper mapper;
	
    public String callOpenAI(String prompt) {
    	String content = String.format("{\"model\": \"%s\", \"prompt\": \"%s\", \"max_tokens\": 100}", AImodel, prompt);
        return webClientBuilder
        		.baseUrl(apiUrl)
        		.build()
        		.post()
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(content)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        clientResponse -> Mono.error(new RuntimeException("Error calling OpenAI API"))
                    )
                .bodyToMono(String.class)
                .block();
    }
	
	public TypeAIResponse editAiType(TypeAIRequest name) {
		AImodel = name.AIType();
		return new TypeAIResponse(String.format("The ai model was changed to: %s", name.AIType()));
	}

	public MessageResponse sendMessage(MessageRequest request) { 
		try {
			String aiResponse = callOpenAI(request.messageContent());
			var message = mapper.toMessage(request);
			repository.save(message);
			var response = mapper.toMessageResponse(aiResponse, message.getData(),message.getHora());
			return response;
		} catch(NotAllowedException e) {
			log.error("Error processing message: {}", e.getMessage(), e);
			throw new NotAllowedException("Failed to process AI message.");
		}
	}

	public List<MessageResponse> getAllQuestions() {
		List<Message> messages = repository.findAll();
		if(messages.isEmpty()) {
			throw new NotFoundException(String.format("No message was found"));
		}
		return messages.stream().map(mapper::fromMessage).collect(Collectors.toList());
	}
}
