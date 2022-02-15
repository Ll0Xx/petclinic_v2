package com.antont.petclinic.v2.service.utils;

import com.antont.petclinic.v2.validation.ValidationResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Consumer;

public class ServiceUtils {

    public static ValidationResult generateValidationResult(BindingResult bindingResult, Consumer<ValidationResult> consumer) {
        try {
            ValidationResult result = new ValidationResult();
            if (bindingResult.hasErrors()) {
                result.setSuccess(false);
                result.setErrors(bindingResult.getAllErrors());
            } else {
                result.setSuccess(true);
                consumer.accept(result);
            }
            return result;
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
