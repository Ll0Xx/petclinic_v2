package com.antont.petclinic.v2.dto;

import com.antont.petclinic.v2.validation.annotation.Doctor;
import com.antont.petclinic.v2.validation.annotation.Pet;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigInteger;

public class IssueDto {

    private BigInteger id;

    @Doctor
    private BigInteger doctor;

    @Pet
    private BigInteger pet;

    @NotBlank
    @Size(min = 3, max = 255)
    private String description;

    public IssueDto() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getDoctor() {
        return doctor;
    }

    public void setDoctor(BigInteger doctor) {
        this.doctor = doctor;
    }

    public BigInteger getPet() {
        return pet;
    }

    public void setPet(BigInteger pet) {
        this.pet = pet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
