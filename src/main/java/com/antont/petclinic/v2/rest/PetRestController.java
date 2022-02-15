package com.antont.petclinic.v2.rest;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.dto.PetDto;
import com.antont.petclinic.v2.service.PetService;
import com.antont.petclinic.v2.service.utils.ServiceUtils;
import com.antont.petclinic.v2.validation.ValidationResult;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Optional;

@RestController
public class PetRestController {

    private final PetService petService;

    public PetRestController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping(path = "/user/pet")
    public Page<Pet> getPetsPaged(@RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size,
                                  @RequestParam("sort") Optional<String> sort,
                                  @RequestParam("dir") Optional<String> direction) {
        return petService.getPetsPaged(page, size, sort, direction);
    }

    @GetMapping(path = "/user/pet/latest")
    public Page<Pet> getPetsPaged() {
        return petService.getPetsLastPage();
    }

    @PostMapping(path = "/user/pet/create")
    public ValidationResult create(@RequestBody @Valid PetDto dto, BindingResult bindingResult) {
        return ServiceUtils.generateValidationResult(bindingResult, validationResult ->
                validationResult.setResult(petService.create(dto)));
    }

    @PostMapping(path = "/user/pet/update")
    public ValidationResult update(@RequestBody @Valid PetDto dto, BindingResult bindingResult) {
        return ServiceUtils.generateValidationResult(bindingResult, validationResult ->
                validationResult.setResult(petService.update(dto)));
    }

    @DeleteMapping(path = "/user/pet/delete/{id}")
    public ResponseEntity<BigInteger> delete(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok().body(petService.deletePet(id));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());

        }
    }
}
