package com.antont.petclinic.v2.rest;

import com.antont.petclinic.v2.dto.PetDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetController {

    @PostMapping(path = "/pet")
    public String updatePet(@RequestBody PetDto dto){
        return "";
    }
}
