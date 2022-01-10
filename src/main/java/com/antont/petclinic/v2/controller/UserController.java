package com.antont.petclinic.v2.controller;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.dto.PetDto;
import com.antont.petclinic.v2.service.PetService;
import com.antont.petclinic.v2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final PetService petService;

    public UserController(UserService userService, PetService petService) {
        this.userService = userService;
        this.petService = petService;
    }

    @GetMapping(path = "/user")
    public String get(Model model) {
        User user = userService.getLoggedInUser();
        List<Pet> pets = petService.getPetsByOwner(user);
        model.addAttribute("pets", pets);

        if (!model.containsAttribute("pet")) {
            model.addAttribute("pet", new PetDto());
        }

        model.addAttribute("petTypes", petService.getPetTypes());
        return "user";
    }
}
