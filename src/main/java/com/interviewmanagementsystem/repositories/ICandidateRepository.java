package com.interviewmanagementsystem.repositories;

import com.ninja_in_pyjamas.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICandidateRepository extends JpaRepository<Candidate, UUID>, JpaSpecificationExecutor<Candidate> {
    Candidate findByEmailOrPhoneNumber(String email, String phoneNumber);

    Candidate findByFullName(String fullName);

    List<Candidate> findByFullNameContainingIgnoreCase(String fullName);
}
