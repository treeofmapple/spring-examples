package com.pogeku.chatgpt.server.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MessageRequest(
		
		@NotBlank(message = "Não pode ficar vazio")
		@NotNull(message = "Não pode ficar sem conteúdo")
		String messageContent) {

}
