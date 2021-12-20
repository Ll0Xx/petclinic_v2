package com.antont.petclinic_v2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping(path = "/yay")
    public String get(){
        return "yay";
    }
}
