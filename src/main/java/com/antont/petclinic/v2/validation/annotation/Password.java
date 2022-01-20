package com.antont.petclinic.v2.validation.annotation;

import com.antont.petclinic.v2.validation.validator.PasswordConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordConstraintValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Password {

    String message() default "Password must contains digit, lower and upper case letter, special characters and no whitespace allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
