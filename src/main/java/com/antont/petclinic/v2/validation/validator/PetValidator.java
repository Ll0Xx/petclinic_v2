package com.antont.petclinic.v2.validation.validator;

import com.antont.petclinic.v2.db.repository.PetRepository;
import com.antont.petclinic.v2.validation.annotation.Pet;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigInteger;

public class PetValidator implements ConstraintValidator<Pet, BigInteger> {

    private final PetRepository petRepository;

    public PetValidator(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public void initialize(Pet constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigInteger value, ConstraintValidatorContext context) {
        return petRepository.existsById(value);
    }
}
