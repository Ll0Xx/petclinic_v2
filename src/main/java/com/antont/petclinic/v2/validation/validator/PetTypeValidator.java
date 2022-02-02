package com.antont.petclinic.v2.validation.validator;

import com.antont.petclinic.v2.db.repository.PetTypeRepository;
import com.antont.petclinic.v2.db.repository.UserRepository;
import com.antont.petclinic.v2.validation.annotation.PetType;
import com.antont.petclinic.v2.validation.annotation.UniqueEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigInteger;

public class PetTypeValidator implements ConstraintValidator<PetType, BigInteger> {

    private final PetTypeRepository petTypeRepository;

    public PetTypeValidator(PetTypeRepository petTypeRepository) {
        this.petTypeRepository = petTypeRepository;
    }

    @Override
    public void initialize(PetType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigInteger value, ConstraintValidatorContext context) {
        if(value == null){
            return false;
        }
        return petTypeRepository.existsById(value);
    }
}
