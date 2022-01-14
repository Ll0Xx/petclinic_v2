package com.antont.petclinic.v2.controller;

import com.antont.petclinic.v2.db.entity.Issue;
import com.antont.petclinic.v2.service.DoctorService;
import com.antont.petclinic.v2.service.IssueService;
import com.antont.petclinic.v2.service.PetService;
import com.antont.petclinic.v2.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class DoctorController {

    private final UserService userService;
    private final IssueService issueService;
    private final PetService petService;
    private final DoctorService doctorService;

    public DoctorController(UserService userService, IssueService issueService, PetService petService,
                            DoctorService doctorService) {
        this.userService = userService;
        this.issueService = issueService;
        this.petService = petService;
        this.doctorService = doctorService;
    }

    @GetMapping(path = "/doctor")
    public String get(Model model) {
        model.addAttribute("issuesList", issueService.findAllIssues());
        model.addAttribute("pets", petService.getPets());
        model.addAttribute("doctors", doctorService.getDoctors());
        model.addAttribute("issue", new Issue());
        return "doctor";
    }
}
