package com.antont.petclinic.v2.dto;

import java.math.BigInteger;

public class IssueDto {

    private BigInteger id;

    private BigInteger doctor;

    private BigInteger pet;

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
