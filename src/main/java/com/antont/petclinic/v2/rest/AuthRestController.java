package com.antont.petclinic.v2.rest;

import com.antont.petclinic.v2.auth.dto.CreateUserDto;
import com.antont.petclinic.v2.service.AuthService;
import com.antont.petclinic.v2.validation.ValidationResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/process_register")
    public ValidationResult processRegister(@Valid CreateUserDto dto, BindingResult bindingResult) {
        ValidationResult validationResult = new ValidationResult();
        if (bindingResult.hasErrors()) {
            validationResult.setSuccess(false);
            validationResult.setErrors(bindingResult.getAllErrors());
        } else {
            authService.create(dto);
            validationResult.setSuccess(true);
        }

        return  validationResult;
    }
}
