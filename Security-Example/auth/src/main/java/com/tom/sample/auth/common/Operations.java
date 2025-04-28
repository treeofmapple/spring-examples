package com.tom.sample.auth.common;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class Operations {

	public void addCookie(HttpServletResponse response, String name, String value, long maxAge) {
	    var cookie = new Cookie(name, value);
	    cookie.setHttpOnly(true);
	    cookie.setSecure(true);
	    cookie.setPath("/");
	    int maxAgeSeconds = (int) (maxAge / 1000);
	    cookie.setMaxAge(maxAgeSeconds);
	    response.addCookie(cookie);
	}
	
	public String getCookieValue(HttpServletRequest request, String name) {
	    if (request.getCookies() != null) {
	        for (Cookie cookie : request.getCookies()) {
	            if (cookie.getName().equals(name)) {
	                return cookie.getValue();
	            }
	        }
	    }
		return null;
	}
	
	public void clearCookie(HttpServletResponse response, String string) {
		Cookie cookie = new Cookie(string, "");
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}
	
	public long parseDuration(String duration) {
	    long millis = 0L;
	    String regex = "(\\d+)([smhdwSMHDW])";
	    var matcher = Pattern.compile(regex).matcher(duration);
	    while (matcher.find()) {
	        long value = Long.parseLong(matcher.group(1));
	        switch (matcher.group(2).toUpperCase()) {
	            case "S" -> millis += value * 1000;
	            case "M" -> millis += value * 60 * 1000;
	            case "H" -> millis += value * 60 * 60 * 1000;
	            case "D" -> millis += value * 24 * 60 * 60 * 1000;
	            case "W" -> millis += value * 7 * 24 * 60 * 60 * 1000;
	            default -> throw new IllegalArgumentException("Invalid time unit in duration: " + matcher.group(2));
	        }
	    }
	    return millis;
	}

}
