package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.PetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface PetTypeRepository extends JpaRepository<PetType, BigInteger> {
}
