package com.tom.front.authbasic.user.service.utils;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.tom.front.authbasic.exception.AlreadyExistsException;
import com.tom.front.authbasic.exception.NotFoundException;
import com.tom.front.authbasic.user.model.User;
import com.tom.front.authbasic.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserUtils {

	private final UserRepository repository;
	
    public User getAuthenticatedUser(Principal connectedUser) {
        if (connectedUser == null) {
            throw new IllegalStateException("Cannot get user from a null Principal.");
        }
        if (!(connectedUser instanceof UsernamePasswordAuthenticationToken authToken)) {
            throw new IllegalStateException(
                "Unexpected Principal type: " + connectedUser.getClass().getName()
            );
        }
        Object principal = authToken.getPrincipal();
        if (!(principal instanceof User user)) {
            throw new IllegalStateException(
                "Principal did not contain a User object. Found: " + principal.getClass().getName()
            );
        }
        return user;
    }
    
    public User findUserByIdentifier(String identifier) {
    	return repository.findByUsername(identifier).or(() -> repository.findByEmail(identifier))
				.orElseThrow(() -> new NotFoundException("User username or email not found"));
    }
    
    public void ensureUsernameAndEmailAreUnique(String username, String email) {
        if (repository.existsByUsername(username)) {
            throw new AlreadyExistsException("Username is already taken: " + username);
        }
        
        if (repository.existsByEmail(email)) {
            throw new AlreadyExistsException("Email is already in use: " + email);
        }
    }
	
    public void checkIfEmailIsTakenByAnotherUser(User currentUser, String newEmail) {
        repository.findByEmail(newEmail).ifPresent(existentUser -> {
            if (!existentUser.getId().equals(currentUser.getId())) {
                throw new AlreadyExistsException("Email is already in use by another account: " + newEmail);
            }
        });
    }

    public void checkIfUsernameIsTakenByAnotherUser(User currentUser, String newUsername) {
        repository.findByUsername(newUsername).ifPresent(existentUser -> {
            if (!existentUser.getId().equals(currentUser.getId())) {
                throw new AlreadyExistsException("Username is already taken by another account: " + newUsername);
            }
        });
    }
	
}
