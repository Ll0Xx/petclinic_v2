package com.antont.petclinic.v2.db.repository;

import com.antont.petclinic.v2.db.entity.Issue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IssueRepository extends JpaRepository<Issue, BigInteger> {
}
