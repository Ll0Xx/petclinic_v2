package com.antont.petclinic.v2.db.entity;

import com.antont.petclinic.v2.db.entity.base.NameEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "pet")
public class Pet extends NameEntity {

    @ManyToOne
    @JoinColumn(name = "Type")
    private PetType petType;

    @ManyToOne
    @JoinColumn(name = "Owner")
    private User owner;

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owned) {
        this.owner = owned;
    }
}
