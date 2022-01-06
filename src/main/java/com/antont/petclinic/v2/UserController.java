package com.antont.petclinic.v2;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.User;
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
        Optional<User> user = userService.getLoggedInUser();
        List<Pet> pets = petService.getPetsByOwner(user.get());
        model.addAttribute("pets", pets);
        return "user";
    }
}
