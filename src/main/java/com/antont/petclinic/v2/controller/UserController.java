package com.antont.petclinic.v2.controller;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.User;
import com.antont.petclinic.v2.dto.PetDto;
import com.antont.petclinic.v2.service.IssueService;
import com.antont.petclinic.v2.service.PetService;
import com.antont.petclinic.v2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final PetService petService;
    private final IssueService issueService;

    public UserController(UserService userService, PetService petService, IssueService issueService) {
        this.userService = userService;
        this.petService = petService;
        this.issueService = issueService;
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
        model.addAttribute("issues", issueService.findIssuesForUser(user));
        return "user";
    }
}
