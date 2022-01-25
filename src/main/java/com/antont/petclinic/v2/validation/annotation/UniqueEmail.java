package com.antont.petclinic.v2.validation.annotation;

import com.antont.petclinic.v2.validation.validator.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface UniqueEmail {
    String message() default "{validation.email}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
