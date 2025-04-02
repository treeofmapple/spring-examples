package com.pogeku.chatgpt.server.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pogeku.chatgpt.server.exception.NotAllowedException;
import com.pogeku.chatgpt.server.model.MessageRequest;
import com.pogeku.chatgpt.server.model.MessageResponse;
import com.pogeku.chatgpt.server.request.TypeAIRequest;
import com.pogeku.chatgpt.server.request.TypeAIResponse;
import com.pogeku.chatgpt.server.service.OpenAiService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/openai")
@RequiredArgsConstructor
public class OpenAiController {

	private final OpenAiService service;

	/**
	 * Updates the AI's name.
	 * 
	 * @param Select the ai type
	 * @return the updated Ai type
	 * 
	 */

	@PutMapping("/edit")
	public ResponseEntity<TypeAIResponse> editAiType(@RequestBody @Valid TypeAIRequest name) {
		log.info("Recieving request to edit the AI Name {} ");
		try {
			var aiType = service.editAiType(name);
			return ResponseEntity.status(HttpStatus.OK).body(aiType);
		} catch (NotAllowedException e) {
			log.error("Error updating the AI type.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Processes a question to the AI and returns the response.
	 *
	 * @param request the message the user wanna do to the ai
	 * @return the AI's response based on the ai type
	 */

	@PostMapping("/question")
	public ResponseEntity<MessageResponse> questionAi(@RequestBody @Valid MessageRequest request) {
		log.info("Question for the AI: {} ");
		try {
			var messages = service.sendMessage(request);
			return ResponseEntity.status(HttpStatus.CREATED).body(messages);
        } catch (NotAllowedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
	}

	/**
	 * Retrieves the last messages sent to the AI.
	 *
	 * @return the list of last messages
	 */

	@GetMapping("/last")
	public ResponseEntity<List<MessageResponse>> lastMessages() {
		log.info("Fecthing last messages from AI");
		try {
			var allQuestions = service.getAllQuestions();
			return ResponseEntity.status(HttpStatus.OK).body(allQuestions);
		} catch (NotAllowedException e) {
			log.error("Error retrieving last messages.", e);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

}
