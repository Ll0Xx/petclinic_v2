package com.antont.petclinic.v2.dto;

import java.math.BigInteger;

public class PetDto {
    private BigInteger id;
    private String name;
    private BigInteger petType;

    public PetDto() {

    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getPetType() {
        return petType;
    }

    public void setPetType(BigInteger petType) {
        this.petType = petType;
    }
}
