package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, BigInteger> {
    Optional<User> findByEmail(String email);
}
