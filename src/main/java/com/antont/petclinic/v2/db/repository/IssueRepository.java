package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.Doctor;
import com.antont.petclinic.v2.db.entity.Issue;
import com.antont.petclinic.v2.db.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, BigInteger> {
    List<Issue> findByPetOwner(User owner);

    List<Issue> findByDoctor(Doctor doctor);

    List<Issue> findByPetNameLike(String owner);

    Page<Issue> findAllByPetOwner(User user, Pageable pageable);
}
