package com.antont.petclinic.v2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping(path = "/user")
    public String get(Model model){
        User userPrincipal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.warn("User Principal: " + userPrincipal);
        model.addAttribute("userPrincipal", userPrincipal.getUsername());
        return "user";
    }
}
