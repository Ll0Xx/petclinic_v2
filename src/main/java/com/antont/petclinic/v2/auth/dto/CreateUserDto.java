package com.antont.petclinic.v2.auth.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class CreateUserDto {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Length(min = 3, max = 25)
    private String password;

    @NotNull
    @Length(min = 3, max = 25)
    private String confirmPassword;

    private Boolean isDoctor = false;

    private BigInteger doctorSpecialization;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Boolean getDoctor() {
        return isDoctor;
    }

    public void setDoctor(Boolean doctor) {
        isDoctor = doctor;
    }

    public BigInteger getDoctorSpecialization() {
        return doctorSpecialization;
    }

    public void setDoctorSpecialization(BigInteger doctorSpecialization) {
        this.doctorSpecialization = doctorSpecialization;
    }
}
