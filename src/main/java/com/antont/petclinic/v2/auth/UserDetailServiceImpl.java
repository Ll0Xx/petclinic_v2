package com.antont.petclinic.v2.auth;

import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        org.springframework.security.core.userdetails.User springUser;

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User with email: " + username + " not found");
        } else {
            List<SimpleGrantedAuthority> authority = List.of(new SimpleGrantedAuthority(user.get().getRole().getName()));
            springUser = new org.springframework.security.core.userdetails.User(
                    username,
                    user.get().getPassword(),
                    authority);
        }

        return springUser;
    }
}
