package com.tom.front.authbasic.user.service.utils;

import org.springframework.stereotype.Component;

import com.tom.front.authbasic.exception.NotFoundException;
import com.tom.front.authbasic.user.model.Token;
import com.tom.front.authbasic.user.model.User;
import com.tom.front.authbasic.user.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenUtils {

	private final TokenRepository tokenRepository;
	
	public void saveUserToken(User user, String jwtToken) {
		var token = Token.builder()
		        .user(user)
		        .token(jwtToken)
//		        .tokenType(TokenType.BEARER)
		        .expired(false)
		        .revoked(false)
		        .build();
		tokenRepository.save(token);
	}

	public void revokeAllUserTokens(User user) {
		var validUser = tokenRepository.findAllValidTokensByUserId(user.getId());
		if (validUser.isEmpty()) {
			throw new NotFoundException("No active tokens found for user");
		}
		validUser.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUser);
	}
	
}
