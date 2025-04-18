package com.tom.sample.auth.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tom.sample.auth.model.User;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class GenerateData implements DatagenUtil {

	private final PasswordEncoder passwordEncoder;
	
	protected User datagen() {
		User user = new User();
		
		user.setName(generateUniqueName());
		
		user.setUsername(faker.internet().username());
		
		user.setEmail(faker.internet().emailAddress());
		
		user.setAge(getRandomInt(isAtributesMet(60) ? 19 : 41, isAtributesMet(20) ? 41 : 59));
		
		user.setPassword(passwordEncoder.encode(faker.internet().password()));
		
		return user;
	}
	
    private String generateUniqueName() {
        String name;
        do {
            name = faker.name().fullName();
        } while (generatedNames.contains(name));

        generatedNames.add(name);
        return name;
    }
	
	protected double getRandomDouble(double min, double max) {
	    if (max <= min) {
	        return min;
	    }
		return loc.nextDouble(min, max);
	}

	protected int getRandomInt(int min, int max) {
	    if (max <= min) {
	        return min;
	    }
		return loc.nextInt(min, max);
	}

	protected boolean isAtributesMet(int atribute) {
		return loc.nextInt(100) < atribute;
	} 
}


