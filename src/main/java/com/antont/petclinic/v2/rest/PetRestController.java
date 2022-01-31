package com.antont.petclinic.v2.rest;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.dto.PetDto;
import com.antont.petclinic.v2.service.PetService;
import com.antont.petclinic.v2.validation.ValidationResult;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Optional;

@RestController
public class PetRestController {

    private final PetService petService;

    public PetRestController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping(path = "/user/pets")
    public Page<Pet> getPetsPaged(@RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size){
        return petService.getPetsPaged(page, size);
    }

    @PostMapping(path = "/user/pet/create")
    public ValidationResult update(@ModelAttribute @Valid PetDto dto, BindingResult bindingResult) {
        ValidationResult result = new ValidationResult();
        if(bindingResult.hasErrors()){
            result.setSuccess(false);
            result.setErrors(bindingResult.getAllErrors());
        }else{
            result.setSuccess(true);
            result.setResult(petService.handlePetRequest(dto));
        }
        return result;
    }

    @DeleteMapping(path = "/user/pet/delete/{id}")
    public ResponseEntity<BigInteger> delete(@PathVariable("id") BigInteger id) {
        return ResponseEntity.ok().body(petService.deletePet(id));
    }
}
