package com.antont.petclinic.v2.rest;

import com.antont.petclinic.v2.dto.PetDto;
import com.antont.petclinic.v2.service.PetService;
import com.antont.petclinic.v2.validation.ValidationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;

@RestController
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
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
