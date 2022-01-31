package com.antont.petclinic.v2.validation.validator;

import com.antont.petclinic.v2.db.repository.DoctorRepository;
import com.antont.petclinic.v2.validation.annotation.Doctor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigInteger;

public class DoctorValidator implements ConstraintValidator<Doctor, BigInteger> {

    private final DoctorRepository doctorRepository;

    public DoctorValidator(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public void initialize(Doctor constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(BigInteger value, ConstraintValidatorContext context) {
        return doctorRepository.existsById(value);
    }
}
