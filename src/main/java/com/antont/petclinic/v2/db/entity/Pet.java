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
    private User owned;

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public User getOwned() {
        return owned;
    }

    public void setOwned(User owned) {
        this.owned = owned;
    }
}
