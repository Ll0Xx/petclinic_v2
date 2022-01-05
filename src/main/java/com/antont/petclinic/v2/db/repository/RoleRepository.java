package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, BigInteger> {
    Optional<Role> findByName(String name);
}
