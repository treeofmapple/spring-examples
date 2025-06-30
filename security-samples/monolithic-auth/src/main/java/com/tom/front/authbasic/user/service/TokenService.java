package com.tom.front.authbasic.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tom.front.authbasic.exception.InvalidTokenException;
import com.tom.front.authbasic.security.JwtService;
import com.tom.front.authbasic.user.dto.AuthenticationResponse;
import com.tom.front.authbasic.user.mapper.UserMapper;
import com.tom.front.authbasic.user.repository.TokenRepository;
import com.tom.front.authbasic.user.service.utils.TokenUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenService {

	private final TokenRepository tokenRepository;
	private final UserMapper mapper;
	private final JwtService jwtService;
	private final TokenUtils tokenUtils;
	
	@Transactional
	public AuthenticationResponse refreshToken(String providedRefreshToken)  {

        var storedToken = tokenRepository.findByToken(providedRefreshToken)
                .orElseThrow(() -> new InvalidTokenException("Refresh token not found."));

        if (storedToken.isRevoked()) {
            log.warn("SECURITY ALERT: Attempted re-use of revoked refresh token for user {}. Revoking all tokens for this user.", storedToken.getUser().getUsername());
            tokenUtils.revokeAllUserTokens(storedToken.getUser()); 
            
            throw new InvalidTokenException("Refresh token has been compromised.");
        }

        if (storedToken.isExpired()) {
            throw new InvalidTokenException("Refresh token is expired.");
        }

        var user = storedToken.getUser();
        var newAccessToken = jwtService.generateToken(user);
        var newRefreshToken = jwtService.generateRefreshToken(user);

        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);

        tokenUtils.saveUserToken(user, newRefreshToken);

        log.info("Token rotated for user {}", user.getUsername());
        return mapper.toResponse(newAccessToken, newRefreshToken);

	}
	
	
}
