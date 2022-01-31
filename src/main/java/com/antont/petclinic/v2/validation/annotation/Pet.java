package com.antont.petclinic.v2.validation.annotation;

import com.antont.petclinic.v2.validation.validator.PetValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PetValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Pet {
    String message() default "{validation.pet-not-exist}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
