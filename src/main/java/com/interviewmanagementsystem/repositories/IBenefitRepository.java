package com.interviewmanagementsystem.repositories;

import com.ninja_in_pyjamas.entities.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface IBenefitRepository extends JpaRepository<Benefit, UUID> {

    // // Trong BenefitRepository
    // @Query("SELECT b FROM Benefit b WHERE b.name IN :names")
    // Set<Benefit> findByNameIn(@Param("names") Set<String> names);
    Set<Benefit> findByNameIn(Set<String> names);
    
}
