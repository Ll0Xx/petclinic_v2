package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.Pet;
import com.antont.petclinic.v2.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, BigInteger> {

    List<Pet> findByOwner(User owner);

    Optional<Pet> findByIdAndOwner(BigInteger id, User owner);
}
