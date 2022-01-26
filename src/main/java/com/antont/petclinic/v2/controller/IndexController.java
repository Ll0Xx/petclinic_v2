package com.antont.petclinic.v2.controller;

import com.antont.petclinic.v2.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final DoctorService doctorService;

    public IndexController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping(path = "/")
    public String get(Model model){
        model.addAttribute("doctors", doctorService.getFirst10Doctors());
        return "index";
    }
}
