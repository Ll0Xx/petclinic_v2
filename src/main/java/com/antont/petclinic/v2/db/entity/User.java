package com.antont.petclinic.v2.db.entity;

import com.antont.petclinic.v2.db.entity.base.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "user")
public class User extends IdEntity {

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    @Column(name = "Email")
    private String email;

    @JsonIgnore
    @Column(name = "Password")
    private String password;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

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
}
