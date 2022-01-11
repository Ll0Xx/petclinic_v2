package com.antont.petclinic.v2.rest;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.dto.PetDto;
import com.antont.petclinic.v2.service.PetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@RestController
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping(path = "/user/pet/create")
    public ResponseEntity<Pet> update(@ModelAttribute PetDto dto) {
        return ResponseEntity.ok().body(petService.handlePetRequest(dto));
    }

    @DeleteMapping(path = "/user/pet/delete/{id}")
    public ResponseEntity<BigInteger> delete(@PathVariable("id") BigInteger id) {
        return ResponseEntity.ok().body(petService.deletePet(id));
    }
}
