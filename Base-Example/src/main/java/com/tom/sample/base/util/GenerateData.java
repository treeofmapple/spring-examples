package com.tom.sample.base.util;

import org.springframework.context.annotation.Configuration;

import com.tom.sample.base.model.Task;
import com.tom.sample.base.model.User;

@Configuration
public class GenerateData implements DatagenUtil {

	protected User genUser() {
		User user = new User();

		
		/*
		
        String uniqueName = generateUniqueProductName();
        pro.setName(uniqueName);

		pro.setQuantity(getRandomInt(10, 1000));

		pro.setPrice(BigDecimal.valueOf(getRandomDouble(10, 120)));
		
		pro.setManufacturer(faker.company().name());

		// 90 % chance
		boolean isActive = getRandomNumber(100) < 90;
		pro.setActive(isActive);

		return pro;
		
		*/
		return null;
	}
	
	protected Task genTask() {
		Task task = new Task();
		
		
		
		return null;
	}

    private String generateUniqueProductName() {
        String name;
        do {
            name = faker.commerce().productName();
        } while (generatedNames.contains(name));

        generatedNames.add(name);
        return name;
    }
	
	protected double getRandomDouble(int min, int max) {
		return loc.nextDouble(min, max);
	}

	protected int getRandomInt(int min, int max) {
		return loc.nextInt(min, max);
	}

	protected int getRandomNumber(int value) {
		return loc.nextInt(value);
	}
}

