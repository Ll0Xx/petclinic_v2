package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.DoctorSpecialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface DoctorSpecializationRepository extends JpaRepository<DoctorSpecialization, BigInteger> {
}
