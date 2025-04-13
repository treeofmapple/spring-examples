package com.tom.sample.base.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tom.sample.base.repository.TaskRepository;
import com.tom.sample.base.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SystemStarts implements CommandLineRunner {

	private static int QUANTITY = 50;
	
	private final GenerateData data;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	
    @Override
    public void run(String... args) throws Exception {
    	taskRepository.deleteAll();
    	userRepository.deleteAll();
		for(int i = 0; i <= QUANTITY; i++) {
			var gen1 = data.genUser();
			var gen2 = data.genTask();
			userRepository.save(gen1);
			taskRepository.save(gen2);
		}
	}
}
