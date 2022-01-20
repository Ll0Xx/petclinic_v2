package com.antont.petclinic.v2.validation;

import org.springframework.validation.ObjectError;

import java.util.List;

public class ValidationResult {

    private Boolean success;
    private List<ObjectError> errors;

    public ValidationResult() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjectError> errors) {
        this.errors = errors;
    }
}
