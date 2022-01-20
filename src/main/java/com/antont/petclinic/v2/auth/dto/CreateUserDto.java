package com.antont.petclinic.v2.auth.dto;

import com.antont.petclinic.v2.validation.annotation.FieldsValueMatch;
import com.antont.petclinic.v2.validation.annotation.Password;
import com.antont.petclinic.v2.validation.annotation.UniqueEmail;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
        )
})
public class CreateUserDto {
    @NotNull
    @NotBlank
    @UniqueEmail
    @Email
    private String email;

    @NotNull
    @Password
    @Length(min = 8, max = 25)
    private String password;

    @NotNull
    @Password
    @Length(min = 8, max = 25)
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
