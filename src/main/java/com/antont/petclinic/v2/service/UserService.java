package com.antont.petclinic.v2.service;

import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public Optional<User> getLoggedInUser() {
        return getByEmail(getCurrentUserEmail());
    }

    public Optional<User> getByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
