package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface DoctorRepository extends JpaRepository<Doctor, BigInteger> {
}
