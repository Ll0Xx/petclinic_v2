package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    Logger log = LoggerFactory.getLogger(PetService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public User getLoggedInUser() {
        return getByEmail(getCurrentUserEmail());
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.error("Failed to find, user type with email " + email + " not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,String.format("User with email: %s not found", email));
        });
    }
}
