package com.antont.petclinic.v2.entity;

import com.antont.petclinic.v2.entity.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "user")
public class User extends IdEntity {

    @ManyToOne
    @JoinColumn(name = "role")
    private Role role;

    @Column(name = "Email")
    private String Email;

    @Column(name = "Password")
    private String password;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
