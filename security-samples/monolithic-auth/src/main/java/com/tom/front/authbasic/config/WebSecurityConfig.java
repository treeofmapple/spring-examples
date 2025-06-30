package com.tom.front.authbasic.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import com.tom.front.authbasic.exception.global.AuthEntryPointJwt;
import com.tom.front.authbasic.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    @Value("${application.security.whitelist}")
    private String[] whiteListUrls;
    
	private final AuthEntryPointJwt unauthorizedHandler;
	private final JwtAuthenticationFilter filter;
	private final AuthenticationProvider provider;
	private final LogoutHandler logoutHandler;
	private final CorsConfigurationSource corsConfigurationSource;
	
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName(null);
    	
    	http
    		.csrf(csrf -> csrf 
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .csrfTokenRequestHandler(requestHandler)
            )
    		.headers(headers -> headers
			    .contentSecurityPolicy(csp -> csp.policyDirectives("script-src 'self'"))
			    .frameOptions(frame -> frame.sameOrigin())
			)
    		.cors(cors -> cors.configurationSource(corsConfigurationSource))
    		.exceptionHandling(exception -> 
    				exception.authenticationEntryPoint(unauthorizedHandler))
    		.sessionManagement(session ->
    				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
    		.authorizeHttpRequests(auth -> auth
    				.requestMatchers(whiteListUrls).permitAll()
    				.anyRequest().authenticated()
    		)
    		.authenticationProvider(provider)
    		.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout 
            		.logoutUrl("/v1/auth/logout")
        			.addLogoutHandler(logoutHandler)
        			.logoutSuccessHandler((request, response, authentication) -> 
        			SecurityContextHolder.clearContext()
                )
        );
    	
    	return http.build();
	}
	
}
