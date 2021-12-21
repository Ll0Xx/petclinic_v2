package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface RoleRepository extends JpaRepository<Role, BigInteger> {
}
