package com.antont.petclinic.v2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping(path = "/yay")
    public String get(@RequestParam(name = "a") String a){
        return "yay: " + a;
    }
}
