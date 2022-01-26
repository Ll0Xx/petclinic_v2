package com.antont.petclinic.v2.dto;

import com.antont.petclinic.v2.validation.annotation.PetType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigInteger;

public class PetDto {
    private BigInteger id;

    @Size(min = 3, max = 20)
    @NotBlank

    private String name;

    @NotNull
    @PetType
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
