package com.tom.front.authbasic.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tom.front.authbasic.user.repository.TokenRepository;
import com.tom.front.authbasic.user.service.utils.CookiesUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final TokenRepository tokenRepository;
	private final JwtService jwtService;
	private final CookiesUtils cookieUtils;
	
	@Override 
	protected void doFilterInternal(@NonNull HttpServletRequest request, 
									@NonNull HttpServletResponse response, 
									@NonNull FilterChain filterChain)
			throws ServletException, IOException {
		
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
		    filterChain.doFilter(request, response);
		    return;
		}
		
		String jwt = extractTokenFromRequest(request);

	    if (jwt == null) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    final String identifier = jwtService.extractUsername(jwt);
	    
	    if (identifier == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(identifier);
		    boolean isTokenRevoked = tokenRepository.findByToken(jwt)
		            .map(token -> token.isExpired() && token.isRevoked())
		            .orElse(true);
			
		    if (!jwtService.isTokenValid(jwt, userDetails) && !isTokenRevoked) {
		    	UsernamePasswordAuthenticationToken authToken = 
			            new UsernamePasswordAuthenticationToken(
			            		userDetails, 
			            		null, 
			            		userDetails.getAuthorities());
		    	authToken.setDetails(new WebAuthenticationDetailsSource()
			    		.buildDetails(request));
			    SecurityContextHolder.getContext().setAuthentication(authToken);
		    	
		    }

	        filterChain.doFilter(request, response);
	    }
	}
	
    private String extractTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return cookieUtils.getCookieValue(request, "access_token").orElse(null);
    }
	
}
