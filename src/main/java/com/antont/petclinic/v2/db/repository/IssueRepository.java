package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.Issue;
import com.antont.petclinic.v2.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, BigInteger> {
    List<Issue> findByPetOwner(User owner);
}
