package com.antont.petclinic.v2.controller;

import com.antont.petclinic.v2.auth.dto.CreateUserDto;
import com.antont.petclinic.v2.db.repository.DoctorSpecializationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final DoctorSpecializationRepository doctorSpecializationRepository;

    public AuthController(DoctorSpecializationRepository doctorSpecializationRepository) {
        this.doctorSpecializationRepository = doctorSpecializationRepository;
    }

    @GetMapping(path = "/login")
    public String login(Model model){
        model.addAttribute("createUserDto", new CreateUserDto());
        model.addAttribute("doctorSpecializationsList", doctorSpecializationRepository.findAll());
        return "login";
    }
}
