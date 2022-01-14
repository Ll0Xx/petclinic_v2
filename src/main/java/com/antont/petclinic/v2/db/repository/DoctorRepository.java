package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.Doctor;
import com.antont.petclinic.v2.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, BigInteger> {

    List<Doctor> findFirst10ByOrderById();

    Optional<Doctor> findByUser(User user);
}
