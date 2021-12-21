package com.antont.petclinic.v2.db.entity;

import com.antont.petclinic.v2.db.entity.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "issue")
public class Issue extends IdEntity {

    @ManyToOne
    @JoinColumn(name = "Doctor")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "Pet")
    private Pet pet;

    @Column(name = "Description")
    private String Description;

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
