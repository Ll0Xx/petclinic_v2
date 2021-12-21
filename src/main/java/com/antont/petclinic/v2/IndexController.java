package com.antont.petclinic.v2;

import com.antont.petclinic.v2.db.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final PetService petService;

    public IndexController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping(path = "/")
    public String get(Model model){
        model.addAttribute("pets", petService.getPets());
        return "index";
    }
}
