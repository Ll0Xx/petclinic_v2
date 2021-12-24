package com.antont.petclinic.v2.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping(path = "/login")
    public String get(){
        return "login";
    }
}
