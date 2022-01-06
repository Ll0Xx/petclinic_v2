package com.antont.petclinic.v2.auth;

import com.antont.petclinic.v2.auth.dto.CreateUserDto;
import com.antont.petclinic.v2.db.entity.Role;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.db.repository.RoleRepository;
import com.antont.petclinic.v2.db.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping(path = "/login")
    public String login(Model model){
        model.addAttribute("createUserDto", new CreateUserDto());
        return "login";
    }

    @PostMapping("/process_register")
    public String processRegister(CreateUserDto dto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(encodedPassword);

        Role a = getUserRole().get();
        user.setRole(a);

        userRepository.save(user);

        return "redirect:/login?tab=1";
    }

    private Optional<Role> getUserRole(){
        return roleRepository.findByName("ROLE_USER");
    }
}
