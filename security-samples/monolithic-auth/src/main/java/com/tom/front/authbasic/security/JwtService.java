package com.tom.front.authbasic.security;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.tom.front.authbasic.user.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private Key signInKey;
	
    @Value("${application.security.secret-key}")
    private String secretKey;

	@Value("${application.security.expiration}")
	private Duration jwtExpiration;

	@Value("${application.security.refresh-token}")
	private Duration refreshExpiration;
	
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.signInKey = Keys.hmacShaKeyFor(keyBytes);
    }
	
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}
	
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails ) {
		return buildToken(extraClaims, userDetails, jwtExpiration.toMillis());
	}
	
	public String generateRefreshToken(UserDetails userDetails) {
		return buildToken(new HashMap<>(), userDetails, refreshExpiration.toMillis());
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails){
		final String username = extractUsername(token);
		return (username.equals(getIdentifier(userDetails))) && !isTokenExpired(token);
	}	
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
    
    private String getIdentifier(UserDetails userDetails) {
        if (userDetails instanceof User user) {
            return user.getId().toString();
        }
        return userDetails.getUsername();
    }
	
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expirationMillis) {
    	long nowMillis = System.currentTimeMillis();
    	
    	Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + expirationMillis);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(getIdentifier(userDetails))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(this.signInKey)
                .compact();
    }
	
    private Claims extractAllClaims(String token) {
        return Jwts.parser() 
                .verifyWith((SecretKey) this.signInKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
