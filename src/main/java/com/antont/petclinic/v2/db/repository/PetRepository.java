package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, BigInteger> {

    List<Pet> findByOwner(User owner);
}
