package com.antont.petclinic.v2.entity.base;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class NameEntity extends IdEntity{

    @Column(name = "Name")
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

