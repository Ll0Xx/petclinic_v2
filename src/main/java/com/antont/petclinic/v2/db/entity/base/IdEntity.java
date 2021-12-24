package com.antont.petclinic.v2.db.entity.base;

import javax.persistence.*;
import java.math.BigInteger;

@MappedSuperclass
public class IdEntity {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }
}
