package com.antont.petclinic.v2.controller;

import com.antont.petclinic.v2.auth.dto.CreateUserDto;
import com.antont.petclinic.v2.db.repository.DoctorSpecializationRepository;
import com.antont.petclinic.v2.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AuthController {

    private final AuthService authController;
    private final DoctorSpecializationRepository doctorSpecializationRepository;

    public AuthController(AuthService authController, DoctorSpecializationRepository doctorSpecializationRepository) {
        this.authController = authController;
        this.doctorSpecializationRepository = doctorSpecializationRepository;
    }

    @GetMapping(path = "/login")
    public String login(Model model){
        model.addAttribute("createUserDto", new CreateUserDto());
        model.addAttribute("doctorSpecializationsList", doctorSpecializationRepository.findAll());
        return "login";
    }

    @PostMapping("/process_register")
    public String processRegister(CreateUserDto dto) {
        authController.create(dto);
        return "redirect:/login?tab=1";
    }
}
